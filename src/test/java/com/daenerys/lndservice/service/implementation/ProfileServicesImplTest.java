package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.employee.ProfileResponse;
import com.daenerys.lndservice.dto.user.UserDetailsDTO;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.service.UserDetailsService;
import com.daenerys.lndservice.service.implementation.ProfileServicesImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileServicesImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private ProfileServicesImpl underTest;


    @Nested
    class GetRequests {
        @Test
        @DisplayName("Expected to call the DB and return a current User Profile using bearerToken")
        void getOwnEmployeeProfile_ShouldCallDatabaseAndFindByEmail_ShouldCallDatabaseAndFindByIdForSupervisorAndEngineeringManager() {
            // given
            ProfileResponse expectedProfileResponse = ProfileResponse.builder()
                    .supervisor("Neil Ryan")
                    .engineeringManager("Engineering Manager not available on database")
                    .presentAddress("a, b, c, d")
                    .permanentAddress("Same as Present Address")
                    .build();

            EmployeeEntity employee = EmployeeEntity.builder()
                    .presentAddressLineAddress("a")
                    .presentAddressBarangay("b")
                    .presentAddressMunicipality("c")
                    .presentAddressProvince("d")
                    .permanentAddressLineAdress("a")
                    .permanentAddressBarangay("b")
                    .permanentAddressMunicipality("c")
                    .permanentAddressProvince("d")
                    .supervisorId(UUID.randomUUID().toString())
                    .build();

            EmployeeEntity supervisorManager = EmployeeEntity.builder()
                    .firstName("Neil")
                    .lastName("Ryan")
                    .build();

            given(userDetailsService.fetchUserDetails("bearerToken")).willReturn(new UserDetailsDTO("test@gmail.com", "Test"));
            given(employeeRepository.findByCompanyEmail("test@gmail.com")).willReturn(Optional.ofNullable(employee));
            given(employeeRepository.findById(UUID.fromString(employee.getSupervisorId()))).willReturn(Optional.ofNullable(supervisorManager));
            //when
            ProfileResponse actualProfileResponse = underTest.fetchProfile("bearerToken");
            // then
            verify(userDetailsService, times(1)).fetchUserDetails("bearerToken");
            verify(employeeRepository, times(1)).findByCompanyEmail("test@gmail.com");
            assertThat(actualProfileResponse).isEqualTo(expectedProfileResponse);
        }
    }
}