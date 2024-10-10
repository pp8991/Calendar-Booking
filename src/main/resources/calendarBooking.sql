CREATE DATABASE IF NOT EXISTS `calendarbooking`;
USE `calendarbooking`;

-- Users Table: Stores information for both Calendar Owners and Invitees
CREATE TABLE IF NOT EXISTS users (
   id INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(50) NOT NULL,
   password CHAR(80) NOT NULL,
   first_name VARCHAR(50),
   last_name VARCHAR(50),
   email VARCHAR(50),
   mobile VARCHAR(50),
   role ENUM('CALENDAR_OWNER', 'INVITEE')
);

-- TimeSlot: Represents available time slots that a Calendar Owner offers for appointments.
CREATE TABLE IF NOT EXISTS `time_slots` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `owner_id` INT(11) NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `is_booked` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Appointment: Represents the actual appointment between an Invitee and a Calendar Owner.
CREATE TABLE IF NOT EXISTS `appointments` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `time_slot_id` INT(11) NOT NULL,
    `invitee_id` INT(11) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `canceled_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`time_slot_id`) REFERENCES `time_slots`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`invitee_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Availability: Represents the availability schedule for the Calendar Owner
CREATE TABLE IF NOT EXISTS `availability` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `owner_id` INT(11) NOT NULL,
    `day_of_week` ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
