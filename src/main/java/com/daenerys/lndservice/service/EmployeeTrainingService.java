package com.daenerys.lndservice.service;

import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EmployeeTrainingService {

    void postEmployeeTraining(EmployeeTrainingEntity actualEmployeeTrainingEntity);
    List<EmployeeTrainingEntity> getOwnerRequest(String id, Pageable pageable);
    EmployeeTrainingEntity getDetailedRequest(Long id);
}
