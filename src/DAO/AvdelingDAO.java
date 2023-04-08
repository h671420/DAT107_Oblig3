package DAO;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.util.List;


public class AvdelingDAO implements Dao{
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
    public Avdeling addAvdeling(Avdeling ny, int sjefId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt sjef = em.find(Ansatt.class,sjefId);
            sjef.getAvdeling().getAnsatte().remove(sjef);
            ny.getAnsatte().add(sjef);
            sjef.setAvdeling(ny);
            ny.setSjef(sjef);
            tx.commit();

            return ny;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }
    public Avdeling slettAvdeling(int avdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Avdeling avdeling = em.find(Avdeling.class,avdId);
            Avdeling init = em.find(Avdeling.class,1);
            List<Ansatt> ansatte = avdeling.getAnsatte();
            for (Ansatt a:ansatte)
                a.setAvdeling(init);
            em.remove(avdeling);
            tx.commit();
            return avdeling;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }
    public void endreNavn(int avdId, String navn){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Avdeling avdeling=em.find(Avdeling.class,avdId);
            avdeling.setNavn(navn);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
    public void endreSjef(int avdId, int nySjefId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Avdeling avdeling=em.find(Avdeling.class,avdId);
            Ansatt nySjef = em.find(Ansatt.class,nySjefId);
            nySjef.getAvdeling().getAnsatte().remove(nySjef);
            avdeling.getAnsatte().add(nySjef);
            avdeling.setSjef(nySjef);
            nySjef.setAvdeling(avdeling);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
}

