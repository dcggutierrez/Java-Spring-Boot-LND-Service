package com.daenerys.lndservice.dto.feedback;

import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewFeedbackResponse {

    private Long id;
    private String fullName;
    private FeedbackFormTypeEnum feedbackFormType;
    private String creationDate;
    private String employee;
}
