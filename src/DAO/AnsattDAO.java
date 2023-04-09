package DAO;


import tekstgrensesnitt.grensesnittentiteter.AnsattGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.AvdelingGrensesnitt;
import jakarta.persistence.*;

import java.lang.Exception;
import java.time.LocalDate;
import java.util.List;


public class AnsattDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");





    public AnsattGrensesnitt finnAnsatt(int ansId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(AnsattGrensesnitt.class, ansId);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAnsatt() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public List<AnsattGrensesnitt> finnAnsatteIkkeSjef(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<AnsattGrensesnitt> tq = em.createQuery("SELECT ansatte FROM AnsattGrensesnitt ansatte WHERE ansatte.id NOT IN (SELECT avdelinger.sjef.id from AvdelingGrensesnitt avdelinger )", AnsattGrensesnitt.class);
            return tq.getResultList();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }
    public void endreFornavn(int ansId, String fornavn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            ansattGrensesnitt.setFornavn(fornavn);
            ansattGrensesnitt.lagBrukerNavn();
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
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            ansattGrensesnitt.setEtternavn(etternavn);
            ansattGrensesnitt.lagBrukerNavn();
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
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            ansattGrensesnitt.setStilling(stilling);
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
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            ansattGrensesnitt.setAnsettelsesDato(nydato);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreMaanedsLonn(int ansId, Integer lønn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            ansattGrensesnitt.setMaanedsLonn(lønn);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public List<AnsattGrensesnitt> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<AnsattGrensesnitt> tq = em.createQuery("SELECT a FROM AnsattGrensesnitt a WHERE a.ansId != 1", AnsattGrensesnitt.class);
            new AnsattGrensesnitt();
            return tq.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("finnAnsatte() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public AnsattGrensesnitt addAnsatt(AnsattGrensesnitt ansattGrensesnitt, int avdNr){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AvdelingGrensesnitt avd = em.find(AvdelingGrensesnitt.class,avdNr);
            System.out.println(avdNr);
            avd.addAnsatt(ansattGrensesnitt);
            ansattGrensesnitt.setAvdeling(avd);
            tx.commit();
            tx.begin();
            ansattGrensesnitt.lagBrukerNavn();
            tx.commit();
            return ansattGrensesnitt;
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
    public boolean slettAnsatt(int ansId) throws RollbackException{
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            em.remove(ansattGrensesnitt);
            tx.commit();
            return true;
        }
//        catch (Exception e){
//            System.out.println(e.getCause().getCause().getClass()) ;
//            return false;
//        }
        finally {
            em.close();
        }
    }
    public void flyttAnsatt(int ansId, int avdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
            if (ansattGrensesnitt.erSjef())
                throw new Exception("er sjef, kan ikke flyttes");
            AvdelingGrensesnitt avdelingGrensesnitt = em.find(AvdelingGrensesnitt.class,avdId);
            ansattGrensesnitt.getAvdeling().getAnsatte().remove(ansattGrensesnitt);
            avdelingGrensesnitt.getAnsatte().add(ansattGrensesnitt);
            ansattGrensesnitt.setAvdeling(avdelingGrensesnitt);
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

