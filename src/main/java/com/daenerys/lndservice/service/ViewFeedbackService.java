package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.feedback.ViewFeedbackResponse;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;

import java.util.List;
import java.util.Optional;

public interface ViewFeedbackService {

    List<FeedbackFormEntity> fetchFeedbackForms(Optional<FeedbackFormTypeEnum> filter);
    Optional<FeedbackFormEntity> findById(Long id);
    List<ViewFeedbackResponse> searchFeedbackForms(List<ViewFeedbackResponse> feedbackResponses, String searchFilter);
    List<ViewFeedbackResponse> filterFeedbackFormByDate(List<ViewFeedbackResponse> feedbackResponses, String to, String from);
}
