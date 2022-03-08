package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.request.*;
import com.daenerys.lndservice.service.RequestsServices;
import com.daenerys.lndservice.service.UserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

//@PreAuthorize()
@RestController
@RequestMapping("/api/lnd/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestsServices requestServices;
    private final UserDetailsService userDetailsService;

    @GetMapping("/own")
    public List<AllRequestsResponse> getRequestsOfCurrentUser(@RequestHeader(AUTHORIZATION) String bearerToken){
        String userEmail = userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail();
        return requestServices.getRequestsOfCurrentUser(userEmail);
    }
    @GetMapping
    public List<RequestResponse> getRequests(){
        return requestServices.getAllRequests();
    }
    @GetMapping("/search")
    public List<RequestResponse> getSearchRequest(@RequestParam(name ="type", required = false) String type,
                                                  @RequestParam(name ="from", required = false) String from,
                                                  @RequestParam(name ="to", required = false) String to,
                                                  @RequestParam(name ="search", required = false) String search

    ) {
        SearchRequest filter = new SearchRequest(type, search, to, from);
        return requestServices.getSearchRequests(filter);

    }
    @GetMapping("/view/{type}/{requestId}")
    public DetailedRequestResponse getDetailedRequest(@PathVariable String type,
                                                      @PathVariable Long requestId){
        return requestServices.getDetailedRequest(type, requestId);
    }
    @PutMapping("/approve/{type}/{requestId}")
    public RequestResponse approveDetailedRequest(@PathVariable String type, @PathVariable Long requestId){
        return requestServices.approveRequest(type, requestId);
    }

    @PutMapping("/decline")
    public RequestResponse approveDetailedRequest(@Valid @RequestBody DeclineRequest declineRequest){
        String type = declineRequest.getRequestType();
        Long requestId = declineRequest.getRequestId();
        String declineReason = declineRequest.getDeclineReason();
        return requestServices.declineRequest(type, requestId, declineReason);
    }
}
