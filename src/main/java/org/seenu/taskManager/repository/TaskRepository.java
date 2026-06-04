package org.seenu.taskManager.repository;
import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    boolean existsByTaskUserAndDueDate(TaskUser currentUser, LocalDateTime  dueDate);
    Task save(Task task);

    @Query("select t from Task t where t.taskUser.id=:userId and t.dueDate between :past and :today")
    List<Task> getUserTask(@Param("userId") Long id,@Param("today") LocalDateTime today, @Param("past") LocalDateTime past);

    List<Task> findByDueDate(LocalDateTime day);
    @Query("select distinct t.dueDate from Task t where t.taskUser.id=:userId and t.dueDate between :start and :end")
    List<LocalDateTime> findDaysWithTask(@Param("userId") Long id,@Param("start") LocalDateTime start, @Param("end")LocalDateTime end);
}
