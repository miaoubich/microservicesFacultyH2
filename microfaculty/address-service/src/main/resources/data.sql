CREATE TABLE ADDRESSES(
  address_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  city VARCHAR(64),
  street VARCHAR(112)
);

INSERT INTO ADDRESSES
(CITY, STREET) VALUES
('OUED FODDA', 'rue abbache mohamed'),
('Split', 'Ulica franko'),
('Zagreb', 'Ulica bisha'),
('Sibanik', 'MEAN');
