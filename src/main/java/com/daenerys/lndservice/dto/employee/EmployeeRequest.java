package com.daenerys.lndservice.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    /*
    private String id;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    private String position;
     */

    @NotBlank
    private String avatarUrl;
    @Size(min = 1, max = 50)
    private String firstName;
    @Size(min = 1, max = 50)
    private String lastName;
    @NotBlank
    private String position;

}
