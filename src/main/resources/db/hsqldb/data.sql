-- ADMINS --
INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('admin','admin',TRUE, 1, 0);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');

INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('admin1','admin1',TRUE, 1, 0);
INSERT INTO authorities(id,username,authority) VALUES (2,'admin1','admin');

INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('admin2','admin2',TRUE, 1, 0);
INSERT INTO authorities(id,username,authority) VALUES (3,'admin2','admin');

-- DEFAULT USERS --
INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('flogam1','lolalola',TRUE, 0, 34);
INSERT INTO authorities(id,username,authority) VALUES (4,'flogam1','player');

INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('joiner','lolalola',TRUE, 0, 23);
INSERT INTO authorities(id,username,authority) VALUES (5,'joiner','player');

INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('user','user',TRUE, 0, 0);
INSERT INTO authorities(id,username,authority) VALUES (6,'user','player');

INSERT INTO users(username,password,enabled, role, rolled_dices) VALUES ('user2','user2',TRUE, 0, 100);
INSERT INTO authorities(id,username,authority) VALUES (7,'user2','player');




-- OTHERS --

-- One owner user, named r00tk1d with passwor moinmeister
--INSERT INTO users(username,password,enabled, role) VALUES ('r00tk1d','moinmeister',TRUE, 0);
-- One vet user, named vet1 with passwor v3t
--INSERT INTO users(username,password,enabled, role) VALUES ('vet1','v3t',TRUE, 0);

-- One owner user, named cdsubanko with passwor hola!
--INSERT INTO users(username,password,enabled, role) VALUES ('cdsubanko','hola!',TRUE, 0);

-- One owner user, named iirtrtrt with passwor tkines
--INSERT INTO users(username,password,enabled, role) VALUES ('iirtrtrt','tkines',TRUE, 0);

-- One owner user, named javiervaz01 with passwor javi
--INSERT INTO users(username,password,enabled, role) VALUES ('javiervaz01','javi',TRUE, 0);

-- One owner user, named alecarnun with passwor alec
--INSERT INTO users(username,password,enabled, role) VALUES ('alecarnun','alec',TRUE, 0);

-- One owner user, named pablopben with passwor pablop
--INSERT INTO users(username,password,enabled, role) VALUES ('pablopben','pablop',TRUE, 0);


--INSERT INTO parchis(id,background,height,width) VALUES (1,'resources/images/background.jpeg',800,800);





