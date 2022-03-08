package com.daenerys.lndservice.repository.employee;

import com.daenerys.lndservice.model.employee.EmployeeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, UUID> {

    @Query(value =
            "SELECT * FROM employees " +
            "WHERE LOWER(CONCAT(\"firstName\", ' ', \"lastName\")) " +
            "LIKE LOWER(CONCAT('%', :name, '%')) " +
            "ORDER BY \"lastName\", \"firstName\", \"middleName\"", nativeQuery = true)
    List<EmployeeEntity> findByNameOrderAlphabetical(@Param("name") String name, Pageable pageable);

    @Query(value =
            "SELECT * FROM employees " +
            "WHERE LOWER(CONCAT(\"firstName\", ' ', \"lastName\")) " +
            "LIKE LOWER(CONCAT('%', :name, '%')) AND position = :position " +
            "ORDER BY \"lastName\", \"firstName\", \"middleName\"", nativeQuery = true)
    List<EmployeeEntity> findByNameAndPositionOrderAlphabetical(@Param("name") String name, @Param("position") String position, Pageable pageable);
    List<EmployeeEntity> findByIdIn(List<UUID> ids);
    List<EmployeeEntity> findByCompanyEmailIn(List<String> companyEmails);
    Optional<EmployeeEntity> findById(UUID id);

    Optional<EmployeeEntity> findByCompanyEmail(String companyEmail);
}