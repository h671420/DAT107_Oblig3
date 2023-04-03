package DAO;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.util.List;


public class avdelingDAO {
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
    public Avdeling addAvdeling(Avdeling ny, int sjefId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Ansatt sjef = em.find(Ansatt.class,sjefId);
            if (sjef.erSjef())
                throw new Exception();
            sjef.getAvdeling().getAnsatte().remove(sjef);
            ny.getAnsatte().add(sjef);
            sjef.setAvdeling(ny);
            ny.setSjef(sjef);
            tx.commit();

            return ny;
        }
        catch (Exception e){
            return null;
//            e.printStackTrace();
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
}

