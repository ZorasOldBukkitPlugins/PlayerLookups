CREATE TABLE IF NOT EXISTS pl_uuids (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `uuid` VARCHAR(36) NOT NULL UNIQUE
) ENGINE=InnoDB;