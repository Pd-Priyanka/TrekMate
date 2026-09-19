CREATE INDEX idx_treks_state ON treks (state);
CREATE INDEX idx_treks_difficulty ON treks (difficulty);
CREATE INDEX idx_reviews_trek_created_at ON reviews (trek_id, created_at);
