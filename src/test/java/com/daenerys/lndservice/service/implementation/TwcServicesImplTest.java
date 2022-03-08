package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.repository.lnd.TwcRepository;
import com.daenerys.lndservice.service.implementation.RequestsServicesImpl;
import com.daenerys.lndservice.service.implementation.TwcServicesImpl;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TwcServicesImplTest {

    @Mock
    private TwcRepository twcRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TwcServicesImpl twcServices;

    @Mock
    private RequestsServicesImpl requestsServices;

    private List<UUID> createList() {
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        return uuidList;
    }

    @Nested
    class CreateEmployeeTraining {

        @Test
        @DisplayName("Expected to save to database")
        public void createEmployeeTraining_success() {
        /*
        TODO
            1. Must be able to submit a new employee training
            private Long id;
        */

            // given
            TwcEntity actualTwcEntity = TwcEntity.builder()
                    .id(1L)
                    .postedBy("philipcalape@whitecloak.com")
                    .title("DevOps")
                    .createdAt(LocalDateTime.now())
                    .proposedDate(LocalDate.now().plusDays(2L))
                    .startTime(LocalTime.parse("10:00"))
                    .endTime(LocalTime.parse("12:00"))
                    .link("philip.com")
                    .speakerIds(createList())
                    .comments("arf arf")
                    .build();

            given(twcRepository.save(actualTwcEntity)).willReturn(actualTwcEntity);
            // when
            twcServices.postRequest(actualTwcEntity);
            // then
            verify(twcRepository, times(1)).save(actualTwcEntity);
        }
    }

    @Nested
    class GetRequests {
        @Test
        @DisplayName("Expected to call the DB and return list of TwcEntity")
        void getRequests_ShouldCallDatabase_ShouldReturnListOfTwcEntity() {
            // given
            Pageable sortedByDateSubmitted = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            List<TwcEntity> expectedTwcEntities = List.of(new TwcEntity(), new TwcEntity(), new TwcEntity());
            String userId = "1";
            given(twcRepository.findByPostedBy(userId, sortedByDateSubmitted)).willReturn(expectedTwcEntities);
            //when
            List<TwcEntity> actualTwcEntities
                    = twcServices.getRequests(userId, sortedByDateSubmitted);
            // then
            verify(twcRepository, times(1)).findByPostedBy(userId, sortedByDateSubmitted);
            assertThat(actualTwcEntities).isEqualTo(expectedTwcEntities);
        }
    }

    @Nested
    class GetSpeakers {

        @Test
        @DisplayName("Return list of EmployeeEntity filtered by name when position is not specified")
        void getSpeakers_WhenPositionNotSpecified_ShouldCallDatabaseAndFindByName() {
            // given
            List<EmployeeEntity> expectedEmployeeEntities = List.of(new EmployeeEntity(), new EmployeeEntity(), new EmployeeEntity());
            Pageable pageable = PageRequest.of(0, 10);
            given(employeeRepository.findByNameOrderAlphabetical("", pageable)).willReturn(expectedEmployeeEntities);

            // when
            List<EmployeeEntity> actualEmployeeEntities = twcServices.getSpeakers("", null, pageable);
            List<EmployeeEntity> actualEmployeeEntities2 = twcServices.getSpeakers("", "", pageable);

            // then
            verify(employeeRepository, times(2)).findByNameOrderAlphabetical("", pageable);
            verify(employeeRepository, times(0)).findByNameAndPositionOrderAlphabetical("", "", pageable);
            assertThat(actualEmployeeEntities).isEqualTo(expectedEmployeeEntities);
            assertThat(actualEmployeeEntities2).isEqualTo(expectedEmployeeEntities);
        }

        @Test
        @DisplayName("Return list of EmployeeEntity filtered by name and position when position is specified")
        void getSpeakers_WhenPositionIsSpecified_ShouldCallDatabase_AndFindByNameAndPosition() {
            // given
            List<EmployeeEntity> expectedEmployeeEntities = List.of(new EmployeeEntity(), new EmployeeEntity(), new EmployeeEntity());
            Pageable pageable = PageRequest.of(0, 10);
            String position = "Fullstack Developer";
            String name = "Philip";
            given(employeeRepository.findByNameAndPositionOrderAlphabetical(name, position, pageable)).willReturn(expectedEmployeeEntities);

            // when
            List<EmployeeEntity> actualEmployeeEntities = twcServices.getSpeakers(name, position, pageable);

            // then
            verify(employeeRepository, times(1)).findByNameAndPositionOrderAlphabetical(name, position, pageable);
            verify(employeeRepository, times(0)).findByNameOrderAlphabetical(name, pageable);
            assertThat(actualEmployeeEntities).isEqualTo(expectedEmployeeEntities);
        }

    }

    @Nested
    class GetRequest {

        @Test
        @DisplayName(("Expected to call the DB and return a TwcEntity"))
        void getRequest_ShouldCallDatabase_ShouldReturnTwcEntity() {
            // given
            Long id = 1L;
            String email = "philipCalape@whitecloak.com";
            TwcEntity expectedTwcEntity = TwcEntity.builder().id(id).build();
            given(twcRepository.findByIdAndPostedBy(id, email)).willReturn(Optional.of(expectedTwcEntity));

            // when
            TwcEntity actualTwcEntity = twcServices.getRequest(id, email);

            // then
            verify(twcRepository, times(1)).findByIdAndPostedBy(id, email);
            assertThat(actualTwcEntity).isEqualTo(expectedTwcEntity);
        }

        @Test
        @DisplayName(("Get Request should throw 404 if TWC Entity does not exist"))
        void getRequest_Throw404_IfTwcEntityDoesNotExistById() {
            // given
            given(twcRepository.findByIdAndPostedBy(any(), anyString())).willReturn(Optional.empty());


            assertThatThrownBy(() -> {
                twcServices.getRequest(any(), anyString());
            }).isInstanceOf(ResponseStatusException.class).hasMessageContaining("404");
        }
    }


}