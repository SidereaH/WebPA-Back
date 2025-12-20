package com.webpa.domain;

import com.webpa.domain.enums.Marketplace;
import com.webpa.domain.enums.TaskStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parse_tasks")
public class ParseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "query_text", nullable = false, length = 512)
    private String queryText;

    @ElementCollection(targetClass = Marketplace.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "parse_task_marketplaces", joinColumns = @JoinColumn(name = "task_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "marketplace", nullable = false, length = 64)
    private List<Marketplace> marketplaces;

    @Column(name = "pages_to_scan", nullable = false)
    private int pagesToScan;

    @Column(name = "page_size", nullable = false)
    private int pageSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private TaskStatus status;

    @Column(name = "comment", length = 1024)
    private String comment;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "append_to_existing", nullable = false)
    private boolean appendToExisting;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (status == null) {
            status = TaskStatus.QUEUED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED) {
            finishedAt = Instant.now();
        }
    }
}
