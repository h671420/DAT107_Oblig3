package DAO;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.lang.Exception;
import java.util.List;


public class ansattDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");





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
    public Ansatt addAnsatt(Ansatt ansatt, int avdNr){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Avdeling avd = em.find(Avdeling.class,avdNr);
            avd.addAnsatt(ansatt);
            ansatt.setAvdeling(avd);
            tx.commit();
            tx.begin();
            ansatt.createUserName();
            tx.commit();
            return ansatt;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("addAnsatt() feilet");
            return null;
        }
        finally {
            em.close();
        }
    }
    public Ansatt slettAnsatt(int ansattId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansattId);
            em.remove(ansatt);
            tx.commit();
            return ansatt;
        }
        catch (Exception e){
            return null;
//            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void flyttAnsatt(int ansattId, int nyAvdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansattId);
            if (ansatt.erSjef())
                throw new Exception();
            Avdeling nyAvd = em.find(Avdeling.class,nyAvdId);
            ansatt.getAvdeling().getAnsatte().remove(ansatt);
            nyAvd.getAnsatte().add(ansatt);
            ansatt.setAvdeling(nyAvd);
            tx.commit();
        }
        catch (Exception e){

//            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
}

