-- liquibase formatted sql

-- changeset chi:1
CREATE TABLE MeetingRoom (
    meeting_room_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    area DECIMAL(10, 2) NULL,
    photo_url TEXT NULL,
    description TEXT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (meeting_room_id),
    CONSTRAINT chk_capacity CHECK (capacity > 0)
);
-- rollback DROP TABLE MeetingRoom;

-- changeset chi:2
CREATE TABLE User (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NULL UNIQUE,
    phone VARCHAR(15) NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    avatar_url TEXT NULL,
    last_login_time DATETIME NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE INDEX idx_username (username)
);
-- rollback DROP TABLE User;

-- changeset chi:3
CREATE TABLE Log (
    log_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    operation_details TEXT NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARBINARY(16) NULL,
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);
-- rollback DROP TABLE Log;

-- changeset chi:4
CREATE TABLE Notification (
    notification_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    INDEX idx_user_id (user_id)
);
-- rollback DROP TABLE Notification;

-- changeset chi:5
CREATE TABLE Reservation (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    meeting_room_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    participant_count INT NOT NULL,
    meeting_subject TEXT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    rejection_reason TEXT NULL,
    approval_time DATETIME NULL,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (meeting_room_id) REFERENCES MeetingRoom(meeting_room_id),
    CONSTRAINT chk_time CHECK (start_time < end_time),
    CONSTRAINT chk_participant_count CHECK (participant_count > 0),
    INDEX idx_user_id (user_id),
    INDEX idx_meeting_room_id (meeting_room_id)
);
-- rollback DROP TABLE Reservation;
