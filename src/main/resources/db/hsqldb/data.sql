-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');

-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
-- One owner user, named flogam1 with passwor lolalola
INSERT INTO users(username,password,enabled) VALUES ('flogam1','lolalola',TRUE);
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





