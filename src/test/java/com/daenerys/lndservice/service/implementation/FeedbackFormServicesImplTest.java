package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.feedback.FeedbackFormRequest;
import com.daenerys.lndservice.dto.user.UserDetailsDTO;
import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import com.daenerys.lndservice.repository.lnd.FeedbackFormRepository;
import com.daenerys.lndservice.service.UserDetailsService;
import com.daenerys.lndservice.service.implementation.FeedbackFormServicesImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum.LND_FEEDBACK_FORM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeedbackFormServicesImplTest {

    @Mock
    private FeedbackFormRepository feedbackFormRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private FeedbackFormServicesImpl underTest;


    @Nested
    class CreateFeedbackForm {
        @Test
        @DisplayName("Expected to call the DB and save the data")
        void createNewLnDFeedbackForm_ShouldCallDatabase_ShouldSaveTheData() {
            //given
            FeedbackFormRequest feedbackFormRequest = new FeedbackFormRequest(
                    "Test",
                    LND_FEEDBACK_FORM,
                    LocalDate.of(2019, 12, 12),
                    1,
                    2,
                    3,
                    "Test",
                    "Test",
                    "Test",
                    "Test");

            given(userDetailsService.fetchUserDetails("Test")).willReturn(new UserDetailsDTO("test@gmail.com", "Test"));

            //when
            underTest.postRequest(feedbackFormRequest, "Test");

            //then
            verify(feedbackFormRepository, times(1)).save(any());
        }
    }


    @Nested
    class GetRequests {
        @Test
        @DisplayName("Expected to call the DB and return list of FeedbackFormEntity")
        void getRequests_ShouldCallDatabase_ShouldReturnListOfFeedbackFormEntity() {
        /*
        TODO
            1. Must be able to get all feedback forms submitted by user through user email (employee column)
        */

            // given
            Pageable sortedByDateSubmitted = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            List<FeedbackFormEntity> expectedFeedbackFormEntities = List.of(
                    new FeedbackFormEntity(),
                    new FeedbackFormEntity(),
                    new FeedbackFormEntity());
            String employee = "test@gmail.com";
            expectedFeedbackFormEntities.forEach(entity -> entity.setEmployee(employee));
            expectedFeedbackFormEntities.forEach(entity -> entity.setFeedbackFormType(LND_FEEDBACK_FORM));

            given(feedbackFormRepository.findByEmployee(employee, sortedByDateSubmitted)).willReturn(expectedFeedbackFormEntities);
            given(userDetailsService.fetchUserDetails("Test")).willReturn(new UserDetailsDTO("test@gmail.com", "Test"));
            //when
            List<FeedbackFormEntity> actualFeedbackFormEntities = underTest.fetchFeedbackForms("Test", Optional.of(LND_FEEDBACK_FORM), sortedByDateSubmitted);
            // then
            verify(feedbackFormRepository, times(1)).findByEmployee(employee, sortedByDateSubmitted);
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
            Optional<FeedbackFormEntity> actualFeedbackFormEntities = underTest.findById(id);
            // then
            verify(feedbackFormRepository, times(1)).findById(id);
            assertThat(actualFeedbackFormEntities).isEqualTo(expectedFeedbackFormEntity);
        }
    }
}