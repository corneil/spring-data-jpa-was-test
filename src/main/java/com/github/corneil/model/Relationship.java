package com.github.corneil.model;

import com.mysema.query.annotations.Config;
import com.mysema.query.annotations.QueryEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(schema = "test", name = "relationship")
@QueryEntity
@Config(entityAccessors = true)
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accepted_date")
    private Date acceptedDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "created_date")
    private Date createdDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_enum", length = 16)
    private RelationshipStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_date")
    private Date submittedDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "archived_date")
    private Date archivedDate;
    @Column(name = "archived_flag")
    private boolean archived;
    @Column(name = "reminder_flag")
    private Boolean reminderSent;
}

