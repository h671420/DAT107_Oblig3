package DAO;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.lang.Exception;
import java.util.List;


public class Dao {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");


    public Avdeling finnAvdeling(int avdId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Avdeling.class, avdId);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdeling() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public List<Avdeling> finnAvdelinger() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Avdeling> tq = em.createQuery("SELECT p FROM Avdeling p", Avdeling.class);
            return tq.getResultList();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdelinger() feilet");
            return null;
        } finally {
            em.close();
        }
    }

    public Ansatt finnAnsatt(int ansId) {
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
    public List<Ansatt> finnAnsatte() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Ansatt> tq = em.createQuery("SELECT a FROM Ansatt a", Ansatt.class);
            return tq.getResultList();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAnsatte() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public void addAnsatt(Ansatt ansatt, int avdNr){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Avdeling avd = em.find(Avdeling.class,avdNr);
            avd.addAnsatt(ansatt);
            ansatt.setAvdeling(avd);
            tx.commit();
        }
        catch (Exception e){
//            e.printStackTrace();
            System.out.println("addAnsatt() feilet");
        }
        finally {
            em.close();
        }
    }
    public void addAvdeling(Avdeling avdeling, int sjefId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt sjef = em.find(Ansatt.class,sjefId);
            if (sjef.erSjef())
                throw new Exception();
            avdeling.setSjef(sjef);
            avdeling.addAnsatt(sjef);
            sjef.setAvdeling(avdeling);
            tx.commit();
        }
        catch (Exception e){
            System.out.println("addAvdeling() feilet");
//            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void slettAnsatt(int ansattId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansattId);
            em.remove(ansatt);
            tx.commit();
        }
        catch (Exception e){
            System.out.println("slettAnsatt() feilet");
//            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void slettAvdeling(int avdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Avdeling avdeling = em.find(Avdeling.class,avdId);
            em.remove(avdeling);
            tx.commit();
        }
        catch (Exception e){
            System.out.println("slettAvdeling() feilet");
//            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
}

