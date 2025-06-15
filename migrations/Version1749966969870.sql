CREATE TABLE IF NOT EXISTS `user`
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(255)                             NULL,
    last_name    VARCHAR(255)                             NULL,
    username     VARCHAR(255)                             NOT NULL,
    reference_id INT                                      NULL,
    created_at   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL
);
CREATE TABLE IF NOT EXISTS user_stat
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    `user`     INT                                      NOT NULL,
    won        INT         DEFAULT 0                    NOT NULL,
    lost       INT         DEFAULT 0                    NOT NULL,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_user_stat_user__id FOREIGN KEY (`user`) REFERENCES `user` (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE TABLE IF NOT EXISTS word
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    word       VARCHAR(255)                             NOT NULL,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL
);
ALTER TABLE word
    ADD CONSTRAINT word_word_unique UNIQUE (word);
