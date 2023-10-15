CREATE TABLE city
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    UNIQUE (name)
);


INSERT INTO city (name)
VALUES ('Moscow'),
       ('Saint Petersburg'),
       ('Novosibirsk');

CREATE TABLE weather_type
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    UNIQUE (type)
);


INSERT INTO weather_type (type)
VALUES ('Sunny'),
       ('Rainy'),
       ('Snowy');


CREATE TABLE city_weather
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    city_id         BIGINT           NOT NULL,
    weather_type_id BIGINT           NOT NULL,
    temperature     FLOAT NOT NULL,
    date_time       TIMESTAMP     NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city (id) ON DELETE CASCADE,
    FOREIGN KEY (weather_type_id) REFERENCES weather_type (id)
);


INSERT INTO city_weather (city_id, weather_type_id, temperature, date_time)
VALUES (1, 1, 20.5, '2023-10-14 12:00:00'),
       (2, 2, 15.3, '2023-10-14 12:00:00'),
       (1, 2, 15.3, '2023-10-14 12:00:00'),
       (3, 3, -5.0, '2023-10-14 12:00:00');