package com.daenerys.lndservice.model.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    UUID id;
    @Column(nullable = false, name = "\"createdAt\"")
    LocalDateTime createdAt;
    @Column(nullable = false, name = "\"updatedAt\"")
    LocalDateTime updatedAt;
    @Column(nullable = false, name = "\"deletedAt\"")
    LocalDateTime deletedAt;
    @Column(nullable = false, name = "\"nickName\"")
    String nickName;
    @Column(nullable = false, name = "\"firstName\"")
    String firstName;
    @Column(nullable = false, name = "\"middleName\"")
    String middleName;
    @Column(nullable = false, name = "\"lastName\"")
    String lastName;
    @Column(nullable = false, name = "\"birthDate\"")
    LocalDate birthDate;
    @Column(nullable = false, name = "\"gender\"")
    String gender;
    @Column(nullable = false, name = "\"mobileNumber\"")
    String mobileNumber;
    @Column(nullable = false, name = "\"civilStatus\"")
    String civilStatus;
    @Column(nullable = false, name = "\"personalEmail\"")
    String personalEmail;
    @Column(nullable = false, name = "\"facebookProfile\"")
    String facebookProfile;
    @Column(nullable = false, name = "\"twitterProfile\"")
    String twitterProfile;
    @Column(nullable = false, name = "\"linkedinProfile\"")
    String linkedinProfile;
    @Column(nullable = false, name = "\"presentAddressProvince\"")
    String presentAddressProvince;
    @Column(nullable = false, name = "\"presentAddressMunicipality\"")
    String presentAddressMunicipality;
    @Column(nullable = false, name = "\"presentAddressBarangay\"")
    String presentAddressBarangay;
    @Column(nullable = false, name = "\"presentAddressLineAddress\"")
    String presentAddressLineAddress;
    @Column(nullable = false, name = "\"permanentAddressProvince\"")
    String permanentAddressProvince;
    @Column(nullable = false, name = "\"permanentAddressMunicipality\"")
    String permanentAddressMunicipality;
    @Column(nullable = false, name = "\"permanentAddressBarangay\"")
    String permanentAddressBarangay;
    @Column(nullable = false, name = "\"permanentAddressLineAdress\"")
    String permanentAddressLineAdress;
    @Column(nullable = false, name = "\"contactName\"")
    String contactName;
    @Column(nullable = false, name = "\"relationship\"")
    String relationship;
    @Column(nullable = false, name = "\"contactNumber\"")
    String contactNumber;
    @Column(nullable = false, name = "\"engagement\"")
    String engagement;
    @Column(nullable = false, name = "\"seniorityLevel\"")
    String seniorityLevel;
    @Column(nullable = false, name = "\"position\"")
    String position;
    @Column(nullable = false, name = "\"dateHired\"")
    LocalDate dateHired;
    @Column(nullable = false, name = "\"companyEmail\"")
    String companyEmail;
    @Column(nullable = false, name = "\"regularizationDate\"")
    LocalDate regularizationDate;
    @Column(nullable = false, name = "\"employmentStatus\"")
    String employmentStatus;
    @Column(nullable = false, name = "\"department\"")
    String department;
    @Column(nullable = false, name = "\"status\"")
    String status;
    @Column(nullable = false, name = "\"avatarUrl\"")
    String avatarUrl;
    @Column(nullable = false, name = "\"employeeId\"")
    String employeeId;
    @Column(nullable = false, name = "\"tinNumber\"")
    String tinNumber;
    @Column(nullable = false, name = "\"sssNumber\"")
    String sssNumber;
    @Column(nullable = false, name = "\"philhealthNumber\"")
    String philhealthNumber;
    @Column(nullable = false, name = "\"pagibigNumber\"")
    String pagibigNumber;
    @Column(nullable = false, name = "\"supervisorId\"")
    String supervisorId;
//    @Column(nullable = false, name = "\"engineeringManagerId\"")
//    String engineeringManagerId;

    public String getFullName() {
        String fullName = this.firstName+" "+this.lastName;
        return fullName;
    }

    public String getPresentAddress() {
        return presentAddressLineAddress + ", " +
               presentAddressBarangay + ", " +
               presentAddressMunicipality + ", " +
               presentAddressProvince;
    }

    public String getPermanentAddress() {
        return permanentAddressLineAdress + ", " +
               permanentAddressBarangay + ", " +
               permanentAddressMunicipality + ", " +
               permanentAddressProvince;
    }
}