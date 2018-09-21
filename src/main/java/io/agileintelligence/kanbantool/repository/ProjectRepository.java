package io.agileintelligence.kanbantool.repository;

import io.agileintelligence.kanbantool.domain.Project;
import io.agileintelligence.kanbantool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {
    //Find by projectId
    Project findByProjectId(String projectId);
    Iterable<Project>findAllByUserUsername(String user);
}
