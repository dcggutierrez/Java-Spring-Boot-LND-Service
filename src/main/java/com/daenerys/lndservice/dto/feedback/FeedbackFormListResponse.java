package com.daenerys.lndservice.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackFormListResponse {

    private Long id;
    private String feedbackFormType;
    private String creationDate;
}
