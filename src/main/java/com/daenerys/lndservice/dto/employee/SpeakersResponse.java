package com.daenerys.lndservice.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeakersResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String position;
}
