DELETE FROM Users;
DROP TABLE Cities;

SELECT * FROM Users;
SELECT * FROM Cities;

-- getAllUsersStatement
SELECT users.name as name, users.phone as phone, cities.name as city
FROM users
         LEFT JOIN cities ON cities.id = users.city;

-- getCityIdByName
SELECT id FROM cities WHERE cities.name = ?;


INSERT INTO cities (name) VALUES ('Saint Petersburg');
INSERT INTO cities (name) VALUES ('Moscow');

INSERT INTO Users (name, phone, city) VALUES ('Vasya', 762042, 1);
INSERT INTO Users (name, phone, city) VALUES ('Jack', 893843, null);
INSERT INTO Users (name, phone, city) VALUES ('Boris', 777293, 1);
INSERT INTO Users (name, phone, city) VALUES ('Dima', 111111, 2);
INSERT INTO Users (name, phone, city) VALUES ('Frank', 213501, null);
INSERT INTO Users (name, phone, city) VALUES ('James', 378300, 1);


DROP TABLE Users;
DROP TABLE Cities;