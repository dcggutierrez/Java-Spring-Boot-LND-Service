package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.user.UserDetailsDTO;
import com.daenerys.lndservice.service.UserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.daenerys.lndservice.config.SwaggerConfig.USER_TAG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/lnd/user")
@RequiredArgsConstructor
@Api(tags = USER_TAG)
public class UserController {

    private final UserDetailsService userDetailsService;

    @ApiOperation(value = "View Current User Details", notes =
            "Returns the current user details", tags = {USER_TAG})
    @GetMapping("/details")
    public UserDetailsDTO getUserDetails(@RequestHeader(AUTHORIZATION) String bearerToken) {
        return userDetailsService.fetchUserDetails(bearerToken);
    }
}
