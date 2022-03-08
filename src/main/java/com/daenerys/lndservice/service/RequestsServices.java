package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.request.DetailedRequestResponse;
import com.daenerys.lndservice.dto.request.RequestResponse;
import com.daenerys.lndservice.dto.request.AllRequestsResponse;
import com.daenerys.lndservice.dto.request.SearchRequest;
import java.util.List;

public interface RequestsServices {
    List<RequestResponse> getAllRequests();
    List<RequestResponse> getSearchRequests(SearchRequest searchRequest);
    DetailedRequestResponse getDetailedRequest(String type, Long id);
    RequestResponse approveRequest(String type, Long id);
    RequestResponse declineRequest(String type, Long id, String declineReason);

    List<AllRequestsResponse> getRequestsOfCurrentUser(String userEmail);
}
