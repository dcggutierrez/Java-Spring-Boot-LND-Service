package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.feedback.ViewFeedbackResponse;
import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.repository.lnd.FeedbackFormRepository;
import com.daenerys.lndservice.service.implementation.ViewFeedbackServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum.LND_FEEDBACK_FORM;
import static com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum.THE_WEEKDAY_CATCH_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ViewFeedbackServiceImplTest {

    @Mock
    private FeedbackFormRepository feedbackFormRepository;



    @InjectMocks
    private ViewFeedbackServiceImpl viewFeedbackService;


    @Test
    @DisplayName("Expected to call the DB and return list of FeedbackFormEntity")
    void getRequests_ShouldCallDatabase_ShouldReturnListOfFeedbackFormEntity() {

        // given
        List<FeedbackFormEntity> expectedFeedbackFormEntities = Arrays.asList(
                new FeedbackFormEntity(),
                new FeedbackFormEntity(),
                new FeedbackFormEntity());

        String employee = "test@gmail.com";
        expectedFeedbackFormEntities.forEach(entity -> entity.setEmployee(employee));
        expectedFeedbackFormEntities.forEach(entity -> entity.setFeedbackFormType(LND_FEEDBACK_FORM));

        given(feedbackFormRepository.findAll()).willReturn(expectedFeedbackFormEntities);
        //when
        List<FeedbackFormEntity> actualFeedbackFormEntities = viewFeedbackService.fetchFeedbackForms(Optional.of(LND_FEEDBACK_FORM));
        // then
        assertThat(actualFeedbackFormEntities).isEqualTo(expectedFeedbackFormEntities);
    }


    @Test
    @DisplayName("Expected to call the DB and return a specific FeedbackFormEntity using id")
    void getFeedbackFormEntity_ShouldCallDatabaseAndFindById() {
        // given
        Optional<FeedbackFormEntity> expectedFeedbackFormEntity = java.util.Optional.of(new FeedbackFormEntity());
        Long id = 1L;
        given(feedbackFormRepository.findById(id)).willReturn(expectedFeedbackFormEntity);
        //when
        Optional<FeedbackFormEntity> actualFeedbackFormEntities = viewFeedbackService.findById(id);
        // then
        verify(feedbackFormRepository, times(1)).findById(id);
        assertThat(actualFeedbackFormEntities).isEqualTo(expectedFeedbackFormEntity);
    }

    @Test
    void searchFeedbackForms_ShouldFilterListOfViewFeedBackResponses_BasedOnEmployeeFullNameOrFeedbackType(){

        //given
        ViewFeedbackResponse feedback1 = ViewFeedbackResponse.builder()
                .feedbackFormType(LND_FEEDBACK_FORM)
                .fullName("Catch")
                .build();

        ViewFeedbackResponse feedback2 = ViewFeedbackResponse.builder()
                .feedbackFormType(FeedbackFormTypeEnum.THE_WEEKDAY_CATCH_UP)
                .fullName("feedback")
                .build();
        ViewFeedbackResponse feedback3 = ViewFeedbackResponse.builder()
                .feedbackFormType(FeedbackFormTypeEnum.LND_FEEDBACK_FORM)
                .fullName("string")
                .build();

        List<ViewFeedbackResponse> feedbackResponses = Arrays.asList(feedback1, feedback2, feedback3);
        List<ViewFeedbackResponse> expectedSearchFeedback1 = Arrays.asList(feedback1, feedback2);
        List<ViewFeedbackResponse> expectedSearchFeedback2 = Arrays.asList(feedback3);

        //when
        List<ViewFeedbackResponse> actualSearchFeedback1 = viewFeedbackService.searchFeedbackForms(feedbackResponses, "catch");
        List<ViewFeedbackResponse> actualSearchFeedback2 = viewFeedbackService.searchFeedbackForms(feedbackResponses, "string");

        //then
        assertThat(actualSearchFeedback1).isEqualTo(expectedSearchFeedback1);
        assertThat(actualSearchFeedback2).isEqualTo(expectedSearchFeedback2);
    }

    @Test
    void filterFeedbackFormByDate_ShouldFilterListOfViewFeedBackResponses_BasedOnDates(){
        //given
        ViewFeedbackResponse feedback1 = ViewFeedbackResponse.builder()
                .creationDate("Jan 25, 2022")
                .build();
        ViewFeedbackResponse feedback2 = ViewFeedbackResponse.builder()
                .creationDate("Jan 26, 2022")
                .build();
        ViewFeedbackResponse feedback3 = ViewFeedbackResponse.builder()
                .creationDate("Jan 17, 2021")
                .build();
        List<ViewFeedbackResponse> feedbackResponses = Arrays.asList(feedback1, feedback2, feedback3);
        List<ViewFeedbackResponse> expectedFeedback = Arrays.asList(feedback1, feedback2);

        //when
        List<ViewFeedbackResponse> actualFeedback =  viewFeedbackService.filterFeedbackFormByDate(feedbackResponses,"2022-01-26", "2022-01-25");

        //then
        assertThat(actualFeedback).isEqualTo(expectedFeedback);

    }
}