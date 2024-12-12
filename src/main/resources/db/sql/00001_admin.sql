-- liquibase formatted sql

-- changeset chi:1
INSERT INTO User (username, password, role, created_time, updated_time) VALUES ('admin', '$argon2id$v=19$m=4096,t=3,p=1$jl9xlB8T1/yzPGMIfU2r8TTr5ZbNzcnr5seDgkRUiU8$aHM14nBXvvm8MOZd4iEE+RozbS3Nw5jfpRU9tyRG62v/oNI3XfFemQ1mAAwNKytvAUzEg/ytXNe7rQYabQDORQ', 'ADMIN', NOW(), NOW());
-- rollback DELETE FROM User WHERE username = 'admin';
