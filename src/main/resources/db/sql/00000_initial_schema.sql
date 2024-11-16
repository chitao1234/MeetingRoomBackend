-- liquibase formatted sql

-- changeset chi:1
CREATE TABLE MeetingRoom (
    meeting_room_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    capacity INT NOT NULL,
    area DECIMAL NULL,
    photo_url TEXT NULL,
    description TEXT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (meeting_room_id)
);
-- rollback DROP TABLE MeetingRoom;

-- changeset chi:2
CREATE TABLE User (
    user_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NULL,
    phone VARCHAR(15) NULL,
    role VARCHAR(20) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME NULL,
    PRIMARY KEY (user_id)
);
-- rollback DROP TABLE User;

-- changeset chi:3
CREATE TABLE Log (
    log_id INT NOT NULL,
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
    notification_id INT NOT NULL,
    user_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);
-- rollback DROP TABLE Notification;

-- changeset chi:5
CREATE TABLE Reservation (
    reservation_id INT NOT NULL,
    user_id INT NOT NULL,
    meeting_room_id INT NOT NULL,
    meeting_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    participant_count INT NOT NULL,
    meeting_subject TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rejection_reason TEXT NULL,
    approval_time DATETIME NULL,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (meeting_room_id) REFERENCES MeetingRoom(meeting_room_id)
);
-- rollback DROP TABLE Reservation;
