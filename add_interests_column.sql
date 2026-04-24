USE `db_second_hub`;

ALTER TABLE `user_profile` ADD COLUMN `interests` VARCHAR(500) DEFAULT NULL AFTER `birthday`;