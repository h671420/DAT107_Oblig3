package DAO;

import entities.Prosjekt;
import jakarta.persistence.*;

import java.util.List;

public class ProsjektDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    public void addProsjekt(Prosjekt prosjekt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();
            em.persist(prosjekt);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Prosjekt finnProsjekt(int prosjId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Prosjekt.class, prosjId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

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

    public void slettProsjekt(int proId) throws RollbackException{
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Prosjekt prosjekt = em.find(Prosjekt.class, proId);
            em.remove(prosjekt);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void endreNavn(int proId, String navn) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Prosjekt prosjekt = em.find(Prosjekt.class, proId);
            prosjekt.setNavn(navn);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void endreBeskrivelse(int prosjId, String beskrivelse) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Prosjekt prosjekt=em.find(Prosjekt.class,prosjId);
            prosjekt.setBeskrivelse(beskrivelse);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
}
