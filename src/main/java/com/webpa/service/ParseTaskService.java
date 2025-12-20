package com.webpa.service;

import com.webpa.domain.ParseTask;
import com.webpa.domain.enums.TaskStatus;
import com.webpa.dto.CreateParseTaskRequest;
import com.webpa.repository.ParseTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ParseTaskService {

    private final ParseTaskRepository parseTaskRepository;
    private final ParserOrchestrator orchestrator;

    public ParseTaskService(ParseTaskRepository parseTaskRepository, ParserOrchestrator orchestrator) {
        this.parseTaskRepository = parseTaskRepository;
        this.orchestrator = orchestrator;
    }

    @Transactional
    public ParseTask createTask(CreateParseTaskRequest request) {
        ParseTask task = ParseTask.builder()
                .queryText(normalizeQuery(request.query()))
                .marketplaces(request.marketplaces())
                .pagesToScan(request.pages())
                .pageSize(request.pageSize())
                .appendToExisting(request.appendToExisting())
                .comment(request.comment())
                .status(TaskStatus.QUEUED)
                .build();
        return parseTaskRepository.save(task);
    }

    @Transactional
    public ParseTask runTask(UUID taskId) {
        ParseTask task = parseTaskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        orchestrator.executeTask(task);
        return task;
    }

    @Transactional
    public ParseTask createAndRun(CreateParseTaskRequest request) {
        ParseTask task = createTask(request);
        orchestrator.executeTask(task);
        return task;
    }

    public List<ParseTask> findAll() {
        return parseTaskRepository.findAll();
    }

    public ParseTask get(UUID id) {
        return parseTaskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    private String normalizeQuery(String query) {
        if (!StringUtils.hasText(query)) {
            return query;
        }
        return query.trim();
    }
}
