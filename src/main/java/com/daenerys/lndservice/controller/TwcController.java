package com.daenerys.lndservice.controller;


import com.daenerys.lndservice.dto.request.AllRequestsResponse;
import com.daenerys.lndservice.dto.twc.TwcReqDetailedResponse;
import com.daenerys.lndservice.dto.twc.TwcRequest;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.service.TwcServices;
import com.daenerys.lndservice.service.UserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.daenerys.lndservice.config.SwaggerConfig.TWC_TAG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/lnd/twc")
@RequiredArgsConstructor
@Api(tags = TWC_TAG)
public class TwcController {

    private final TwcServices twcServices;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;

    @PostMapping(path = "/request")
    @ApiOperation(value = "Create a new TWC Request", notes = "Creates a new TWC Request for the current user", tags = {TWC_TAG})
    public void postRequest(@RequestHeader(AUTHORIZATION) String bearerToken, @Valid @RequestBody TwcRequest request) {
        TwcEntity newTwcRequest = mapper.map(request, TwcEntity.class);
        newTwcRequest.setPostedBy(userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail());
        twcServices.postRequest(newTwcRequest);
    }

    @GetMapping(path = "/request")
    @ApiOperation(value = "Get TWC Requests", notes = "Gets all TWC Requests by email", tags = {TWC_TAG})
    public List<AllRequestsResponse> getRequests(@RequestHeader(AUTHORIZATION) String bearerToken, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable sortedByDateSubmitted = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String userEmail = getUserEmail(bearerToken);
        List<TwcEntity> twcRequests = twcServices.getRequests(userEmail, sortedByDateSubmitted);
        return twcRequests
                .stream()
                .map(req -> mapper.map(req, AllRequestsResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/request/details/{twc_req_id}")
    @ApiOperation(value = "Get details of TWC Request ", notes = "Gets details of TWC Request by id", tags = {TWC_TAG})
    public TwcReqDetailedResponse getDetailedRequest(@RequestHeader(AUTHORIZATION) String bearerToken, @PathVariable("twc_req_id") Long twcReqId) {
        String userEmail = getUserEmail(bearerToken);
        TwcEntity twcRequest = twcServices.getRequest(twcReqId,userEmail);
        TwcReqDetailedResponse twcReqDetailedResponse = mapper.map(twcRequest, TwcReqDetailedResponse.class);
        List<String> speakerNames = employeeRepository.findByIdIn(twcRequest.getSpeakerIds())
                .stream()
                .map(EmployeeEntity::getFullName)
                .collect(Collectors.toList());
        twcReqDetailedResponse.setSpeakers(speakerNames);
        return twcReqDetailedResponse;
    }

    private String getUserEmail(String bearerToken) {
        return userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail();
    }

}
