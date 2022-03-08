package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.user.UserDetailsDTO;

public interface UserDetailsService {
    UserDetailsDTO fetchUserDetails(String bearerToken);
}
