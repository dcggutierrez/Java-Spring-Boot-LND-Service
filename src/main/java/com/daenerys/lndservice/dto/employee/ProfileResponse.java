package com.daenerys.lndservice.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    UUID id;
    String firstName;
    String middleName;
    String lastName;
    LocalDate birthDate;
    String gender;
    String civilStatus;
    String mobileNumber;
    String personalEmail;
    String presentAddress;
    String permanentAddress;
    String contactName;
    String relationship;
    String contactNumber;
    String employeeId;
    String companyEmail;
    String engagement;
    String position;
    String department;
    String seniorityLevel;
    String employmentStatus;
    LocalDate dateHired;
    LocalDate regularizationDate;
    String supervisor;
    String engineeringManager;
    String status;
    String avatarUrl;
    String tinNumber;
    String sssNumber;
    String philhealthNumber;
    String pagibigNumber;
}
