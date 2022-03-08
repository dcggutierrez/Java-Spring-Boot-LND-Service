package com.daenerys.lndservice.model.lnd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "employee_trainings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String postedBy;

    @Column(nullable = false, length = 100)
    private String trainingName;

    @Column(nullable = false)
    private LocalDate proposedDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 1000)
    private String comments;

    private Boolean isApproved;


    private String requestType = "Employee Training Request";

    @Column(length = 1000)
    private String declineReason;

    public LocalDate getDateRequested() {
        return this.proposedDate;
    }

    public LocalDate getDateSubmitted() {
        return this.createdAt.toLocalDate();
    }

    public String getTitle() { return this.trainingName; }

}
