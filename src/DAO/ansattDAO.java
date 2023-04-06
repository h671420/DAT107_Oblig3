package DAO;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.lang.Exception;
import java.time.LocalDate;
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
    public void endreFornavn(int ansId, String fornavn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            ansatt.setFornavn(fornavn);
            ansatt.createUserName();
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreEtternavn(int ansId, String etternavn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            ansatt.setEtternavn(etternavn);
            ansatt.createUserName();
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreStilling(int ansId, String stilling){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            ansatt.setStilling(stilling);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreAnsettelsesDato(int ansId, LocalDate nydato){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            ansatt.setAnsDato(nydato);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreMånedsLønn(int ansId, Integer lønn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            ansatt.setMndLønn(lønn);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public List<Ansatt> finnAnsatte() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Ansatt> tq = em.createQuery("SELECT a FROM Ansatt a WHERE a.ansId != 1", Ansatt.class);
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
            System.out.println(avdNr);
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
    public boolean slettAnsatt(int ansId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            em.remove(ansatt);
            tx.commit();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            em.close();
        }
    }
    public void flyttAnsatt(int ansId, int avdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class,ansId);
            if (ansatt.erSjef())
                throw new Exception("er sjef, kan ikke flyttes");
            Avdeling avdeling = em.find(Avdeling.class,avdId);
            ansatt.getAvdeling().getAnsatte().remove(ansatt);
            avdeling.getAnsatte().add(ansatt);
            ansatt.setAvdeling(avdeling);
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

