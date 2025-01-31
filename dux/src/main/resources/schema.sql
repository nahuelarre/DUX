DROP TABLE IF EXISTS teams;

CREATE TABLE teams (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       league VARCHAR(255) NOT NULL,
                       country VARCHAR(255) NOT NULL
);
