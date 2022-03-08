package com.daenerys.lndservice.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackFormResponse {

    private Long id;
    private String dateSubmitted;
    private String peerEvaluator;
    private String topicTitle;
    private String feedbackFormType;
    private String dateOfSession;
    private int methodOfDelivery;
    private int knowledgeAndMastery;
    private int relevance;
    private String likeOrDislikeTrainerOrTopic;
    private String additionalFeedbackTrainer;
    private String likeOrDislikeSession;
    private String additionalFeedbackTopicOrOverall;
}
