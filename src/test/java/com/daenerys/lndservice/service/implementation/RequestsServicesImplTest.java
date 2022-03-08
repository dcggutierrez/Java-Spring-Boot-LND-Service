package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.request.DetailedRequestResponse;
import com.daenerys.lndservice.dto.request.RequestResponse;
import com.daenerys.lndservice.dto.request.SearchRequest;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.repository.lnd.EmployeeTrainingRepository;
import com.daenerys.lndservice.repository.lnd.TwcRepository;
import com.daenerys.lndservice.service.TwcServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RequestsServicesImplTest {
    @Mock
    private EmployeeEntity employeeEntity;

    @Mock
    private TwcRepository twcRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeTrainingRepository employeeTrainingRepository;


    @Test
    void getAllRequests_ShouldCallDatabase_ShouldReturnListOfAllRequests() {
        //given
        TwcEntity twcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,05,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(1,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                new ArrayList<>(),
                "string",
                "string");
        List<TwcEntity> twcRequests = new ArrayList<>();
        twcRequests.add(twcRequest);

        EmployeeTrainingEntity employeeTraining = new EmployeeTrainingEntity(
                2L,
                LocalDateTime.of(LocalDate.of(2022,05,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,06,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(1,30),
                "string",
                true,
                "Employee Training Request",
                "string");
        EmployeeTrainingEntity employeeTraining2 = new EmployeeTrainingEntity(
                3L,
                LocalDateTime.of(LocalDate.of(2022,04,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,06,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(1,30),
                "string",
                true,
                "Employee Training Request",
                "string");
        EmployeeTrainingEntity employeeTraining3 = new EmployeeTrainingEntity(
                4L,
                LocalDateTime.of(LocalDate.of(2022,05,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,06,13), LocalTime.of(06,30)),
                "string",
                "string1",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(1,30),
                "string",
                true,
                "Employee Training Request",
                "string");

        List<RequestResponse> twcRequestResponses = Arrays.asList(new RequestResponse(1L,
                "The Weekday Catch-Up Request",
                "string",
                LocalDate.of(2022,05,13),
                true));
        List<RequestResponse> employeeTrainingRequestResponses = Arrays.asList(new RequestResponse(2L,
                "Employee Training Request",
                "string",
                LocalDate.of(2022,05,13),
                true));
        List<RequestResponse> employeeTrainingRequestResponses2 = Arrays.asList(new RequestResponse(3L,
                "Employee Training Request",
                "string",
                LocalDate.of(2022,04,13),
                true));
        List<RequestResponse> employeeTrainingRequestResponses3 = Arrays.asList(new RequestResponse(4L,
                "Employee Training Request",
                "string1",
                LocalDate.of(2022,05,13),
                true));
        List<EmployeeTrainingEntity> employeeTrainingRequests = Arrays.asList(employeeTraining,employeeTraining2,employeeTraining3);
        List<RequestResponse> expectedRequests = new ArrayList<>(employeeTrainingRequestResponses);
        expectedRequests.addAll(employeeTrainingRequestResponses3);
        expectedRequests.addAll(twcRequestResponses);
        expectedRequests.addAll(employeeTrainingRequestResponses2);


        given(twcRepository.findAll()).willReturn(twcRequests);
        given(employeeTrainingRepository.findAll()).willReturn(employeeTrainingRequests);

        //when
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RequestsServicesImpl requestsServices = new RequestsServicesImpl(twcRepository, employeeRepository, employeeTrainingRepository, mapper);
        List<RequestResponse> actualRequests = requestsServices.getAllRequests();

        //then
        assertThat(actualRequests).isEqualTo(expectedRequests);
    }

    @Test
    void getSearchRequests_ReturnsListOfRequestsThatSatisfiesTypeSearchDateFilters() {
        //given
        EmployeeTrainingEntity employeeTraining1 = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,6,13), LocalTime.of(06,30)),
                "string",
                "weekends",
                LocalDate.of(2022,9,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                false,
                "Employee Training Request",
                "string");
        TwcEntity twcRequest1 = new TwcEntity(
                2L,
                "string",
                "trainer",
                LocalDateTime.of(LocalDate.of(2022,6,14), LocalTime.of(06,30)),
                LocalDate.of(2022,8,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        TwcEntity twcRequest2 = new TwcEntity(
                3L,
                "string",
                "weekend",
                LocalDateTime.of(LocalDate.of(2022,1,25), LocalTime.of(06,30)),
                LocalDate.of(2022,9,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        TwcEntity twcRequest3 = new TwcEntity(
                4L,
                "string",
                "violet",
                LocalDateTime.of(LocalDate.of(2022,1,26), LocalTime.of(06,30)),
                LocalDate.of(2022,9,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        List<TwcEntity> twcRequests = Arrays.asList(twcRequest1,twcRequest2, twcRequest3);
        List<EmployeeTrainingEntity> employeeTrainingRequests = Arrays.asList(employeeTraining1);

        RequestResponse twc1 = new RequestResponse(2L,
                "The Weekday Catch-Up Request",
                "trainer",
                LocalDate.of(2022,6,14),
                true);
        RequestResponse twc2 = new RequestResponse(3L,
                "The Weekday Catch-Up Request",
                "weekend",
                LocalDate.of(2022,1,25),
                true);
        RequestResponse twc3 = new RequestResponse(4L,
                "The Weekday Catch-Up Request",
                "violet",
                LocalDate.of(2022,1,26),
                true);
        RequestResponse employeeTraining = (new RequestResponse(1L,
                "Employee Training Request",
                "weekends",
                LocalDate.of(2022,5,13),
                false));
        List<RequestResponse> expectedTwcRequests = Arrays.asList(twc1, twc3, twc2);
        List<RequestResponse> expectedFilteredDateRequests = Arrays.asList(twc3,twc2);
        List<RequestResponse> expectedSearchRequests = Arrays.asList(twc1,employeeTraining);
        List<RequestResponse> expectedEmployeeTrainingRequests = Arrays.asList(employeeTraining);

        given(twcRepository.findAll()).willReturn(twcRequests);
        given(employeeTrainingRepository.findAll()).willReturn(employeeTrainingRequests);

        //when
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RequestsServicesImpl requestsServices = new RequestsServicesImpl(twcRepository, employeeRepository, employeeTrainingRepository, mapper);
        SearchRequest filter1 = new SearchRequest("The Weekday Catch-Up Request", null,null,null);
        SearchRequest filter2 = new SearchRequest(null, "train",null,null);
        SearchRequest filter3 = new SearchRequest(null, null,"2022-01-26","2022-01-25");
        SearchRequest filter4 = new SearchRequest("Employee Training Request", null,null,null);
        List<RequestResponse> actualTwcRequests =requestsServices.getSearchRequests(filter1);
        List<RequestResponse> actualSearchRequests =requestsServices.getSearchRequests(filter2);
        List<RequestResponse> actualFilteredDateRequests =requestsServices.getSearchRequests(filter3);
        List<RequestResponse> actualEmployeeTrainingRequests =requestsServices.getSearchRequests(filter4);

        //then
        assertThat(actualTwcRequests).isEqualTo(expectedTwcRequests);
        assertThat(actualEmployeeTrainingRequests).isEqualTo(expectedEmployeeTrainingRequests);
        assertThat(actualSearchRequests).isEqualTo(expectedSearchRequests);
        assertThat(actualFilteredDateRequests).isEqualTo(expectedFilteredDateRequests);
    }


    @Test
    void getDetailedRequest_ShouldGetTwcEntityOrEmployeeTrainingEntityFromDatabase_ShouldReturnDetailedRequestResponse() {
        //given
        EmployeeEntity employee1 = new EmployeeEntity();
        employee1.setCompanyEmail("string");
        employee1.setFirstName("Trial");
        employee1.setLastName("Name");
        TwcEntity twcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,05,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);

        EmployeeTrainingEntity employeeTraining = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,05,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,06,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                false,
                "Employee Training Request",
                "string");
        DetailedRequestResponse expectedEmployeeTrainingRequest = new DetailedRequestResponse(
                1L,
                "Employee Training Request",
                "string",
                "string",
                employee1.getFullName(),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                LocalDate.of(2022,05,13),
                null,
                null,
                null,
                null,
                "string",
                false,
                "string");
        DetailedRequestResponse expectedTwcRequest = new DetailedRequestResponse(
                1L,
                "The Weekday Catch-Up Request",
                "string",
                "string",
                employee1.getFullName(),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                LocalDate.of(2022,05,13),
                "string",
                "string",
                Arrays.asList(employeeEntity.getFullName()),
                Arrays.asList(employeeEntity.getId()),
                "string",
                true,
                null);

        given(twcRepository.findById(1L)).willReturn(Optional.of(twcRequest));
        given(employeeTrainingRepository.findById(1L)).willReturn(Optional.of(employeeTraining));
        given(employeeRepository.findByCompanyEmail("string")).willReturn(Optional.of(employee1));
        given(employeeRepository.findByIdIn(Arrays.asList(employeeEntity.getId()))).willReturn(Arrays.asList(employeeEntity));

        //when
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RequestsServicesImpl requestsServices = new RequestsServicesImpl(twcRepository, employeeRepository, employeeTrainingRepository, mapper);
        DetailedRequestResponse actualEmployeeTrainingRequest = requestsServices.getDetailedRequest("Employee Training Request", 1L);
        DetailedRequestResponse actualTwcRequest = requestsServices.getDetailedRequest("The Weekday Catch-Up Request", 1L);

        //then
        assertThat(actualTwcRequest).isEqualTo(expectedTwcRequest);
        assertThat(actualEmployeeTrainingRequest).isEqualTo(expectedEmployeeTrainingRequest);
    }

    @Test
    void approveRequest_ShouldCallDatabase_ShouldReturnUpdatedRequestResponse(){
        //given
        TwcEntity twcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                null,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        TwcEntity updatedTwcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                true,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        EmployeeTrainingEntity employeeTrainingRequest = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,6,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                null,
                "Employee Training Request",
                null);
        EmployeeTrainingEntity updatedEmployeeTrainingRequest = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,6,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                true,
                "Employee Training Request",
                null);

        RequestResponse expectedUpdatedEmployeeTrainingRequest = (new RequestResponse(1L,
                "Employee Training Request",
                "string",
                LocalDate.of(2022,5,13),
                true));
        RequestResponse expectedUpdatedTwcRequest = (new RequestResponse(1L,
                "The Weekday Catch-Up Request",
                "string",
                LocalDate.of(2022,5,13),
                true));
        given(twcRepository.findById(1L)).willReturn(Optional.of(twcRequest));
        given(employeeTrainingRepository.findById(1L)).willReturn(Optional.of(employeeTrainingRequest));
        given(twcRepository.save(twcRequest)).willReturn(updatedTwcRequest);
        given(employeeTrainingRepository.save(employeeTrainingRequest)).willReturn(updatedEmployeeTrainingRequest);

        //when
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RequestsServicesImpl requestsServices = new RequestsServicesImpl(twcRepository, employeeRepository, employeeTrainingRepository, mapper);
        RequestResponse actualUpdatedTwcRequest = requestsServices.approveRequest("The Weekday Catch-Up Request",1L);
        RequestResponse actualUpdatedEmployeeTrainingRequest = requestsServices.approveRequest("Employee Training Request",1L);

        //then
        assertThat(actualUpdatedTwcRequest).isEqualTo(expectedUpdatedTwcRequest);
        assertThat(actualUpdatedEmployeeTrainingRequest).isEqualTo(expectedUpdatedEmployeeTrainingRequest);
    }

    @Test
    void declineRequest_ShouldCallDatabase_ShouldReturnUpdatedRequestResponse(){
//given
        TwcEntity twcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                null,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                null);
        TwcEntity updatedTwcRequest = new TwcEntity(
                1L,
                "string",
                "string",
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                "string",
                false,
                "The Weekday Catch-Up Request",
                Arrays.asList(employeeEntity.getId()),
                "string",
                "string");
        EmployeeTrainingEntity employeeTrainingRequest = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,6,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                null,
                "Employee Training Request",
                null);
        EmployeeTrainingEntity updatedEmployeeTrainingRequest = new EmployeeTrainingEntity(
                1L,
                LocalDateTime.of(LocalDate.of(2022,5,13), LocalTime.of(06,30)),
                LocalDateTime.of(LocalDate.of(2022,6,13), LocalTime.of(06,30)),
                "string",
                "string",
                LocalDate.of(2022,05,13),
                LocalTime.of(10,45),
                LocalTime.of(13,30),
                "string",
                false,
                "Employee Training Request",
                "string");

        RequestResponse expectedUpdatedEmployeeTrainingRequest = (new RequestResponse(1L,
                "Employee Training Request",
                "string",
                LocalDate.of(2022,5,13),
                false));
        RequestResponse expectedUpdatedTwcRequest = (new RequestResponse(1L,
                "The Weekday Catch-Up Request",
                "string",
                LocalDate.of(2022,5,13),
                false));
        given(twcRepository.findById(1L)).willReturn(Optional.of(twcRequest));
        given(employeeTrainingRepository.findById(1L)).willReturn(Optional.of(employeeTrainingRequest));
        given(twcRepository.save(twcRequest)).willReturn(updatedTwcRequest);
        given(employeeTrainingRepository.save(employeeTrainingRequest)).willReturn(updatedEmployeeTrainingRequest);

        //when
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RequestsServicesImpl requestsServices = new RequestsServicesImpl(twcRepository, employeeRepository, employeeTrainingRepository, mapper);
        RequestResponse actualUpdatedTwcRequest = requestsServices.declineRequest("The Weekday Catch-Up Request",1L, "string");
        RequestResponse actualUpdatedEmployeeTrainingRequest = requestsServices.declineRequest("Employee Training Request",1L, "string");

        //then
        assertThat(actualUpdatedTwcRequest).isEqualTo(expectedUpdatedTwcRequest);
        assertThat(actualUpdatedEmployeeTrainingRequest).isEqualTo(expectedUpdatedEmployeeTrainingRequest);
    }
}