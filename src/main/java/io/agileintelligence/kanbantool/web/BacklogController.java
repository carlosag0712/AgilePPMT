package io.agileintelligence.kanbantool.web;


import io.agileintelligence.kanbantool.domain.Backlog;
import io.agileintelligence.kanbantool.domain.ProjectTask;
import io.agileintelligence.kanbantool.service.BacklogService;
import io.agileintelligence.kanbantool.service.MapValidationErrorsService;
import io.agileintelligence.kanbantool.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private BacklogService backlogService;

    @Autowired
    private MapValidationErrorsService mapValidationErrorsService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String projectId, Principal principal){
        return new ResponseEntity<List<ProjectTask>>(backlogService.findBacklogByProjectId(projectId, principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                BindingResult result, @PathVariable String backlog_id, Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorsService.MapValidationErrorsService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = backlogService.addProjectTask(backlog_id,projectTask,principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){

        ProjectTask projectTask = projectTaskService.findByProjectIdentifier(pt_id,backlog_id,principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask,HttpStatus.OK);

    }


    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorsService.MapValidationErrorsService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.updateByProjectIdentifier(projectTask,pt_id,backlog_id,principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){

        projectTaskService.deleteByProjectIdentifier(pt_id,backlog_id,principal.getName());

        return new ResponseEntity<String>("Project Task "+pt_id+" was deleted successfully", HttpStatus.OK);
    }


}
