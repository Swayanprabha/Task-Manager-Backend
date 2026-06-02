package org.seenu.taskManager.repository;

import org.seenu.taskManager.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {
    Optional<TaskUser> findByEmail(String mail);
}
