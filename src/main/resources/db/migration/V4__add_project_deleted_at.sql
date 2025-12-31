-- Add soft-delete support to projects
ALTER TABLE project_model ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL;