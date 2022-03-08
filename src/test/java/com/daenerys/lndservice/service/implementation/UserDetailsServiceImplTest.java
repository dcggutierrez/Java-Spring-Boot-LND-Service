package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.user.UserDetailsDTO;
import com.daenerys.lndservice.dto.user.UserDetailsResponse;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.proxy.UserDetailsFromToken;
import com.daenerys.lndservice.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserDetailsFromToken userDetailsFromToken;

    @InjectMocks
    private UserDetailsServiceImpl underTest;

    @Nested
    class GetUserDetails {
        @Test
        @DisplayName("Expected to call the DB and return the name and email of the current user")
        void getUserDetails_ShouldCallOpenFeignShouldGetUserDetailsFromToken() {
            // given
            String bearerToken = "Test";
            String email = "test@gmail.com";
            String name = "TestName";
            UserDetailsDTO expectedUserDetails = new UserDetailsDTO(email, name);
            Optional<EmployeeEntity> employeeEntity = java.util.Optional.of(new EmployeeEntity());
            employeeEntity.get().setCompanyEmail(email);
            employeeEntity.get().setFirstName(name);
            given(userDetailsFromToken.getUserDetailsFromToken(bearerToken)).willReturn(new UserDetailsResponse("Test", name, email, "Test"));
            //when
            UserDetailsDTO actualUserDetails = underTest.fetchUserDetails(bearerToken);
            // then
            verify(userDetailsFromToken, times(1)).getUserDetailsFromToken(bearerToken);
            assertThat(actualUserDetails).isEqualTo(expectedUserDetails);
        }
    }
}