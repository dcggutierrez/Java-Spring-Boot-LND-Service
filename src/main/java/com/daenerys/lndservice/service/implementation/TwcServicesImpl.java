package com.daenerys.lndservice.service.implementation;


import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.model.lnd.TwcEntity;
import com.daenerys.lndservice.repository.employee.EmployeeRepository;
import com.daenerys.lndservice.repository.lnd.TwcRepository;
import com.daenerys.lndservice.service.TwcServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TwcServicesImpl implements TwcServices {

    private final TwcRepository twcRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void postRequest(TwcEntity twcRequest) {
        twcRepository.save(twcRequest);
    }

    @Override
    public List<TwcEntity> getRequests(String postedBy, Pageable sortedByDateSubmitted) {
        return twcRepository.findByPostedBy(postedBy, sortedByDateSubmitted);
    }

    @Override
    public List<EmployeeEntity> getSpeakers(String name, String position, Pageable pageable) {
        if (position == null || position.isEmpty()) {
            return employeeRepository.findByNameOrderAlphabetical(name, pageable);
        }
        return employeeRepository.findByNameAndPositionOrderAlphabetical(name, position, pageable);
    }

    @Override
    public Optional<EmployeeEntity> getEmployee(UUID id) {
        Optional<EmployeeEntity> returnValue = employeeRepository.findById(id);
        return returnValue;
    }

    @Override
    public TwcEntity getRequest(Long twcReqId, String email) {
        return twcRepository.findByIdAndPostedBy(twcReqId, email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
