
-- Inserting sample users with encrypted passwords
INSERT INTO users (username, password, email, name, surname, imagePath)
VALUES ('user1', '$2a$10$Kv8IeOMp3TxuM6TANkW8o.BPOnRy5T2ZMGPMpJawWFe8OyDUhF3o2', 'user1@example.com', 'John', 'Doe', '/images/avatar/avatar1.png');

INSERT INTO users (username, password, email, name, surname, imagePath)
VALUES ('user2', '$2a$10$rYE.gZtB1VeBiISfydk2xOJXSc.Zv6NtP.JHdZvR8OnDxzr8xJbuC', 'user2@example.com', 'Jane', 'Doe', '/images/avatar/avatar2.png');

-- Inserting roles for the users
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1); -- user1 has role with id 1 (adjust the role_id accordingly)

INSERT INTO user_roles (user_id, role_id)
VALUES (2, 2); -- user2 has role with id 2 (adjust the role_id accordingly)

-- Inserting friendships between users
INSERT INTO user_friends (user_id, friend_id)
VALUES (1, 2); -- user1 is friends with user2

INSERT INTO user_friends (user_id, friend_id)
VALUES (2, 1); -- user2 is friends with user1