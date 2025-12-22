-- Add name and last_seen columns to user_model
ALTER TABLE user_model ADD COLUMN IF NOT EXISTS name VARCHAR(255) NOT NULL DEFAULT 'John Doe';
ALTER TABLE user_model ADD COLUMN IF NOT EXISTS last_seen TIMESTAMP NULL;