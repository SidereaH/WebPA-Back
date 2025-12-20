package com.webpa.domain.enums;

public enum TaskStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED;

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED;
    }
}
