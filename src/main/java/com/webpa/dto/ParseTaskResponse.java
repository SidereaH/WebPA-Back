package com.webpa.dto;

import com.webpa.domain.ParseTask;
import com.webpa.domain.enums.Marketplace;
import com.webpa.domain.enums.TaskStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ParseTaskResponse(
        UUID id,
        String query,
        List<Marketplace> marketplaces,
        int pages,
        int pageSize,
        TaskStatus status,
        boolean appendToExisting,
        String comment,
        String errorMessage,
        Instant createdAt,
        Instant startedAt,
        Instant finishedAt
) {

    public static ParseTaskResponse fromEntity(ParseTask task) {
        return new ParseTaskResponse(
                task.getId(),
                task.getQueryText(),
                task.getMarketplaces(),
                task.getPagesToScan(),
                task.getPageSize(),
                task.getStatus(),
                task.isAppendToExisting(),
                task.getComment(),
                task.getErrorMessage(),
                task.getCreatedAt(),
                task.getStartedAt(),
                task.getFinishedAt()
        );
    }
}
