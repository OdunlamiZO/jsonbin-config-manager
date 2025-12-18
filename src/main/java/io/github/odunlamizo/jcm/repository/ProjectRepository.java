package io.github.odunlamizo.jcm.repository;

import io.github.odunlamizo.jcm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {}
