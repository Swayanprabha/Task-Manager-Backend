package org.seenu.taskManager.repository;

import org.seenu.taskManager.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {
    Optional<TaskUser> findByEmail(String mail);
}
