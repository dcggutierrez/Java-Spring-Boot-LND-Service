package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.feedback.FeedbackFormResponse;
import com.daenerys.lndservice.dto.feedback.ViewFeedbackResponse;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.service.FeedbackFormServices;
import com.daenerys.lndservice.service.ViewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lnd/view")
@RequiredArgsConstructor
public class ViewFeedbackController {

    private final FeedbackFormServices feedbackFormServices;
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final ViewFeedbackService viewFeedbackService;


    @GetMapping(path = "/all")
    public List<ViewFeedbackResponse> fetchRequests(@RequestParam Optional<String> filter,
                                                    @RequestParam(name ="search", required = false) String search,
                                                    @RequestParam(name ="to", required = false) String to,
                                                    @RequestParam(name ="from", required = false) String from) {

        List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) employeeRepository.findAll();
        Optional<FeedbackFormTypeEnum> filterEnum = Optional.empty();
        if (filter.isPresent()) {
            if (FeedbackFormTypeEnum.getFeedbackFormTypeFromString(filter.get()) == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            filterEnum = Optional.ofNullable(FeedbackFormTypeEnum.getFeedbackFormTypeFromString(filter.get()));
        }

        List<FeedbackFormEntity> feedbackFormEntities = viewFeedbackService.fetchFeedbackForms(filterEnum);
        List<ViewFeedbackResponse> viewFeedbackResponses = feedbackFormEntities.stream().map(this::mapToViewFeedbackFormResponse)
                .sorted(Comparator.comparing(ViewFeedbackResponse::getCreationDate)).collect(Collectors.toList());
        this.setFullName(viewFeedbackResponses);
        List<ViewFeedbackResponse> dateFiltered = viewFeedbackService.filterFeedbackFormByDate(viewFeedbackResponses, to, from);
        return viewFeedbackService.searchFeedbackForms(dateFiltered, search);
    }


    @GetMapping(path = "/{id}")
    public FeedbackFormResponse getRequest(@PathVariable Long id,
                                           FeedbackFormEntity feedbackForm) {
        FeedbackFormResponse returnValue = new FeedbackFormResponse();
        Optional<FeedbackFormEntity> data = feedbackFormServices.findById(id);
        FeedbackFormEntity feedbackFormEntity = data.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        FeedbackFormResponse response = mapToFeedbackFormResponse(feedbackFormEntity);
        response.setPeerEvaluator(employeeRepository.findByCompanyEmail(feedbackForm.getEmployee()).get().getFullName());
        return response;
    }

    private ViewFeedbackResponse mapToViewFeedbackFormResponse(FeedbackFormEntity feedbackForm) {

        ViewFeedbackResponse response = mapper.map(feedbackForm, ViewFeedbackResponse.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        response.setCreationDate(feedbackForm.getCreatedAt().format(formatter));
        response.setFeedbackFormType(feedbackForm.getFeedbackFormType());
        return response;
    }

    private FeedbackFormResponse mapToFeedbackFormResponse(FeedbackFormEntity feedbackForm) {
        FeedbackFormResponse response = mapper.map(feedbackForm, FeedbackFormResponse.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        response.setDateOfSession(feedbackForm.getDateOfSession().format(formatter));
        response.setFeedbackFormType(feedbackForm.getFeedbackFormType().toString());
        response.setDateSubmitted(feedbackForm.getCreatedAt().toLocalDate().format(formatter));
        return response;
    }
    private void setFullName(List<ViewFeedbackResponse> viewFeedbackResponses){
        List<String> companyEmails = viewFeedbackResponses.stream().map(ViewFeedbackResponse::getEmployee).collect(Collectors.toList());
        List<EmployeeEntity> employeesWithFeedback = employeeRepository.findByCompanyEmailIn(companyEmails);
        for (ViewFeedbackResponse viewFeedbackResponse : viewFeedbackResponses) {
            viewFeedbackResponse.setFullName(this.mapFullName(employeesWithFeedback, viewFeedbackResponse));
        }
    }
    private String mapFullName(List<EmployeeEntity> employeesWithFeedback, ViewFeedbackResponse viewFeedbackResponse){
        List<String> employeeFullName = employeesWithFeedback.stream()
                .filter(employee -> employee.getCompanyEmail().equals(viewFeedbackResponse.getEmployee()))
                .map(EmployeeEntity::getFullName)
                .collect(Collectors.toList());
        if (employeeFullName.size()==1){
            return  employeeFullName.get(0);
        } else{return null;}
    }
}
