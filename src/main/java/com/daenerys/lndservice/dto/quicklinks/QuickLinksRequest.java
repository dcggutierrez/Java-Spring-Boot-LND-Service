package com.daenerys.lndservice.dto.quicklinks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuickLinksRequest {
    private String header;
    private String access;
    private String category;
    private String description;
    private String link;
}
