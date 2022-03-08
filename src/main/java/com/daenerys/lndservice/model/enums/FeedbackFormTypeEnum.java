package com.daenerys.lndservice.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FeedbackFormTypeEnum {
    LND_FEEDBACK_FORM("L&D Feedback"),
    THE_WEEKDAY_CATCH_UP("The Weekday Catch-Up");


    private String feedbackFormType;

    FeedbackFormTypeEnum(String feedbackFormType) {
        this.feedbackFormType = feedbackFormType;
    }

    public String getFeedbackFormType() {
        return feedbackFormType;
    }

    @JsonCreator
    public static FeedbackFormTypeEnum getFeedbackFormTypeFromString(String value) {

        for (FeedbackFormTypeEnum form : FeedbackFormTypeEnum.values()) {

            if (form.getFeedbackFormType().equals(value)) {

                return form;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return feedbackFormType == FeedbackFormTypeEnum.LND_FEEDBACK_FORM.getFeedbackFormType() ? "L&D Feedback" : "The Weekday Catch-Up";
    }
}
