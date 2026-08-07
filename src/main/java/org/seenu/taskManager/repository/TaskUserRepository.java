package org.seenu.taskManager.repository;

import org.seenu.taskManager.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {
    Optional<TaskUser> findByEmail(String mail);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Task t where t.taskUser.id=:userId")
    void deleteMyTasks(@Param("userId") Long userid);
}
