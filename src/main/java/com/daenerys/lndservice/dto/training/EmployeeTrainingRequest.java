package com.daenerys.lndservice.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrainingRequest {

    @NotNull
    @Size(min = 1, max = 100, message = "Training name must be between 1 and 100 characters")
    private String trainingName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate proposedDate;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Size(max = 1000, message = "Remarks must be a maximum 1000 characters")
    private String comments;
}
