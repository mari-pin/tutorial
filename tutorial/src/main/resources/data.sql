
INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');




INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');



INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);


INSERT INTO client(name) VALUES ('Ana');
INSERT INTO client(name) VALUES ('Amparo');
INSERT INTO client(name) VALUES ('Fede');


INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (1, 1, '2025-04-07', '2025-04-09');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (2, 2, '2025-04-07', '2025-04-11');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (2, 1, '2025-04-08', '2025-04-12');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (3, 3, '2025-04-07', '2025-04-09');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (2, 1, '2025-04-09', '2025-04-15');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (4, 3, '2025-04-10', '2025-04-16');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (3, 1, '2025-04-12', '2025-04-16');
INSERT INTO prestamo(game_id, client_id, initdate, enddate) VALUES (5, 2, '2025-04-08', '2025-04-19');