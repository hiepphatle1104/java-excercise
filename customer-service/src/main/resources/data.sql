-- Đảm bảo bảng 'customer' tồn tại, khớp với Customer entity trong Java
CREATE TABLE IF NOT EXISTS customer
(
    id              UUID PRIMARY KEY,
    email           VARCHAR(255) UNIQUE NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    address         VARCHAR(255),
    phone_number    VARCHAR(25),
    created_date    DATE NOT NULL
    );

-- Chèn dữ liệu mẫu cho bảng customer
INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       'John', 'Doe',
       'john.doe@example.com',
       '123 Main St, Springfield',
       '090-1111-2222',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-01-10'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '123e4567-e89b-12d3-a456-426614174001',
       'Jane', 'Smith',
       'jane.smith@example.com',
       '456 Elm St, Shelbyville',
       '091-2222-3333',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-12-01'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '123e4567-e89b-12d3-a456-426614174002',
       'Alice', 'Johnson',
       'alice.johnson@example.com',
       '789 Oak St, Capital City',
       '092-3333-4444',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2022-06-20'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '123e4567-e89b-12d3-a456-426614174002');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '123e4567-e89b-12d3-a456-426614174003',
       'Bob', 'Brown',
       'bob.brown@example.com',
       '321 Pine St, Springfield',
       '093-4444-5555',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-05-14'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '123e4567-e89b-12d3-a456-426614174003');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '123e4567-e89b-12d3-a456-426614174004',
       'Emily', 'Davis',
       'emily.davis@example.com',
       '654 Maple St, Shelbyville',
       '094-5555-6666',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-03-01'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '123e4567-e89b-12d3-a456-426614174004');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174005',
       'Michael', 'Green',
       'michael.green@example.com',
       '987 Cedar St, Springfield',
       '095-6666-7777',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-02-15'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174005');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174006',
       'Sarah', 'Taylor',
       'sarah.taylor@example.com',
       '123 Birch St, Shelbyville',
       '096-7777-8888',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-08-25'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174006');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174007',
       'David', 'Wilson',
       'david.wilson@example.com',
       '456 Ash St, Capital City',
       '097-8888-9999',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2022-10-10'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174007');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174008',
       'Laura', 'White',
       'laura.white@example.com',
       '789 Palm St, Springfield',
       '098-9999-0000',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-04-20'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174008');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174009',
       'James', 'Harris',
       'james.harris@example.com',
       '321 Cherry St, Shelbyville',
       '089-1234-5678',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-06-30'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174009');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174010',
       'Emma', 'Moore',
       'emma.moore@example.com',
       '654 Spruce St, Capital City',
       '088-2345-6789',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-01-22'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174010');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174011',
       'Ethan', 'Martinez',
       'ethan.martinez@example.com',
       '987 Redwood St, Springfield',
       '087-3456-7890',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-05-12'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174011');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174012',
       'Sophia', 'Clark',
       'sophia.clark@example.com',
       '123 Hickory St, Shelbyville',
       '086-4567-8901',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2022-11-11'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174012');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174013',
       'Daniel', 'Lewis',
       'daniel.lewis@example.com',
       '456 Cypress St, Capital City',
       '085-5678-9012',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2023-09-19'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174013');

INSERT INTO customer (id, first_name, last_name, email, address, phone_number, hashed_password, created_date)
SELECT '223e4567-e89b-12d3-a456-426614174014',
       'Isabella', 'Walker',
       'isabella.walker@example.com',
       '789 Willow St, Springfield',
       '084-6789-0123',
       '$2a$10$Y.aVnZ/1oV.G6.yY7.2J9uBCc2UuI7b3o4.B5.O9.p.m6.A7.a8.o',
       '2024-03-29'
    WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '223e4567-e89b-12d3-a456-426614174014');