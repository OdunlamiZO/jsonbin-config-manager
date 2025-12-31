package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Env;
import io.github.odunlamizo.jcm.model.EnvRecord;
import io.github.odunlamizo.jcm.model.Project;
import io.github.odunlamizo.jcm.repository.EnvRepository;
import io.github.odunlamizo.jcm.repository.ProjectRepository;
import io.github.odunlamizo.jsonbin.JsonBin;
import io.github.odunlamizo.jsonbin.model.Bin;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final JsonBin jsonBin;

    private final EnvRepository envRepository;

    private final ProjectRepository projectRepository;

    private static final String[] DEFAULT_ENVIRONMENTS = {"dev", "staging", "prod"};

    @Override
    @Transactional
    public void createProject(String name, String description, String collectionId) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }

        Bin<String> collectionBin;
        if (Objects.nonNull(collectionId)) {
            collectionBin = jsonBin.updateCollection(collectionId, name);
        } else {
            collectionBin = jsonBin.createCollection(name);
        }

        Project project =
                Project.builder()
                        .name(name)
                        .description(description)
                        .externalId(collectionBin.getRecord())
                        .build();

        project = projectRepository.save(project);

        for (String envName : DEFAULT_ENVIRONMENTS) {
            Bin<EnvRecord> bin =
                    jsonBin.createBin(new EnvRecord(), envName, true, project.getExternalId());

            Env env =
                    Env.builder()
                            .name(envName)
                            .project(project)
                            .externalId(bin.getMetadata().getId())
                            .build();

            envRepository.save(env);
        }
    }

    @Override
    public void archiveProject(int projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (Objects.nonNull(project)) {
            project.setDeletedAt(LocalDateTime.now());
            projectRepository.save(project);
        }
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAllByDeletedAtIsNull();

        for (Project project : projects) {
            for (Env env : project.getEnvs()) {
                Bin<EnvRecord> bin = jsonBin.readBin(env.getExternalId(), EnvRecord.class);
                env.setRecord(bin.getRecord());
            }
        }

        return projects;
    }

    @Override
    public void addVariable(int projectId, int envId, String key, String value) {
        Env env = envRepository.findById(envId).orElseThrow();
        EnvRecord record = jsonBin.readBin(env.getExternalId(), EnvRecord.class).getRecord();

        Map<String, String> values =
                Objects.requireNonNullElse(record.getValues(), new HashMap<>());
        values.put(key, value);

        jsonBin.updateBin(new EnvRecord(values), env.getExternalId());
    }

    @Override
    public void deleteVariable(int projectId, int envId, String key) {
        Env env = envRepository.findById(envId).orElseThrow();
        EnvRecord record = jsonBin.readBin(env.getExternalId(), EnvRecord.class).getRecord();

        Map<String, String> values =
                Objects.requireNonNullElse(record.getValues(), new HashMap<>());
        values.remove(key);

        jsonBin.updateBin(new EnvRecord(values), env.getExternalId());
    }
}
