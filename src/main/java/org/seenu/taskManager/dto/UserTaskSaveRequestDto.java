package org.seenu.taskManager.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTaskSaveRequestDto {
    private String taskName;
    private boolean completed=false;
    @FutureOrPresent(message="the date must be today or any future date")
    private LocalDateTime dueDate;
    private boolean ispriority=false;
}
