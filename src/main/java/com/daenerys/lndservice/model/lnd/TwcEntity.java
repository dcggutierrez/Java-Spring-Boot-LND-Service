package com.daenerys.lndservice.model.lnd;

//import com.Daenerys.LDNService.dto.EmployeeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "twc")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwcEntity implements Serializable {

    private static final long serialVersionUID = -7570383552566287300L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postedBy;

    @Column(nullable = false,length = 100)
    private String title;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate proposedDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false,length = 1000)
    private String description;

    @Column(length = 500)
    private String link;

    private Boolean isApproved;

    private String requestType = "The Weekday Catch-Up Request";

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "twc_request_speakers", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "speaker_id")
    private List<UUID> speakerIds = new ArrayList<>();
    
    @Column(length = 1000)
    private String comments;

    @Column(length = 1000)
    private String declineReason;

    public LocalDate getDateRequested() {
        return this.proposedDate;
    }
    public LocalDate getDateSubmitted() {
        return this.createdAt.toLocalDate();
    }

}
