package DAO;

import entities.Ansatt;
import entities.Prosjekt;
import entities.asd;
import jakarta.persistence.*;

import java.util.List;

public class ProsjektDAO implements Dao {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    @Override
    public <T extends asd> void leggTil(T nyEntitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist((Prosjekt)nyEntitet);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Prosjekt finn(int id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Prosjekt.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Prosjekt> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Prosjekt> tq = em.createQuery("SELECT p from Prosjekt p", Prosjekt.class);
            return tq.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }


    @Override
    public <T extends asd> void oppdater(T oppdatertEntitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Prosjekt prosjekt =(Prosjekt) oppdatertEntitet;
            em.merge(prosjekt);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public <T extends asd> void slett(T entitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Prosjekt prosjekt = em.find(Prosjekt.class, ((Prosjekt)entitet).getId());
            em.remove(prosjekt);
            tx.commit();
        } finally {
            em.close();
        }
    }
}
