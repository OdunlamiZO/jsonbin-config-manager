package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Project;
import java.util.List;

public interface ConfigService {

    void createProject(String name, String description, String collectionId);

    List<Project> getAllProjects();

    void addVariable(int projectId, int envId, String key, String value);
}
