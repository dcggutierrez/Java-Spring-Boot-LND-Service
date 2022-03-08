package com.daenerys.lndservice.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeclineRequest {
    @NotNull
    private Long requestId;

    @NotNull
    private String requestType;

    @NotNull
    @Size(min = 1, max = 1000, message = "Decline Reason must be between 1 and 1000 characters")
    private String declineReason;

}
