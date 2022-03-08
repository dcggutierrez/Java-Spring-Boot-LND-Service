package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.employee.ProfileResponse;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.service.ProfileServices;
import com.daenerys.lndservice.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServicesImpl implements ProfileServices {

    private final EmployeeRepository employeeRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public ProfileResponse fetchProfile(String bearerToken) {
        String employee = userDetailsService.fetchUserDetails(bearerToken).getCompanyEmail();
        EmployeeEntity employeeEntity = employeeRepository.findByCompanyEmail(employee)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProfileResponse profile = new ProfileResponse();
        BeanUtils.copyProperties(employeeEntity, profile);
        profile.setPresentAddress(employeeEntity.getPresentAddress());
        profile.setPermanentAddress(
                        (employeeEntity.getPermanentAddress().equals(profile.getPresentAddress())) ?
                                "Same as Present Address" : employeeEntity.getPermanentAddress());
        profile.setSupervisor(
                employeeRepository.findById(UUID.fromString(employeeEntity.getSupervisorId()))
                        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND))
                        .getFullName());
        profile.setEngineeringManager("Engineering Manager not available on database");
        return profile;
    }
}
