-- liquibase formatted sql

-- changeset chi:1
INSERT INTO User (username, password, role, created_time, updated_time) VALUES ('admin', '$argon2id$v=19$m=4096,t=3,p=1$MzBlYzc3NTdjMjMyZGNmNmI2NmU3YTZmZDgyODFlZjk1YTgyYjNhMGY5MjJjZjgyOWFhYmNmYmI0YmU0NmVkOA$IjTKgJnuIpE6DqHUM30GVoL738YBRmCj1h08rrA3F3bmxdsYcBwJPE3FymK3zQiybca5UAKaBg3Wf2Yq4x8gPg', 'ADMIN', NOW(), NOW());
-- rollback DELETE FROM User WHERE username = 'admin';
