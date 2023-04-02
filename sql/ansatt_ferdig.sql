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
    --prosjekter
    avd_id INT NOT NULL
);
CREATE TABLE avdeling
(
    avd_id SERIAL PRIMARY KEY ,
    sjef_id INT NOT NULL,
    FOREIGN KEY(sjef_id) REFERENCES ansatt(ans_id)
);
INSERT INTO ansatt(avd_id) VALUES(1);
INSERT INTO avdeling(sjef_id) VALUES(1);
ALTER TABLE ansatt ADD FOREIGN KEY(avd_id) REFERENCES avdeling(avd_id);

SELECT * FROM  avdeling;