package com.daenerys.lndservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private String sub;
    private String name;
    private String email;
    private String email_verified;
}
