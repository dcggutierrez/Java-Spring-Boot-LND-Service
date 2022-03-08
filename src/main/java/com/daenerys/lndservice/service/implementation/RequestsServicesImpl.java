package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.request.DetailedRequestResponse;
import com.daenerys.lndservice.dto.request.RequestResponse;
import com.daenerys.lndservice.dto.request.AllRequestsResponse;
import com.daenerys.lndservice.dto.request.SearchRequest;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.repository.lnd.EmployeeTrainingRepository;
import com.daenerys.lndservice.repository.lnd.TwcRepository;

import com.daenerys.lndservice.service.RequestsServices;
import com.daenerys.lndservice.service.TwcServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestsServicesImpl implements RequestsServices {

    private final TwcRepository twcRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeTrainingRepository employeeTrainingRepository;
    private final ModelMapper mapper;

    @Override
    public List<RequestResponse> getAllRequests() {
        List<TwcEntity> twcRequests = twcRepository.findAll();
        List<RequestResponse> twcRequestResponses = this.mapTwcRequests(twcRequests);
        List<RequestResponse> allRequests = new ArrayList<>(twcRequestResponses);
        List<EmployeeTrainingEntity> employeeTrainingRequests = employeeTrainingRepository.findAll();
        List<RequestResponse> employeeTrainingRequestResponses = this.mapEmployeeTrainingRequests(employeeTrainingRequests);
        allRequests.addAll(employeeTrainingRequestResponses);
        sortRequests(allRequests);
        return allRequests;
    }

    @Override
    public List<RequestResponse> getSearchRequests(SearchRequest filter) {
        String requestType = filter.getRequestType();
        String search = filter.getSearch();
        String from = filter.getFrom();
        String to = filter.getTo();
        List<RequestResponse> requests = this.filterRequestType(requestType);
        List<RequestResponse> searchResult = searchRequests(search, requests);
        List<RequestResponse> dateResult =filterRequestsByDate(from, to, searchResult);
        sortRequests(dateResult);
        return dateResult;
    }

    @Override
    public DetailedRequestResponse getDetailedRequest(String type, Long id){
        if (type.equals("The Weekday Catch-Up Request")){
            return this.getDetailedTwcRequest(id);
        } else {
            return this.getDetailedEmployeeTrainingRequest(id);
        }
    }

    @Override
    public RequestResponse approveRequest(String type, Long id) {
        if (type.equals("The Weekday Catch-Up Request")){
            Optional<TwcEntity> twcRequest = twcRepository.findById(id);
            if(twcRequest.isPresent()){
                twcRequest.get().setIsApproved(true);
                TwcEntity updatedTwcRequest = twcRepository.save(twcRequest.get());
                return mapper.map(updatedTwcRequest, RequestResponse.class);
            } else {return null;}
        } else {
            Optional<EmployeeTrainingEntity> employeeTrainingRequest = employeeTrainingRepository.findById(id);
            if(employeeTrainingRequest.isPresent()){
                employeeTrainingRequest.get().setIsApproved(true);
                EmployeeTrainingEntity updatedEmployeeTrainingRequest = employeeTrainingRepository.save(employeeTrainingRequest.get());
                return mapper.map(updatedEmployeeTrainingRequest, RequestResponse.class);
            } else{return null;}
        }
    }

    @Override
    public RequestResponse declineRequest(String type, Long id, String declineReason) {
        if (type.equals("The Weekday Catch-Up Request")){
            Optional<TwcEntity> twcRequest = twcRepository.findById(id);
            if(twcRequest.isPresent()){
                twcRequest.get().setIsApproved(false);
                twcRequest.get().setDeclineReason(declineReason);
                TwcEntity updatedTwcRequest = twcRepository.save(twcRequest.get());
                return mapper.map(updatedTwcRequest, RequestResponse.class);
            } else {return null;}
        } else {
            Optional<EmployeeTrainingEntity> employeeTrainingRequest = employeeTrainingRepository.findById(id);
            if(employeeTrainingRequest.isPresent()){
                employeeTrainingRequest.get().setIsApproved(false);
                employeeTrainingRequest.get().setDeclineReason(declineReason);
                EmployeeTrainingEntity updatedEmployeeTrainingRequest = employeeTrainingRepository.save(employeeTrainingRequest.get());
                return mapper.map(updatedEmployeeTrainingRequest, RequestResponse.class);
            } else{return null;}
        }
    }

    @Override
    public List<AllRequestsResponse> getRequestsOfCurrentUser(String userEmail) {
        List<TwcEntity> twcRequests = twcRepository.findByPostedBy(userEmail, Pageable.unpaged());
        List<EmployeeTrainingEntity> employeeTrainingRequests = employeeTrainingRepository.findByPostedBy(userEmail, Pageable.unpaged());
        List<AllRequestsResponse> allReqResponse = new ArrayList<>();
        for (TwcEntity twcRequest : twcRequests){
            AllRequestsResponse twcReq = mapper.map(twcRequest, AllRequestsResponse.class);
            allReqResponse.add(twcReq);
        }

        for (EmployeeTrainingEntity employeeTrainingRequest : employeeTrainingRequests){
            AllRequestsResponse employeeTrainingReq = mapper.map(employeeTrainingRequest, AllRequestsResponse.class);
            allReqResponse.add(employeeTrainingReq);
        }

        allReqResponse.sort(Comparator.comparing(AllRequestsResponse::getCreatedAt).reversed());
        return allReqResponse;


    }

    private DetailedRequestResponse getDetailedTwcRequest(Long id){
        Optional<TwcEntity> twcRequest = twcRepository.findById(id);

        if(twcRequest.isPresent()){
            List<EmployeeEntity> speakers = employeeRepository.findByIdIn(twcRequest.get().getSpeakerIds());
            DetailedRequestResponse detailedTwcRequest = mapper.map(twcRequest.get(), DetailedRequestResponse.class);
            detailedTwcRequest.setSpeakers(speakers.stream().map(EmployeeEntity::getFullName).collect(Collectors.toList()));
            if (employeeRepository.findByCompanyEmail(detailedTwcRequest.getPostedBy()).isPresent()) {
                detailedTwcRequest.setRequestedBy(employeeRepository.findByCompanyEmail(detailedTwcRequest.getPostedBy()).get().getFullName());
            }
            return detailedTwcRequest;
        } else {return null; }
    }

    private DetailedRequestResponse getDetailedEmployeeTrainingRequest(Long id){
        Optional<EmployeeTrainingEntity> employeeTrainingRequest = employeeTrainingRepository.findById(id);
        if(employeeTrainingRequest.isPresent()){
            DetailedRequestResponse detailedEmployeeTrainingRequest = mapper.map(employeeTrainingRequest.get(), DetailedRequestResponse.class);
            if (employeeRepository.findByCompanyEmail(detailedEmployeeTrainingRequest.getPostedBy()).isPresent()) {
                detailedEmployeeTrainingRequest.setRequestedBy(employeeRepository.findByCompanyEmail(detailedEmployeeTrainingRequest.getPostedBy()).get().getFullName());
            }
            return detailedEmployeeTrainingRequest;
        } else {return null; }
    }

    private List<RequestResponse> filterRequestType(String requestType) {
        if (!Objects.isNull(requestType)){
            if (requestType.equals("The Weekday Catch-Up Request")){
                List<TwcEntity> twcRequests = twcRepository.findAll();
                return this.mapTwcRequests(twcRequests);
            } else if (requestType.equals("Employee Training Request")){
                List<EmployeeTrainingEntity> employeeTrainingRequests = employeeTrainingRepository.findAll();
                return this.mapEmployeeTrainingRequests(employeeTrainingRequests);
            } else {return this.getAllRequests();}
        } else { return this.getAllRequests();}
    }

    private static List<RequestResponse> searchRequests(String search, List<RequestResponse> requests) {
        if (!Objects.isNull(search)) {
            return requests
                    .stream().filter(request ->
                            (request.getTitle().toLowerCase().contains(search.toLowerCase()) || request.getRequestType().toLowerCase().contains(search.toLowerCase()))
                    ).collect(Collectors.toList());
        } else { return requests;}
    }

    private static List<RequestResponse> filterRequestsByDate(String from, String to, List<RequestResponse> requests) {
        if (!Objects.isNull(from) && !Objects.isNull(to)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateFrom = LocalDate.parse(from, format);
            LocalDate dateTo = LocalDate.parse(to, format);
            return requests.stream().filter((request -> (request.getDateSubmitted().isAfter(dateFrom.minusDays(1))
                            && request.getDateSubmitted().isBefore(dateTo.plusDays(1)))))
                    .collect(Collectors.toList());
        } else {return requests;}
    }

    private static void sortRequests (List<RequestResponse> requests){
        requests.sort(Comparator.comparing(RequestResponse::getDateSubmitted).reversed().thenComparing(RequestResponse::getRequestType).thenComparing(RequestResponse::getTitle));
    }

    private List<RequestResponse> mapTwcRequests(List<TwcEntity> twcRequests){
        return twcRequests
                .stream()
                .map(req -> mapper.map(req, RequestResponse.class))
                .collect(Collectors.toList());
    }
    private List<RequestResponse> mapEmployeeTrainingRequests(List<EmployeeTrainingEntity> employeeTrainingRequests){
        return employeeTrainingRequests.stream()
                .map(req -> mapper.map(req, RequestResponse.class))
                .collect(Collectors.toList());
    }
}
