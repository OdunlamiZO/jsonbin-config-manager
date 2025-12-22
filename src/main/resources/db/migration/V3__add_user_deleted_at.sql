-- Add soft-delete support to users
ALTER TABLE user_model ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL;