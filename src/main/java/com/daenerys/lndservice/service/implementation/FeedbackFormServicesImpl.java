package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.feedback.FeedbackFormRequest;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.daenerys.lndservice.repository.lnd.FeedbackFormRepository;
import com.daenerys.lndservice.service.FeedbackFormServices;
import com.daenerys.lndservice.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackFormServicesImpl implements FeedbackFormServices {

    private final FeedbackFormRepository feedbackFormRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public void postRequest(FeedbackFormRequest feedbackFormRequest, String bearerToken) {
        FeedbackFormEntity feedbackFormEntity = new FeedbackFormEntity();
        BeanUtils.copyProperties(feedbackFormRequest, feedbackFormEntity);
        feedbackFormEntity.setEmployee(userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail());
        feedbackFormRepository.save(feedbackFormEntity);
    }

    @Override
    public List<FeedbackFormEntity> fetchFeedbackForms(String bearerToken,
                                                       Optional<FeedbackFormTypeEnum> filter,
                                                       Pageable pageable) {
        String employee = userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail();
        List<FeedbackFormEntity> allFeedbackForms = feedbackFormRepository.findByEmployee(employee, pageable);
        return filter.map(feedbackFormTypeEnum -> allFeedbackForms.stream()
                .filter(form -> form.getFeedbackFormType() == feedbackFormTypeEnum)
                .collect(Collectors.toList())).orElse(allFeedbackForms);
    }

    @Override
    public Optional<FeedbackFormEntity> findById(Long id) {
        return feedbackFormRepository.findById(id);
    }
}
