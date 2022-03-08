package com.daenerys.lndservice.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedRequestResponse {
    private Long id;
    private String requestType;
    private String title;
    private String postedBy;
    private String requestedBy;
    private LocalDate dateRequested;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dateSubmitted;
    private String description;
    private String link;
    private List<String> speakers;
    private List<UUID> speakerIds;
    private String comments;
    private Boolean isApproved;
    private String declineReason;
}
