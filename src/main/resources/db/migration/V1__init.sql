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
  POINT_ORDER varchar(16) NOT NULL,
  EFFECT_ID int,
  EFFECT_VAL int,
  foreign key (EFFECT_ID) references EFFECT (ID)
);

INSERT INTO ELEMENT (ID, NAME) values (1, 'Water');
INSERT INTO ELEMENT (ID, NAME) values (2, 'Fire');
INSERT INTO ELEMENT (ID, NAME) values (3, 'Metal');
INSERT INTO ELEMENT (ID, NAME) values (4, 'Wood');
INSERT INTO ELEMENT (ID, NAME) values (5, 'Light');
INSERT INTO ELEMENT (ID, NAME) values (6, 'Darkness');


CREATE TABLE RESOURCE_TYPE
(
  ID int PRIMARY KEY,
  NAME varchar(255) NOT NULL
);

CREATE TABLE RESOURCE_POINT
(
  ID int PRIMARY KEY auto_increment,
  LAT double NOT NULL,
  LON double NOT NULL,
  RESOURCE_TYPE_ID int NOT NULL,
  foreign key (RESOURCE_TYPE_ID) references RESOURCE_TYPE (ID)
);

INSERT INTO RESOURCE_TYPE (ID, NAME) values (1, 'Magic');
INSERT INTO RESOURCE_TYPE (ID, NAME) values (2, 'Tech');
