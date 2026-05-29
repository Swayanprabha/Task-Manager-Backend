package org.seenu.taskManager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private boolean completed;
    private LocalDateTime dueDate;
    private boolean ispriority;
    @JoinColumn(name="user_id")
    @ManyToOne()
    private TaskUser taskUser;


}
