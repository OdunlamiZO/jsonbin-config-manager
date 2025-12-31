package io.github.odunlamizo.jcm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_model")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Env> envs;

    @Column(name = "external_id", nullable = false)
    private String externalId; // simply the collection id on JSONBIN.io

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
