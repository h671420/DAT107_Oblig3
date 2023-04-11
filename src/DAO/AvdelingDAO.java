package DAO;

import entities.Ansatt;
import entities.Avdeling;
import entities.asd;
import jakarta.persistence.*;

import java.util.List;


public class AvdelingDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    @Override
    public <T extends asd> void leggTil(T nyEntitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Avdeling ny = (Avdeling)nyEntitet;
            tx.begin();
            Ansatt sjef = em.find(Ansatt.class, ny.getSjef().getId());
            sjef.getAvdeling().getAnsatte().remove(sjef);
            ny.getAnsatte().add(sjef);
            sjef.setAvdeling(ny);
            ny.setSjef(sjef);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Override
    public List<Avdeling> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Avdeling> tq = em.createQuery("SELECT a FROM Avdeling a WHERE a.id != 1", Avdeling.class);
            return tq.getResultList();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdelinger() feilet");
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Avdeling finn(int id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Avdeling.class, id);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdeling() feilet");
            return null;
        } finally {
            em.close();
        }
    }

    public void endreSjef(int avdId, int nySjefId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Avdeling avdeling = em.find(Avdeling.class, avdId);
            Ansatt nySjef = em.find(Ansatt.class, nySjefId);
            nySjef.getAvdeling().getAnsatte().remove(nySjef);
            avdeling.getAnsatte().add(nySjef);
            avdeling.setSjef(nySjef);
            nySjef.setAvdeling(avdeling);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
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
            Avdeling avdeling =(Avdeling) oppdatertEntitet;
            em.merge(avdeling);
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
            Avdeling avdeling = em.find(Avdeling.class, ((Avdeling)entitet).getId());
            Avdeling init = em.find(Avdeling.class, 1);
            List<Ansatt> ansatte = avdeling.getAnsatte();
            for (Ansatt a : ansatte)
                a.setAvdeling(init);
            em.remove(avdeling);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}

