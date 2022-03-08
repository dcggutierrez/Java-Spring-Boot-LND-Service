package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import com.daenerys.lndservice.repository.lnd.EmployeeTrainingRepository;
import com.daenerys.lndservice.service.implementation.EmployeeTrainingServiceImpl;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeTrainingServiceImplTest {

    @Mock
    EmployeeTrainingRepository employeeTrainingRepository;

    @InjectMocks
    EmployeeTrainingServiceImpl employeeTrainingServiceImpl;

    @Nested
    class CreateEmployeeTraining {
        @Test
        @DisplayName("Expected to save to database")
        public void createEmployeeTraining_success() {

            // given
            EmployeeTrainingEntity actualEmployeeTrainingEntity = EmployeeTrainingEntity.builder()
                    .id(1L)
                    .postedBy("philipcalape@whitecloak.com")
                    .trainingName("DevOps")
                    .createdAt(LocalDateTime.now())
                    .proposedDate(LocalDate.now().plusDays(2L))
                    .startTime(LocalTime.parse("10:00"))
                    .endTime(LocalTime.parse("12:00"))
                    .build();

            given(employeeTrainingRepository.save(actualEmployeeTrainingEntity)).willReturn(actualEmployeeTrainingEntity);
            // when
            employeeTrainingServiceImpl.postEmployeeTraining(actualEmployeeTrainingEntity);
            // then
            verify(employeeTrainingRepository, times(1)).save(actualEmployeeTrainingEntity);
        }
    }

    @Nested
    class GetRequests {
        @Test
        @DisplayName("Expected to call the DB and return list of EmployeeTraining Entity")
        void getRequests_ShouldCallDatabase_ShouldReturnListOfEmployeeTrainingEntity() {
            // given
            Pageable sortedByDateSubmitted = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            List<EmployeeTrainingEntity> expectedEmployeeEntities = List.of(new EmployeeTrainingEntity(), new EmployeeTrainingEntity(), new EmployeeTrainingEntity());
            String userId = "1";
            given(employeeTrainingRepository.findByPostedBy(userId, sortedByDateSubmitted)).willReturn(expectedEmployeeEntities);
            //when
            List<EmployeeTrainingEntity> actualEmployeeEntities = employeeTrainingServiceImpl.getOwnerRequest(userId, sortedByDateSubmitted);
            // then
            verify(employeeTrainingRepository, times(1)).findByPostedBy(userId, sortedByDateSubmitted);
            assertThat(actualEmployeeEntities).isEqualTo(expectedEmployeeEntities);
        }

        @Test
        @DisplayName("Expected to call the DB and return a EmployeeTrainingEntity")
        void getRequests_ShouldCallDatabase_ShouldReturnEmployeeTrainingEntity() {
            Long id = 1L;
            EmployeeTrainingEntity expectedEmployeeTrainingEntity = EmployeeTrainingEntity.builder().id(id).build();
            given(employeeTrainingRepository.findById(id)).willReturn(Optional.of(expectedEmployeeTrainingEntity));

            EmployeeTrainingEntity actualEmployeeTrainingEntity = employeeTrainingServiceImpl.getDetailedRequest(id);

            verify(employeeTrainingRepository, times(1)).findById(id);
            assertThat(actualEmployeeTrainingEntity).isEqualTo(expectedEmployeeTrainingEntity);
        }

    }
}


