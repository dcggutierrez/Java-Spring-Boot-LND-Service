package com.daenerys.lndservice.repository.lnd;

import com.daenerys.lndservice.model.lnd.FeedbackFormEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackFormRepository extends JpaRepository<FeedbackFormEntity, Long> {
    List<FeedbackFormEntity> findByEmployee(String email, Pageable pageable);
}
