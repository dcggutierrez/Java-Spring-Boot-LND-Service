package com.daenerys.lndservice.repository.lnd;


import com.daenerys.lndservice.model.lnd.TwcEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TwcRepository extends JpaRepository<TwcEntity, Long> {
    List<TwcEntity> findByPostedBy(String postedBy, Pageable sortedByDateSubmitted);
    Optional<TwcEntity> findByIdAndPostedBy(Long id, String postedBy);

}
