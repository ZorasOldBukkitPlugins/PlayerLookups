CREATE TABLE IF NOT EXISTS pl_ips (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `player_id` INT UNSIGNED NOT NULL,
  `ip` VARCHAR(16) NOT NULL,
  FOREIGN KEY (`player_id`) REFERENCES pl_uuids(`id`)
    ON UPDATE CASCADE
    ON DELETE CASCADE
) ENGINE=InnoDB;