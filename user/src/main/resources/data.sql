INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

INSERT INTO users(notification_type, role_id, "e-mail", full_name, password, telegram)
VALUES ('TELEGRAM', 1, 'admin@mail.co', 'Admin Korniev',
        '$2a$10$bIjGIiFFRtxMLutKDvuPTeTtpROhRceXImnIqzOUkkwN/aBMJE/hu', '@mskdrttt');