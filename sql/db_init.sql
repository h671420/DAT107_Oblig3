DROP SCHEMA IF EXISTS Oblig_3 CASCADE;
CREATE SCHEMA Oblig_3;
SET SEARCH_PATH TO Oblig_3;

CREATE TABLE ansatt
(
    ans_id SERIAL PRIMARY KEY ,
    user_name VARCHAR(5) UNIQUE,
    fornavn VARCHAR,
    etternavn VARCHAR,
    ans_dato DATE,
    stilling VARCHAR,
    mnd_lonn INT,
    avd_id INT NOT NULL
);
CREATE TABLE avdeling
(
    avd_id SERIAL PRIMARY KEY ,
    sjef_id INT NOT NULL,
    navn VARCHAR,
    FOREIGN KEY(sjef_id) REFERENCES ansatt(ans_id)
);
CREATE TABLE prosjekt
(
    prosj_id SERIAL PRIMARY KEY,
    prosj_navn VARCHAR,
    prosj_beskr VARCHAR
);
CREATE TABLE prosjektdeltakelse
(
    prosjekt_deltakelse_id SERIAL PRIMARY KEY,
    ans_id INT,
    prosj_id INT,
    rolle VARCHAR,
    ant_timer INT,
    FOREIGN KEY(ans_id) REFERENCES ansatt(ans_id),
    FOREIGN KEY(prosj_id) REFERENCES prosjekt(prosj_id),
    UNIQUE(prosj_id,ans_id)
);
INSERT INTO ansatt(avd_id, user_name, fornavn, etternavn) VALUES(1,'init','dummy','ansatt');
INSERT INTO avdeling(sjef_id,navn) VALUES(1,'Nyansatte');
ALTER TABLE ansatt ADD FOREIGN KEY(avd_id) REFERENCES avdeling(avd_id);

SELECT * FROM  avdeling;