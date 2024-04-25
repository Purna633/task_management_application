package com.purna.service;

import com.purna.model.Task;
import com.purna.model.TaskStatus;
import com.purna.repository.TaskRepository;
import org.bouncycastle.pqc.crypto.ExhaustedPrivateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImplementation implements  TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public Task createTask(Task task, String requesterRole) throws Exception {
        if(!requesterRole.equals(("ROLE_ADMIN")))
        {
            throw new Exception("Only admin can create the task");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());


        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.findById(id).orElseThrow(()->new ExhaustedPrivateKeyException("task not found"+id));
    }

    @Override
    public List<Task> getAllTask(TaskStatus status) {
        List<Task> allTask=taskRepository.findAll();

        List<Task> filteredTasks=allTask.stream().filter(
                task -> status==null || task.getStatus().name().equalsIgnoreCase(status.toString())

        ).collect(Collectors.toList());

        return filteredTasks;
    }

    @Override
    public Task updateTask(Long id, Task updateTask, Long userId) throws Exception {

        Task existingTask = getTaskById(id);
        if(updateTask.getTitle()!=null)
        {
            existingTask.setTitle(updateTask.getTitle());
        }
        if(updateTask.getImage()!=null){
            existingTask.setImage(updateTask.getImage());
        }
        if(updateTask.getDescription()!=null)
        {
            existingTask.setDescription(updateTask.getDescription());

        }
        if(updateTask.getStatus()!=null)
        {
            existingTask.setStatus(updateTask.getStatus());
        }
        if(updateTask.getDeadline()!=null)
        {
            existingTask.setDeadline(updateTask.getDeadline());
        }


        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) throws Exception {

        getTaskById(id);
        taskRepository.deleteById(id);

    }

    @Override
    public Task assignedToUser(Long userId, Long taskId) throws Exception {
        Task existingTask= getTaskById(taskId);
        existingTask.setAssignedUserId(userId);
        existingTask.setStatus(TaskStatus.DONE);
        return taskRepository.save(existingTask);
    }

    @Override
    public List<Task> assignedUsersTask(Long userId, TaskStatus status) {

       List<Task> allTask=taskRepository.findByAssignedUserId(userId);
        List<Task> filteredTasks=allTask.stream().filter(
                task -> status==null || task.getStatus().name().equalsIgnoreCase(status.toString())

        ).collect(Collectors.toList());

        return filteredTasks;
    }

    @Override
    public Task completeTask(Long taskId) throws Exception {
        Task task=getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);

        return taskRepository.save(task);
    }
}
