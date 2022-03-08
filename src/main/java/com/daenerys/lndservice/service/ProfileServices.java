package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.employee.ProfileResponse;

public interface ProfileServices {
    ProfileResponse fetchProfile(String bearerToken);
}
