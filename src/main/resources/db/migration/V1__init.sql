CREATE TABLE ELEMENT
(
  ID int PRIMARY KEY,
  NAME varchar(255) NOT NULL
);

CREATE TABLE EFFECT
(
  ID int PRIMARY KEY auto_increment,
  NAME varchar(255) NOT NULL,
  ELEMENT_ID int,
  foreign key (ELEMENT_ID) references ELEMENT (ID)
);

CREATE TABLE RUNE
(
  ID int PRIMARY KEY auto_increment,
  NAME varchar(255) NOT NULL,
  LATIN_NAME varchar(255) NOT NULL,
  EFFECT_ID int,
  EFFECT_VAL int,
  foreign key (EFFECT_ID) references EFFECT (ID)
);

INSERT INTO ELEMENT (ID, NAME) values (1, 'Water');
INSERT INTO ELEMENT (ID, NAME) values (2, 'Fire');
INSERT INTO ELEMENT (ID, NAME) values (3, 'Metal');
