package com.daenerys.lndservice.model.lnd;

import com.daenerys.lndservice.model.enums.FeedbackFormTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_forms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String employee;

    @Column(nullable = false,length = 100)
    private String topicTitle;

    @Column(nullable = false)
    private FeedbackFormTypeEnum feedbackFormType;

    @Column(nullable = false)
    private LocalDate dateOfSession;

    @Column(nullable = false)
    private int methodOfDelivery;

    @Column(nullable = false)
    private int knowledgeAndMastery;

    @Column(nullable = false)
    private int relevance;

    //session evaluation

    @Column(nullable = false, length = 1000)
    private String likeOrDislikeTrainerOrTopic;

    @Column(length = 1000)
    private String additionalFeedbackTrainer;

    @Column(nullable = false, length = 1000)
    private String likeOrDislikeSession;

    @Column(nullable = false, length = 1000)
    private String additionalFeedbackTopicOrOverall;
}
