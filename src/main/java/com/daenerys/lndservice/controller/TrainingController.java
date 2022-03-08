package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.request.AllRequestsResponse;
import com.daenerys.lndservice.dto.training.EmployeeTrainingDetailedResponse;
import com.daenerys.lndservice.dto.training.EmployeeTrainingRequest;
import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import com.daenerys.lndservice.service.EmployeeTrainingService;
import com.daenerys.lndservice.service.UserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import static com.daenerys.lndservice.config.SwaggerConfig.TRAINING_TAG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/lnd/training")
@RequiredArgsConstructor
@Api(tags = TRAINING_TAG)
public class TrainingController {

    private final EmployeeTrainingService employeeTrainingService;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;

    @ApiOperation(value = "Create a new training request", notes = "Creates a new Employee Training request for the current user", tags = TRAINING_TAG)
    @PostMapping(path = "/request")
    public void postEmployeeTraining(@RequestHeader(AUTHORIZATION) String bearerToken, @Valid @RequestBody EmployeeTrainingRequest employeeTrainingRequest) {
        EmployeeTrainingEntity newEmployeeTraining = mapper.map(employeeTrainingRequest, EmployeeTrainingEntity.class);
        newEmployeeTraining.setPostedBy(userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail());
        employeeTrainingService.postEmployeeTraining(newEmployeeTraining);
    }

    @ApiOperation(value = "View employee training requests", notes = "Views all employee training requests by email", tags = TRAINING_TAG)
    @GetMapping(path = "/request")
    public List<AllRequestsResponse> viewOwnRequest(@RequestHeader(AUTHORIZATION) String bearerToken, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String userEmail = userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail();
        List<EmployeeTrainingEntity> employeeTrainingResponse = employeeTrainingService.getOwnerRequest(userEmail, pageable);
        return employeeTrainingResponse
                .stream()
                .map(req -> mapper.map(req, AllRequestsResponse.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "View detailed employee training requests", notes = "View a single employee request", tags = TRAINING_TAG)
    @GetMapping(path = "/request/details/{id}")
    public EmployeeTrainingDetailedResponse viewTrainingDetails(@PathVariable("id") Long id){
        return mapper.map(employeeTrainingService.getDetailedRequest(id),EmployeeTrainingDetailedResponse.class);
    }
}
