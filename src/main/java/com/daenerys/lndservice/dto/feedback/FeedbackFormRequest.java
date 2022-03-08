package com.daenerys.lndservice.dto.feedback;

import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackFormRequest {

    @Size(min = 1, max = 100, message = "Title/topic must be between 1 and 100 characters")
    private String topicTitle;

    @NotNull
    private FeedbackFormTypeEnum feedbackFormType;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfSession;

    @Min(0)
    private int methodOfDelivery;

    @Min(0)
    private int knowledgeAndMastery;

    @Min(0)
    private int relevance;

    //session evaluation
    @Size(min = 1, max = 1000, message = "Must be between 1 and 1000 characters")
    private String likeOrDislikeTrainerOrTopic;
    @Size(min = 1, max = 1000, message = "Must be between 1 and 1000 characters")
    private String additionalFeedbackTrainer;
    @Size(min = 1, max = 1000, message = "Must be between 1 and 1000 characters")
    private String likeOrDislikeSession;
    @Size(min = 1, max = 1000, message = "Must be between 1 and 1000 characters")
    private String additionalFeedbackTopicOrOverall;

}
