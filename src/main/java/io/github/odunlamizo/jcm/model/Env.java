package io.github.odunlamizo.jcm.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "env_model")
public class Env {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(name = "external_id", nullable = false)
    private String externalId; // simply the bin id on JSONBIN.io

    @Transient private EnvRecord record; // simply the bin record
}
