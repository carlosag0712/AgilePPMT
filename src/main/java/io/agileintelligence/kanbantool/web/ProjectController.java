package io.agileintelligence.kanbantool.web;

import io.agileintelligence.kanbantool.domain.Project;
import io.agileintelligence.kanbantool.service.MapValidationErrorsService;
import io.agileintelligence.kanbantool.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorsService mapValidationErrorsService;

    //Create a new Project
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorsService.MapValidationErrorsService(result);
        if(errorMap != null) return errorMap;

        Project newProject = projectService.saveProject(project, principal.getName());

        return new ResponseEntity<Project>(newProject,HttpStatus.CREATED);
    }

    //Get an existing Project by Id
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
        Project project = projectService.findByProjectId(projectId, principal.getName());

        return new ResponseEntity<Project>(project, HttpStatus.OK);

    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByProjectId(projectId, principal.getName());

        return new ResponseEntity<String>("Project with Id: '"+projectId+"' was deleted succcesfully", HttpStatus.OK);
    }

    //Get All existing projects

    @GetMapping("/all")
    public Iterable<Project> getProjects(){
        return projectService.findAllProjects();
    }

    @GetMapping("/myProjects")
    public Iterable<Project> getMyProjects(Principal principal){
        System.out.println(principal.getName());
        return projectService.findAllProjectsByUser(principal.getName());
    }


}
