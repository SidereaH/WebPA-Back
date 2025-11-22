package com.webpa.repository;

import com.webpa.domain.ParseTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParseTaskRepository extends JpaRepository<ParseTask, UUID> {
}
