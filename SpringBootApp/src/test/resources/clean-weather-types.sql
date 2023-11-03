DELETE FROM city_weather WHERE weather_type_id IN (SELECT id FROM weather_type);

DELETE FROM weather_type;