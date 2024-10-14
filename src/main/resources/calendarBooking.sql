CREATE DATABASE IF NOT EXISTS `calendarbooking`;
USE `calendarbooking`;

CREATE TABLE IF NOT EXISTS users (
   id VARCHAR(255) PRIMARY KEY,
   username VARCHAR(50) NOT NULL,
   password CHAR(80) NOT NULL,
   first_name VARCHAR(50),
   last_name VARCHAR(50),
   email VARCHAR(50),
   mobile VARCHAR(50),
   role ENUM('CALENDAR_OWNER', 'INVITEE')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `time_slots` (
    `id` VARCHAR(255) NOT NULL PRIMARY KEY,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `is_booked` BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `appointments` (
    `id` VARCHAR(255) NOT NULL PRIMARY KEY,
    `time_slot_id` VARCHAR(255) NOT NULL,
    `owner_id` VARCHAR(255) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `canceled_at` DATETIME NULL,
    FOREIGN KEY (`time_slot_id`) REFERENCES `time_slots`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `appointment_invitees` (
    `appointment_id` VARCHAR(255) NOT NULL,
    `invitee_id` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`appointment_id`, `invitee_id`),
    FOREIGN KEY (`appointment_id`) REFERENCES `appointments`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`invitee_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `availability` (
    `id` VARCHAR(255) NOT NULL PRIMARY KEY,
    `owner_id` VARCHAR(255) NOT NULL,
    `day_of_week` ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
