CREATE TABLE if not exists cities(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name text);
CREATE TABLE if not exists users (
                                     userId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                                     name varchar(255),
    phone INTEGER,
    cityId INTEGER,
    FOREIGN KEY (cityId) REFERENCES cities(id) ON DELETE SET NULL
    );

SELECT * FROM users;
SELECT * FROM cities;

-- getAllUsersStatement
SELECT users.name as name, users.phone as phone, cities.name as city
FROM users
         LEFT JOIN cities ON cities.id = users.cityId;

-- getCityIdByName
SELECT id FROM cities WHERE cities.name = ?;

DELETE FROM users;
DELETE FROM cities;

DROP TABLE users;
DROP TABLE cities;

INSERT INTO cities (name) VALUES ('Saint Petersburg');
INSERT INTO cities (name) VALUES ('Moscow');

INSERT INTO users (name, phone, cityId) VALUES ('Vasya', 762042, 1);
INSERT INTO users (name, phone, cityId) VALUES ('Jack', 893843, null);
INSERT INTO users (name, phone, cityId) VALUES ('Boris', 777293, 1);
INSERT INTO users (name, phone, cityId) VALUES ('Dima', 111111, 2);
INSERT INTO users (name, phone, cityId) VALUES ('Frank', 213501, null);
INSERT INTO users (name, phone, cityId) VALUES ('James', 378300, 1);
