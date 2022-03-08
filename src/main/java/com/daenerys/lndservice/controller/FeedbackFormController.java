package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.feedback.FeedbackFormListResponse;
import com.daenerys.lndservice.dto.feedback.FeedbackFormRequest;
import com.daenerys.lndservice.dto.feedback.FeedbackFormResponse;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.daenerys.lndservice.service.FeedbackFormServices;
import com.daenerys.lndservice.service.UserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.daenerys.lndservice.config.SwaggerConfig.FEEDBACK_TAG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/lnd/feedback")
@RequiredArgsConstructor
@Api(tags = FEEDBACK_TAG)
public class FeedbackFormController {

    private final FeedbackFormServices feedbackFormServices;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;

    @ApiOperation(value = "Post Feedback Form", notes =
            "Create a feedback form and persist in database.", tags = {FEEDBACK_TAG})
    @PostMapping("/request")
    public void postRequest(@RequestHeader(AUTHORIZATION) String bearerToken,
                            @Valid @RequestBody FeedbackFormRequest request){
        FeedbackFormRequest feedback = new FeedbackFormRequest();
        BeanUtils.copyProperties(request,feedback);
        feedbackFormServices.postRequest(feedback, bearerToken);
    }

    @ApiOperation(value = "View All Feedback Forms", notes =
            "Returns a list of feedback forms. Filter can be done using \"feedbackFormType\"as query parameters.\n" +
                    "Pagination using \"page\" and \"size\". Default pagination size is 10.", tags = {FEEDBACK_TAG})
    @GetMapping("/all")
    public List<FeedbackFormListResponse> fetchRequests(@RequestHeader(AUTHORIZATION) String bearerToken,
                                                        @RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<Integer> limit,
                                                        @RequestParam Optional<String> sort,
                                                        @RequestParam Optional<String> direction,
                                                        @RequestParam Optional<String> filter) {
        Pageable pageable = getPageable(page, limit, sort, direction);
        Optional<FeedbackFormTypeEnum> filterEnum = Optional.empty();
        if (filter.isPresent()) {
            if (FeedbackFormTypeEnum.getFeedbackFormTypeFromString(filter.get()) == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            filterEnum = Optional.ofNullable(FeedbackFormTypeEnum.getFeedbackFormTypeFromString(filter.get()));
        }
        List<FeedbackFormEntity> feedbackFormEntities = feedbackFormServices.fetchFeedbackForms(bearerToken, filterEnum, pageable);
        return feedbackFormEntities.stream().map(this::mapToFeedbackFormListResponse).collect(Collectors.toList());
    }

    @ApiOperation(value = "View a Feedback Form", notes =
            "Returns a feedback form with the specific id", tags = {FEEDBACK_TAG})
    @GetMapping(path = "/{id}")
    public FeedbackFormResponse getRequest(@PathVariable Long id,
                                           @RequestHeader(AUTHORIZATION) String bearerToken) {
        FeedbackFormResponse returnValue = new FeedbackFormResponse();
        Optional<FeedbackFormEntity> data = feedbackFormServices.findById(id);
        FeedbackFormEntity feedbackFormEntity = data.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        FeedbackFormResponse response = mapToFeedbackFormResponse(feedbackFormEntity);
        response.setPeerEvaluator(userDetailsService.fetchUserDetails(bearerToken).getFullName());
        return response;
    }

    private Pageable getPageable(Optional<Integer> page, Optional<Integer> limit, Optional<String> sort, Optional<String> direction) {
        Sort.Direction sortDirection = direction.map(Sort.Direction::fromString).orElse(Sort.Direction.DESC);
        return PageRequest.of(page.orElse(0),
                limit.orElse(10),
                sortDirection,
                sort.orElse("createdAt"));
    }

    private FeedbackFormListResponse mapToFeedbackFormListResponse(FeedbackFormEntity feedbackForm) {
        FeedbackFormListResponse response = mapper.map(feedbackForm, FeedbackFormListResponse.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        response.setCreationDate(feedbackForm.getCreatedAt().format(formatter));
        response.setFeedbackFormType(feedbackForm.getFeedbackFormType().toString());
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
}
