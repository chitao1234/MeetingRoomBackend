-- liquibase formatted sql

-- changeset chi:1
ALTER TABLE User MODIFY COLUMN password VARCHAR(255);
-- rollback ALTER TABLE User MODIFY COLUMN password VARCHAR(100);