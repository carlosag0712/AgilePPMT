package io.agileintelligence.kanbantool.service;


import io.agileintelligence.kanbantool.domain.Backlog;
import io.agileintelligence.kanbantool.domain.ProjectTask;
import io.agileintelligence.kanbantool.exceptions.ProjectNotFoundException;
import io.agileintelligence.kanbantool.repository.BacklogRepository;
import io.agileintelligence.kanbantool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private CheckProjectOwnerService checkProjectOwnerService;

    @Autowired
    private BacklogRepository backlogRepository;


    public ProjectTask findByProjectIdentifier(String ProjectId, String backlogId, String username){

        return getProjectTask(ProjectId, backlogId, username);

    }



    public ProjectTask updateByProjectIdentifier(ProjectTask projectTask, String ProjectTaskId, String backlogId, String username){

        Backlog backlog =backlogRepository.findByProjectIdentifier(backlogId);
        if( backlog== null){
            throw new ProjectNotFoundException("Project with code: "+backlogId+" does not exist");
        }

        checkProjectOwnerService.PROJECT_checkProjectOwner(backlogId,username);

        ProjectTask projectTask1 = projectTaskRepository.findByProjectSequence(ProjectTaskId);

        if(projectTask1 == null){
            throw new ProjectNotFoundException("Project Task: "+ProjectTaskId+" not found");
        }

        if(!projectTask1.getBacklog().getProjectIdentifier().toUpperCase().equals(backlogId.toUpperCase())){
            throw new ProjectNotFoundException("Project Task does not exist in project: "+backlogId);
        }

        projectTask1 = projectTask;

       // projectTask1.setBacklog(backlog);
        return projectTaskRepository.save(projectTask1);

    }

    public void deleteByProjectIdentifier(String ProjectId, String backlogId, String username){

        Backlog backlog =backlogRepository.findByProjectIdentifier(backlogId);
        if( backlog== null){
            throw new ProjectNotFoundException("Project with code: "+backlogId+" does not exist");
        }

        checkProjectOwnerService.PROJECT_checkProjectOwner(backlogId,username);

        ProjectTask projectTask1 = projectTaskRepository.findByProjectSequence(ProjectId);

        if(projectTask1 == null){
            throw new ProjectNotFoundException("Project Task: "+ProjectId+" not found");
        }

        if(!projectTask1.getBacklog().getProjectIdentifier().equals(backlogId)){
            throw new ProjectNotFoundException("Project Task does not exist in project: "+backlogId+" not found");
        }


        List<ProjectTask> pts = backlog.getProjectTasks();
        pts.remove(projectTask1);
        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask1);

    }


    private ProjectTask getProjectTask(String ProjectId, String backlogId, String username) {
        Backlog backlog =backlogRepository.findByProjectIdentifier(backlogId);
        if( backlog== null){
            throw new ProjectNotFoundException("Project with code: "+backlogId+" does not exist");
        }

        checkProjectOwnerService.PROJECT_checkProjectOwner(backlogId,username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ProjectId);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task: "+ProjectId+" not found");
        }

        if(!projectTask.getBacklog().getProjectIdentifier().equals(backlogId)){
            throw new ProjectNotFoundException("Project Task does not exist in project: "+backlogId+" not found");
        }
        return projectTask;
    }


}
