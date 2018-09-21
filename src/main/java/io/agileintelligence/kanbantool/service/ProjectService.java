package io.agileintelligence.kanbantool.service;

import io.agileintelligence.kanbantool.domain.Backlog;
import io.agileintelligence.kanbantool.domain.Project;
import io.agileintelligence.kanbantool.domain.User;
import io.agileintelligence.kanbantool.exceptions.ProjectIdAlreadyExistException;
import io.agileintelligence.kanbantool.exceptions.ProjectNotFoundException;
import io.agileintelligence.kanbantool.repository.BacklogRepository;
import io.agileintelligence.kanbantool.repository.ProjectRepository;
import io.agileintelligence.kanbantool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private CheckProjectOwnerService checkProjectOwnerService;

    public Project saveProject(Project project, String username) {



        checkProjectOwnerService.ProjectOwnerOnly(project, username);

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            //The project id will only be null  when it is a new project, so we only instantiate a new backlog
            //for a new project
            project.setProjectId(project.getProjectId().toUpperCase());
            /**
             * Here is the logic behind this, when we update the project we do not want to update the backlog
             * so when the project has a null id, we know that we are creating a project from scratch so we need a new backlog
             */
            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectId().toUpperCase());
            }
            /**
             * here, if we pass a project id, that means that we are updating the project information
             * however, we do not want to update the backlog and we don't want to duplicate it either
             * for this reason, we need to reset the backlog on update, this way we
             * don't pass an null backlog for not including the backlog attribute when sending the json update
             * we protect the backlog from changes related to project info
             */

            if(project.getId()!= null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectId()));
            }
            return projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdAlreadyExistException("Project ID already exists");
        }
    }

    public Project findByProjectId(String projectId, String username){
        return checkProjectOwnerService.PROJECT_checkProjectOwner(projectId,username);
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public Iterable<Project> findAllProjectsByUser(String username){
        return projectRepository.findAllByUserUsername(username);
    }

    public void deleteProjectByProjectId(String projectId, String username){
        projectRepository.delete(checkProjectOwnerService.PROJECT_checkProjectOwner(projectId,username));
    }



}
