package com.daenerys.lndservice.proxy;

import com.daenerys.lndservice.dto.user.UserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "user",
        url = "${url.client}")
public interface UserDetailsFromToken {

    @GetMapping
    UserDetailsResponse getUserDetailsFromToken(@RequestHeader(AUTHORIZATION) String authorization);
}
