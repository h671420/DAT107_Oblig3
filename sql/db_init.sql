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
    navn VARCHAR,
    FOREIGN KEY(sjef_id) REFERENCES ansatt(ans_id)
);
INSERT INTO ansatt(avd_id, user_name) VALUES(1,'init');
INSERT INTO avdeling(sjef_id,navn) VALUES(1,'init');
ALTER TABLE ansatt ADD FOREIGN KEY(avd_id) REFERENCES avdeling(avd_id);

SELECT * FROM  avdeling;