-- liquibase formatted sql

-- changeset chi:1
ALTER TABLE Reservation 
MODIFY COLUMN start_time DATETIME,
MODIFY COLUMN end_time DATETIME,
DROP COLUMN meeting_date;
-- rollback ALTER TABLE meeting_room_booking 
-- rollback MODIFY COLUMN start_time TIME,
-- rollback MODIFY COLUMN end_time TIME,
-- rollback ADD COLUMN meeting_date DATE;
