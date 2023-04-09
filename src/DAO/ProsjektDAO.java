package DAO;

import tekstgrensesnitt.grensesnittentiteter.ProsjektGrensesnitt;
import jakarta.persistence.*;

import java.util.List;

public class ProsjektDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    public void addProsjekt(ProsjektGrensesnitt prosjektGrensesnitt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();
            em.persist(prosjektGrensesnitt);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public ProsjektGrensesnitt finnProsjekt(int prosjId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(ProsjektGrensesnitt.class, prosjId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public List<ProsjektGrensesnitt> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<ProsjektGrensesnitt> tq = em.createQuery("SELECT p from ProsjektGrensesnitt p", ProsjektGrensesnitt.class);
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
            ProsjektGrensesnitt prosjektGrensesnitt = em.find(ProsjektGrensesnitt.class, proId);
            em.remove(prosjektGrensesnitt);
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
            ProsjektGrensesnitt prosjektGrensesnitt = em.find(ProsjektGrensesnitt.class, proId);
            prosjektGrensesnitt.setProsjektNavn(navn);
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
            ProsjektGrensesnitt prosjektGrensesnitt =em.find(ProsjektGrensesnitt.class,prosjId);
            prosjektGrensesnitt.setBeskrivelse(beskrivelse);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
}
