CREATE TABLE favorites (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    trek_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_favorites_user_trek UNIQUE (user_id, trek_id),
    CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_favorites_trek FOREIGN KEY (trek_id) REFERENCES treks (id)
);

CREATE INDEX idx_favorites_user_created_at ON favorites (user_id, created_at);
