-- ADMINS --
INSERT INTO users(username,password,enabled) VALUES ('admin','admin',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');

INSERT INTO users(username,password,enabled) VALUES ('admin1','admin1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'admin1','admin2');

INSERT INTO users(username,password,enabled) VALUES ('admin2','admin2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'admin2','admin3');

-- DEFAULT USERS --
INSERT INTO users(username,password,enabled) VALUES ('flogam1','lolalola',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'flogam1','user');

INSERT INTO users(username,password,enabled) VALUES ('joiner','lolalola',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'joiner','user');

INSERT INTO users(username,password,enabled) VALUES ('user','user',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'user','user');

INSERT INTO users(username,password,enabled) VALUES ('user2','user2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'user2','user');




-- OTHERS --

-- One owner user, named r00tk1d with passwor moinmeister
INSERT INTO users(username,password,enabled) VALUES ('r00tk1d','moinmeister',TRUE);
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);

-- One owner user, named cdsubanko with passwor hola!
INSERT INTO users(username,password,enabled) VALUES ('cdsubanko','hola!',TRUE);

-- One owner user, named iirtrtrt with passwor tkines
INSERT INTO users(username,password,enabled) VALUES ('iirtrtrt','tkines',TRUE);

-- One owner user, named javiervaz01 with passwor javi
INSERT INTO users(username,password,enabled) VALUES ('javiervaz01','javi',TRUE);

-- One owner user, named alecarnun with passwor alec
INSERT INTO users(username,password,enabled) VALUES ('alecarnun','alec',TRUE);

-- One owner user, named pablopben with passwor pablop
INSERT INTO users(username,password,enabled) VALUES ('pablopben','pablop',TRUE);


--INSERT INTO parchis(id,background,height,width) VALUES (1,'resources/images/background.jpeg',800,800);





