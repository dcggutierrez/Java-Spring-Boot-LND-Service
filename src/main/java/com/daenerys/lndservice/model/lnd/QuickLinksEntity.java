package com.daenerys.lndservice.model.lnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quick_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuickLinksEntity {
    @Id
    private String header;
    private String access;
    private String category;
    private String description;
    private String link;
}
