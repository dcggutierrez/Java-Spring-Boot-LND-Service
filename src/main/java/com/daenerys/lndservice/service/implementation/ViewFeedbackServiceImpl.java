package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.feedback.ViewFeedbackResponse;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.daenerys.lndservice.repository.lnd.FeedbackFormRepository;
import com.daenerys.lndservice.service.ViewFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ViewFeedbackServiceImpl implements ViewFeedbackService {

    private final FeedbackFormRepository feedbackFormRepository;

    public List<FeedbackFormEntity> fetchFeedbackForms(Optional<FeedbackFormTypeEnum> filter) {

        List<FeedbackFormEntity> allFeedbackForms = feedbackFormRepository.findAll();
        return filter.map(feedbackFormTypeEnum -> allFeedbackForms.stream()
                .filter(form -> form.getFeedbackFormType() == feedbackFormTypeEnum)
                .collect(Collectors.toList())).orElse(allFeedbackForms);
    }

    @Override
    public Optional<FeedbackFormEntity> findById(Long id) {
        return feedbackFormRepository.findById(id);
    }

    @Override
    public List<ViewFeedbackResponse> searchFeedbackForms(List<ViewFeedbackResponse> feedbackForms, String search){
        if (!Objects.isNull(search)) {
            return feedbackForms
                    .stream().filter(feedback ->{
                        if (Objects.isNull(feedback.getFullName())){
                            return feedback.getFeedbackFormType().getFeedbackFormType().toLowerCase().contains(search.toLowerCase());
                        } else {
                            return feedback.getFullName().toLowerCase().contains(search.toLowerCase()) || feedback.getFeedbackFormType().getFeedbackFormType().toLowerCase().contains(search.toLowerCase());
                        }
                            }
                    ).collect(Collectors.toList());
        } else { return feedbackForms;}
    }

    @Override
    public List<ViewFeedbackResponse> filterFeedbackFormByDate(List<ViewFeedbackResponse> feedbackResponses, String to, String from) {
        if (!Objects.isNull(from) && !Objects.isNull(to)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            LocalDate dateFrom = LocalDate.parse(from, format);
            LocalDate dateTo = LocalDate.parse(to, format);
            return feedbackResponses.stream().filter(feedbackResponse -> (LocalDate.parse(feedbackResponse.getCreationDate(), formatter).isAfter(dateFrom.minusDays(1))
                    && LocalDate.parse(feedbackResponse.getCreationDate(), formatter).isBefore(dateTo.plusDays(1)))).collect(Collectors.toList());
        } else {
            return feedbackResponses;
        }
    }
}

