package com.webpa.web.controller;

import com.webpa.dto.CreateParseTaskRequest;
import com.webpa.dto.ParseTaskResponse;
import com.webpa.service.ParseTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class ParserTaskController {

    private final ParseTaskService parseTaskService;

    public ParserTaskController(ParseTaskService parseTaskService) {
        this.parseTaskService = parseTaskService;
    }

    @PostMapping
    public ResponseEntity<ParseTaskResponse> createTask(
            @Valid @RequestBody CreateParseTaskRequest request,
            @RequestParam(name = "autorun", defaultValue = "true") boolean autorun
    ) {
        var task = autorun ? parseTaskService.createAndRun(request) : parseTaskService.createTask(request);
        return ResponseEntity.ok(ParseTaskResponse.fromEntity(task));
    }

    @PostMapping("/{id}/run")
    public ResponseEntity<ParseTaskResponse> runExisting(@PathVariable UUID id) {
        var task = parseTaskService.runTask(id);
        return ResponseEntity.ok(ParseTaskResponse.fromEntity(task));
    }

    @GetMapping
    public List<ParseTaskResponse> list() {
        return parseTaskService.findAll().stream()
                .map(ParseTaskResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ParseTaskResponse get(@PathVariable UUID id) {
        return ParseTaskResponse.fromEntity(parseTaskService.get(id));
    }
}
