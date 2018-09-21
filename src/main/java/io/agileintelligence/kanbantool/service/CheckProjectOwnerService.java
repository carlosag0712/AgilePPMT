package io.agileintelligence.kanbantool.service;


import io.agileintelligence.kanbantool.domain.Project;
import io.agileintelligence.kanbantool.exceptions.ProjectNotFoundException;
import io.agileintelligence.kanbantool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckProjectOwnerService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project PROJECT_checkProjectOwner(String projectId, String username){

        Project project = projectRepository.findByProjectId(projectId);

        if(project == null){
            throw new ProjectNotFoundException("Project not found");
        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;

    }


    public void ProjectOwnerOnly(Project project, String username) {
        if(project.getId()!=null){
            Project existingProject = projectRepository.findByProjectId(project.getProjectId());

            if(existingProject !=null && (!existingProject.getUser().getUsername().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("No project found to update, please create a project");

            }

        }
    }


}
