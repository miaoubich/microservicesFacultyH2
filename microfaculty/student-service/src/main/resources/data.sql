CREATE TABLE STUDENTS(
  student_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(64),
  last_name VARCHAR(64),
  email VARCHAR(64),
  FK_ADDRESS_ID int
);

INSERT INTO STUDENTS
(FK_ADDRESS_ID, EMAIL, FIRST_NAME, LAST_NAME) VALUES
(2, 'ali@bouzar.org', 'Dave', 'heaven'),
(4, 'billy@bouzar.org', 'KUNA', 'NEAN'),
(1, 'darin@bouzar.org', 'Boshish', 'douda'),
(3, 'lina@bouzar.org', 'Gonina', 'baby');
