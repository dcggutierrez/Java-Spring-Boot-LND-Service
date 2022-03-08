package com.daenerys.lndservice.dto.training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrainingDetailedResponse {
    private Long id;
    private String title;
    private LocalDate dateRequested;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dateSubmitted;
    private String comments;
}
