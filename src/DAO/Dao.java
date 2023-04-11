package DAO;

import entities.asd;

import java.util.List;

public interface Dao {
    //Create
    <T extends asd> void leggTil(T nyEntitet);

    //Read
    <T extends asd> List<T> finnAlle();

    <T extends asd> T finn(int id);

    //Update
    <T extends asd> void oppdater(T oppdatertEntitet);

    //Delete
    <T extends asd> void slett(T entitet);
}
