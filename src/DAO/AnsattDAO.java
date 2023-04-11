package DAO;

import entities.Ansatt;
import entities.Avdeling;
import entities.asd;
import jakarta.persistence.*;

import java.lang.Exception;
import java.time.LocalDate;
import java.util.List;


public class AnsattDAO implements Dao {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");


    @Override
    public <T extends asd> void leggTil(T nyEntitet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Ansatt ansatt = (Ansatt)nyEntitet;

            tx.begin();
            Avdeling avd = em.find(Avdeling.class,ansatt.getAvdeling().getId());
            avd.getAnsatte().add(ansatt);
            ansatt.setAvdeling(avd);
            tx.commit();
            tx.begin();
            ansatt = em.find(Ansatt.class,ansatt.getId());
            ansatt.createUserName();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ansatt> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Ansatt> tq = em.createQuery("SELECT a FROM Ansatt a WHERE a.id != 1", Ansatt.class);
            return tq.getResultList();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAnsatte() feilet");
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Ansatt finn(int ansId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Ansatt.class, ansId);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAnsatt() feilet");
            return null;
        } finally {
            em.close();
        }
    }

    public List<Ansatt> finnAnsatteIkkeSjef() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ansatt> tq = em.createQuery("SELECT ansatte FROM Ansatt ansatte WHERE ansatte.id NOT IN (SELECT avdelinger.sjef.id from Avdeling avdelinger )", Ansatt.class);
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
            Ansatt ansatt =(Ansatt)oppdatertEntitet;
            ansatt.createUserName();
            em.merge(ansatt);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void flyttAnsatt(Ansatt ansatt, Avdeling avdeling) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ansatt = em.find(Ansatt.class, ansatt.getId());
            if (ansatt.erSjef())
                throw new Exception("er sjef, kan ikke flyttes");
            avdeling = em.find(Avdeling.class, avdeling.getId());
            ansatt.getAvdeling().getAnsatte().remove(ansatt);
            avdeling.getAnsatte().add(ansatt);
            ansatt.setAvdeling(avdeling);
            tx.commit();
        } catch (Exception e) {

//            e.printStackTrace();
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
            Ansatt ansatt = em.find(Ansatt.class,((Ansatt)entitet).getId());
            em.remove(ansatt);
            tx.commit();
        }
        finally {
            em.close();
        }
    }
}

