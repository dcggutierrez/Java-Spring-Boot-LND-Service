package com.daenerys.lndservice.repository.lnd;

import com.daenerys.lndservice.model.lnd.EmployeeTrainingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeTrainingRepository extends JpaRepository<EmployeeTrainingEntity, Long> {
    List<EmployeeTrainingEntity> findByPostedBy(String userId, Pageable sortedByDateSubmitted);
}
