DROP DATABASE IF EXISTS laskettelukeskus;
CREATE DATABASE laskettelukeskus;
USE laskettelukeskus;


CREATE TABLE laskettelukeskus (
    ID int auto_increment,
    kokonaisAika DOUBLE,
    tulot DOUBLE,
    asiakkaidenMaara DOUBLE,
    poistuneetAsiakkaat DOUBLE,
    lapimenoaikaAVG DOUBLE,
    suoritusTeho DOUBLE,
    PRIMARY KEY (ID)
);

CREATE TABLE asiakas (
    id INT NOT NULL AUTO_INCREMENT,
    rahaaKaytetty DOUBLE,
    vietettyAika DOUBLE,
    palvelupisteidenMaara  INT,
    PRIMARY KEY(ID)
);

CREATE TABLE kassa (
    ID int auto_increment not null,
    tulot DOUBLE,
    palvellutAsiakkaat int,
    aktiiviAika DOUBLE,
    palveluAikaAVG DOUBLE,
    kayttoAste DOUBLE,
    PRIMARY KEY(ID)
);

CREATE TABLE vuokraamo (
    ID int auto_increment not null,
    tulot DOUBLE,
    palvellutAsiakkaat int,
    aktiiviAika DOUBLE,
    palveluAikaAVG DOUBLE,
    kayttoAste DOUBLE,
    PRIMARY KEY(ID)
);

CREATE TABLE kahvila (
    ID int auto_increment not null,
    tulot DOUBLE,
    palvellutAsiakkaat int,
    aktiiviAika DOUBLE,
    palveluAikaAVG DOUBLE,
    kayttoAste DOUBLE,
    PRIMARY KEY(ID)
);

CREATE TABLE rinne1 (
    ID int auto_increment not null,
    tulot DOUBLE,
    palvellutAsiakkaat int,
    aktiiviAika DOUBLE,
    palveluAikaAVG DOUBLE,
    kayttoAste DOUBLE,
    PRIMARY KEY(ID)
);

CREATE TABLE rinne2 (
    ID int auto_increment not null,
    tulot DOUBLE,
    palvellutAsiakkaat int,
    aktiiviAika DOUBLE,
    palveluAikaAVG DOUBLE,
    kayttoAste DOUBLE,
    PRIMARY KEY(ID)
);

CREATE TABLE syotteet (
    ID int auto_increment not null,
    simulaationKesto double,
    saapumisvali double,
    kassaPalveluAjanKA double,
    lipunHinta double,
    vuokraamoPalveluAjanKA double,
    vuokraamoOstostenKA double,
    kahvilaPalveluAjanKA double,
    kahvilaOstostenKA double,
    rinne1PalveluAjanKA double,
    rinne2PalveluAjanKA double,
    PRIMARY KEY(ID)
);