package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.user.UserDetailsDTO;
import com.daenerys.lndservice.dto.user.UserDetailsResponse;
import com.daenerys.lndservice.proxy.UserDetailsFromToken;
import com.daenerys.lndservice.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsFromToken userDetailsFromToken;

    @Override
    public UserDetailsDTO fetchUserDetails(String bearerToken) {
        UserDetailsResponse userDetailsFromToken = this.userDetailsFromToken.getUserDetailsFromToken(bearerToken);
        String companyEmail = userDetailsFromToken.getEmail();
        String fullName = userDetailsFromToken.getName();
        return new UserDetailsDTO(companyEmail, fullName);
    }
}
