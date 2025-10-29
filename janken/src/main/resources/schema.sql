CREATE TABLE IF NOT EXISTS janken_history (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  user_hand VARCHAR(16) NOT NULL,
  cpu_hand VARCHAR(16) NOT NULL,
  result VARCHAR(16) NOT NULL,
  played_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_janken_history_username_played_at ON janken_history(username, played_at DESC);
