package com.daenerys.lndservice.service;


import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TwcServices {

    void postRequest(TwcEntity twcEntity);
    List<TwcEntity> getRequests(String userId, Pageable sortedByDateSubmitted);
    List<EmployeeEntity> getSpeakers(String name, String position, Pageable pageable);
    Optional<EmployeeEntity> getEmployee(UUID id);

    TwcEntity getRequest(Long twcReqId, String email);
}