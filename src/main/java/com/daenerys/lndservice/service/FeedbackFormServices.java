package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.feedback.FeedbackFormRequest;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedbackFormServices {
    void postRequest(FeedbackFormRequest feedbackFormRequest, String bearerToken);
    List<FeedbackFormEntity> fetchFeedbackForms(String bearerToken, Optional<FeedbackFormTypeEnum> filter, Pageable pageable);
    Optional<FeedbackFormEntity> findById(Long id);
}
