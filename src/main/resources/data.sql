INSERT INTO User (id, name, pass, grade) VALUES
('user1', 'Alice', 'password1', 'GUEST'),
('user2', 'Bob', 'password2', 'GUEST'),
('user3', 'Charlie', 'password3', 'GUEST'),
('user4', 'David', 'password4', 'GUEST'),
('user5', 'Eve', 'password5', 'GUEST'),
('user6', 'Frank', 'password6', 'ADMIN'),
('user7', 'Grace', 'password7', 'ADMIN');

INSERT INTO Board (title, content, user_id) VALUES
('Title 1', 'Content 1', 'user1'),
('Title 2', 'Content 2', 'user2'),
('Title 3', 'Content 3', 'user3'),
('Title 4', 'Content 4', 'user4'),
('Title 5', 'Content 5', 'user5'),
('Title 6', 'Content 6', 'user6'),
('Title 7', 'Content 7', 'user7'),
('Title 8', 'Content 8', 'user1'),
('Title 9', 'Content 9', 'user2'),
('Title 10', 'Content 10', 'user3'),
('Title 11', 'Content 11', 'user1'),
('Title 12', 'Content 12', 'user5'),
('Title 13', 'Content 13', 'user6'),
('Title 14', 'Content 14', 'user7'),
('Title 15', 'Content 15', 'user1');