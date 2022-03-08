package com.daenerys.lndservice.dto.twc;
//import com.Daenerys.LDNService.dto.EmployeeEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwcRequest {

    @NotNull
    @Size(min = 1, max = 100, message = "Title/topic must be between 1 and 100 characters")
    private String title;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate proposedDate;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @NotBlank
    private String description;

    private String link;
    private List<UUID> speakerIds;
    @Size(max = 1000, message = "Comments must be a maximum of 1000 characters")
    private String comments;
}
