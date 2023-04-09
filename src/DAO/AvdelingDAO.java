package DAO;

import entities.Ansatt;
import tekstgrensesnitt.grensesnittentiteter.AnsattGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.AvdelingGrensesnitt;
import jakarta.persistence.*;

import java.util.List;


public class AvdelingDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");


    public AvdelingGrensesnitt finnAvdeling(int avdId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(AvdelingGrensesnitt.class, avdId);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdeling() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public List<AvdelingGrensesnitt> finnAlle() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<AvdelingGrensesnitt> tq = em.createQuery("SELECT a FROM AvdelingGrensesnitt a WHERE a.id != 1", AvdelingGrensesnitt.class);
            return tq.getResultList();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("finnAvdelinger() feilet");
            return null;
        } finally {
            em.close();
        }
    }
    public AvdelingGrensesnitt addAvdeling(AvdelingGrensesnitt ny, int sjefId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AnsattGrensesnitt sjef = em.find(AnsattGrensesnitt.class,sjefId);
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
    public AvdelingGrensesnitt slettAvdeling(int avdId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AvdelingGrensesnitt avdelingGrensesnitt = em.find(AvdelingGrensesnitt.class,avdId);
            AvdelingGrensesnitt init = em.find(AvdelingGrensesnitt.class,1);
            List<AnsattGrensesnitt> ansatte = avdelingGrensesnitt.getAnsatte();
            for (Ansatt a:ansatte)
                a.setAvdeling(init);
            em.remove(avdelingGrensesnitt);
            tx.commit();
            return avdelingGrensesnitt;
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
            AvdelingGrensesnitt avdelingGrensesnitt =em.find(AvdelingGrensesnitt.class,avdId);
            avdelingGrensesnitt.setAvdelingsNavn(navn);
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
            AvdelingGrensesnitt avdelingGrensesnitt =em.find(AvdelingGrensesnitt.class,avdId);
            AnsattGrensesnitt nySjef = em.find(AnsattGrensesnitt.class,nySjefId);
            nySjef.getAvdeling().getAnsatte().remove(nySjef);
            avdelingGrensesnitt.getAnsatte().add(nySjef);
            avdelingGrensesnitt.setSjef(nySjef);
            nySjef.setAvdeling(avdelingGrensesnitt);
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

