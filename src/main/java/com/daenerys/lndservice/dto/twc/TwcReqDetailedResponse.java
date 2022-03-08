package com.daenerys.lndservice.dto.twc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwcReqDetailedResponse {
    private Long id;
    private String title;
    private LocalDate dateRequested;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dateSubmitted;
    private String description;
    private String link;
    private List<String> speakers;
    private List<UUID> speakerIds;
    private String comments;
}
