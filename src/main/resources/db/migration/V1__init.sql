-- Flyway initial schema migration for H2

-- Users
CREATE TABLE IF NOT EXISTS user_model (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Projects
CREATE TABLE IF NOT EXISTS project_model (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    external_id VARCHAR(255) NOT NULL
);

-- Environments
CREATE TABLE IF NOT EXISTS env_model (
    id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_env_project FOREIGN KEY (project_id)
        REFERENCES project_model(id)
        ON DELETE CASCADE
);

-- Indexes for faster lookups
CREATE INDEX IF NOT EXISTS idx_env_project ON env_model(project_id);
