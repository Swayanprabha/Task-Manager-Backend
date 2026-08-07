package org.seenu.taskManager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {
    private Long id;
    private String taskName;
    private boolean completed;
    private LocalDateTime dueDate;
    private boolean ispriority;
}
