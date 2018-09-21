package io.agileintelligence.kanbantool.service;

import io.agileintelligence.kanbantool.domain.Backlog;
import io.agileintelligence.kanbantool.domain.ProjectTask;
import io.agileintelligence.kanbantool.repository.BacklogRepository;
import io.agileintelligence.kanbantool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacklogService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private CheckProjectOwnerService checkProjectOwnerService;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public List<ProjectTask> findBacklogByProjectId(String identifier, String username){

        checkProjectOwnerService.PROJECT_checkProjectOwner(identifier, username);
        //return backlogRepository.findByProjectIdentifier(identifier);
        return projectTaskRepository.findByProjectIdentiferOrderByPriority(identifier);
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){
        //Check if the user is the owner of the project. Remember, the project object and the backlog share the same exact String id
        //so we can get away with
        checkProjectOwnerService.PROJECT_checkProjectOwner(projectIdentifier,username);

        //Now let's find the Backlog object
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        //Now let's set the backlog for the project task we are creating
        projectTask.setBacklog(backlog);

        //Set Sequence

        /*
        i.e PROJ-0, PROJ-1

        We also want it in a way that if PROJ-33 is erased before PROJ-34 is created, the next id given out by the server
        is PROJ-34

         */

        //Extract current count

        Integer BacklogSequence = backlog.getPTSequence();

        BacklogSequence++;

        projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
        projectTask.setProjectIdentifer(projectIdentifier);

        backlog.setPTSequence(BacklogSequence);

        //In most professional project management tools outthere, the tool will determine an initial state for the task, this is
        //a 100% optional

        if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }

        if(projectTask.getPriority()== 0||projectTask.getPriority()==null ){
            projectTask.setPriority(3);
        }

        return projectTaskRepository.save(projectTask);
    }
}
