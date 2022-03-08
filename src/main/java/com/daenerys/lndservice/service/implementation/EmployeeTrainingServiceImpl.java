package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import com.daenerys.lndservice.repository.lnd.EmployeeTrainingRepository;
import com.daenerys.lndservice.service.EmployeeTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeTrainingServiceImpl implements EmployeeTrainingService {

    private final EmployeeTrainingRepository employeeTrainingRepository;

    @Override
    public void postEmployeeTraining(EmployeeTrainingEntity actualEmployeeTrainingEntity) {
        employeeTrainingRepository.save(actualEmployeeTrainingEntity);
    }

    @Override
    public List<EmployeeTrainingEntity> getOwnerRequest(String id, Pageable pageable) {
        return employeeTrainingRepository.findByPostedBy(id,pageable);
    }

    @Override
    public EmployeeTrainingEntity getDetailedRequest(Long id) {
        return employeeTrainingRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}
