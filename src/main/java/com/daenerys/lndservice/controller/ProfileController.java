package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.employee.ProfileResponse;
import com.daenerys.lndservice.service.ProfileServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.daenerys.lndservice.config.SwaggerConfig.FEEDBACK_TAG;
import static com.daenerys.lndservice.config.SwaggerConfig.PROFILE_TAG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/lnd/profile")
@RequiredArgsConstructor
@Api(tags = FEEDBACK_TAG)
public class ProfileController {

    private final ProfileServices profileServices;

    @ApiOperation(value = "View Current User Profile", notes =
            "Returns the profile of the current user", tags = {PROFILE_TAG})
    @GetMapping
    public ProfileResponse getRequest(@RequestHeader(AUTHORIZATION) String bearerToken) {
        return profileServices.fetchProfile(bearerToken);
    }
}
