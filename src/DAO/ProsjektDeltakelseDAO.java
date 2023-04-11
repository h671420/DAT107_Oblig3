package DAO;

import entities.*;
import jakarta.persistence.*;

import java.util.List;

public class ProsjektDeltakelseDAO implements Dao {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    @Override
    public <T extends asd> void leggTil(T nyEntitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProsjektDeltakelse ny = (ProsjektDeltakelse) nyEntitet;
            Prosjekt prosjekt=em.find(Prosjekt.class,ny.getProsjekt().getId());
            Ansatt ansatt = em.find(Ansatt.class,ny.getAnsatt().getId());
            prosjekt.getDeltakelser().add(ny);
            ansatt.getProsjekter().add(ny);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public ProsjektDeltakelse finn(int id) {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(ProsjektDeltakelse.class,id);
        }
        finally {
            em.close();
        }
    }

    public List<ProsjektDeltakelse> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<ProsjektDeltakelse> tq = em.createQuery("SELECT p from ProsjektDeltakelse p", ProsjektDeltakelse.class);
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

    }

    @Override
    public <T extends asd> void slett(T entitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProsjektDeltakelse pd = (ProsjektDeltakelse)entitet;
            pd = em.find(ProsjektDeltakelse.class,pd.getId());
            pd.getAnsatt().getProsjekter().remove(pd);
            pd.getProsjekt().getDeltakelser().remove(pd);
            em.remove(pd);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
