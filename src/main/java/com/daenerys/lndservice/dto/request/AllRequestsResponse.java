package com.daenerys.lndservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRequestsResponse {
    private LocalDate dateSubmitted;
    private LocalDate dateRequested;
    private String requestType;
    private Long id;
    private String title;

    @JsonIgnore
    private LocalDateTime createdAt;
}

