package com.daenerys.lndservice.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RequestResponse {
    private Long id;
    private String requestType;
    private String title;
    private LocalDate dateSubmitted;
    private Boolean isApproved;
}
