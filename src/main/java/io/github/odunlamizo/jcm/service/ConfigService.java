package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Project;
import java.util.List;

public interface ConfigService {

    void createProject(String name, String description, String collectionId);

    void archiveProject(int projectId);

    List<Project> getAllProjects();

    void addVariable(int projectId, int envId, String key, String value);

    void deleteVariable(int projectId, int envId, String key);
}
