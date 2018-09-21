package io.agileintelligence.kanbantool.repository;

import io.agileintelligence.kanbantool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

    ProjectTask findByProjectSequence(String sequence);

    List<ProjectTask> findByProjectIdentiferOrderByPriority(String id);
}
