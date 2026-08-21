package org.seenu.taskManager.service;
import lombok.extern.slf4j.Slf4j;
import org.seenu.taskManager.ExceptionHandle.InvalidUserException;
import org.seenu.taskManager.ExceptionHandle.ResourceNotFoundException;
import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskRepository;
import org.seenu.taskManager.util.TaskUtil;
import org.seenu.taskManager.util.UserAuthUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserAuthUtil  userAuthUtil;
    private final HashMap<String,FilterService> filterServiceMap;
    TaskService(TaskRepository taskRepository, UserAuthUtil userAuthUtil,List<FilterService> filterServices) {
       this.taskRepository = taskRepository;
       this.userAuthUtil = userAuthUtil;
       this.filterServiceMap = new HashMap<>();
       for(FilterService filterService: filterServices) {
           String filterType = filterService.getFilterType();
           filterServiceMap.put(filterType,filterService);
       }
       log.info("TaskService initialized with {} filter strategies", filterServiceMap.size());
    }

    public String addNewTask(UserTaskSaveRequestDto userTaskSaveRequestDto) {
       TaskUser currentUser=userAuthUtil.getTheContextUser();
       log.info("Creating task for userId={}, taskName={}, dueDate={}, completed={}, priority={}",
               currentUser.getId(), userTaskSaveRequestDto.getTaskName(), userTaskSaveRequestDto.getDueDate(),
               userTaskSaveRequestDto.isCompleted(), userTaskSaveRequestDto.isIspriority());
       boolean exist=taskRepository.existsByTaskUserIdAndDueDateAndTaskName(currentUser.getId(),userTaskSaveRequestDto.getDueDate(),userTaskSaveRequestDto.getTaskName());
       if(exist)
       {
           log.warn("Duplicate task request rejected for userId={}, taskName={}, dueDate={}",
                   currentUser.getId(), userTaskSaveRequestDto.getTaskName(), userTaskSaveRequestDto.getDueDate());
           return "Task with the same due date already exists for the user.";
       }
       Task newTask=new Task();
       newTask.setTaskUser(currentUser);
       newTask.setDueDate(userTaskSaveRequestDto.getDueDate());
       newTask.setTaskName(userTaskSaveRequestDto.getTaskName());
       if(userTaskSaveRequestDto.isCompleted())newTask.setCompleted(true);
       if(userTaskSaveRequestDto.isIspriority()) newTask.setIspriority(true);
       Task savedTask=taskRepository.save(newTask);
       log.info("Task created successfully for userId={}, taskId={}", currentUser.getId(), savedTask.getId());
       return "Task added successfully.";
    }
    public List<TaskResponseDto> getAllTaskByDay(LocalDateTime day) {
       TaskUser currentUser=userAuthUtil.getTheContextUser();
       LocalDateTime today=day.withHour(0).withMinute(0).withSecond(0);
       LocalDateTime future=day.withHour(23).withMinute(59).withSecond(59);
       log.info("Fetching tasks for userId={}, from={}, to={}", currentUser.getId(), today, future);
       List<Task> myallNewtasks=taskRepository.getUserTask(currentUser.getId(),today,future);
       List<TaskResponseDto> taskResponseDtoList = TaskUtil.giveMyTask(myallNewtasks);
       log.info("Task fetch completed for userId={}, count={}", currentUser.getId(), taskResponseDtoList.size());
       return taskResponseDtoList;
    }
    public List<Integer> getDaysWithTask() {
       TaskUser currentUser=userAuthUtil.getTheContextUser();
       LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
       LocalDateTime end = LocalDateTime.now().withDayOfMonth(
               LocalDateTime.now().toLocalDate().lengthOfMonth()
       ).withHour(23).withMinute(59).withSecond(59);
       log.info("Fetching days with tasks for userId={}, start={}, end={}", currentUser.getId(), start, end);
       List<LocalDateTime> hasTask=taskRepository.findDaysWithTask(currentUser.getId(),start,end);
       List<Integer> daysWithTask = hasTask.stream().map((day)->day.getDayOfMonth()).collect(Collectors.toList());
       log.info("Days with tasks found for userId={}, days={}", currentUser.getId(), daysWithTask);
       return daysWithTask;

    }

    public String deleteMyTask(Long id) {
       TaskUser currentUser=userAuthUtil.getTheContextUser();
       log.info("Attempting to delete taskId={} for userId={}", id, currentUser.getId());
       Task mytask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("the requested task not found."));
       if(mytask.getTaskUser().getId()!=currentUser.getId()){
           log.warn("Unauthorized task deletion attempt for taskId={}, requestedByUserId={}, ownerUserId={}",
                   id, currentUser.getId(), mytask.getTaskUser().getId());
           throw new InvalidUserException("you r not authorised to update this task");
       }
       taskRepository.deleteById(id);
       log.info("Task deleted successfully. taskId={}, userId={}", id, currentUser.getId());
       return "task deleted successfully.";

    }

    public String updateMyTask(Long id, UserTaskSaveRequestDto userTaskSaveRequestDto) {
       TaskUser currentUser=userAuthUtil.getTheContextUser();
       log.info("Attempting to update taskId={} for userId={}, newTaskName={}, newDueDate={}",
               id, currentUser.getId(), userTaskSaveRequestDto.getTaskName(), userTaskSaveRequestDto.getDueDate());
       Task mytask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("the requested task not found."));
       if(mytask.getTaskUser().getId()!=currentUser.getId()){
           log.warn("Unauthorized task update attempt for taskId={}, requestedByUserId={}, ownerUserId={}",
                   id, currentUser.getId(), mytask.getTaskUser().getId());
           throw new InvalidUserException("you r not authorised to update this task");
       }
       mytask.setDueDate(userTaskSaveRequestDto.getDueDate());
       mytask.setTaskName(userTaskSaveRequestDto.getTaskName());
       if (userTaskSaveRequestDto.isCompleted()) mytask.setCompleted(true);
       if (userTaskSaveRequestDto.isIspriority()) mytask.setIspriority(true);
       taskRepository.save(mytask);
       log.info("Task updated successfully. taskId={}, userId={}", id, currentUser.getId());
       return "Task updated successfully.";
    }

    public List<TaskResponseDto> getFilteredTasks(List<String> filterType, String date) {
       log.info("Applying filters {} to tasks for date={}", filterType, date);
       List<TaskResponseDto> result= this.getAllTaskByDay(LocalDateTime.parse(date));
       if(result.isEmpty()){
           log.info("No tasks found for date={}, filters={} ", date, filterType);
           return result;
       }
       if (filterType == null || filterType.isEmpty()) return result;
       for(String type: filterType) {
           FilterService filterService = filterServiceMap.get(type);
           if (filterService == null) {
               log.error("Invalid filter type requested: {}", type);
               throw new RuntimeException("sorry, u have entrered an invalid filter type");
           }
           result = filterService.getMyTasksByFilter(result);
           log.info("Filter {} applied successfully. Remaining tasks count={}", type, result.size());
       }
       log.info("Filter processing completed for date={}, finalTaskCount={}", date, result.size());
        return result;
    }
}
 
