package DAO;

import tekstgrensesnitt.grensesnittentiteter.AnsattGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.ProsjektGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.ProsjektDeltakelseGrensesnitt;
import jakarta.persistence.*;

import java.util.List;

public class ProsjektDeltakelseDAO implements Dao{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    public ProsjektDeltakelseGrensesnitt finnProsjektDeltakelse(int deltId){
        EntityManager em = emf.createEntityManager();

        try{
            return em.find(ProsjektDeltakelseGrensesnitt.class, deltId);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }

    public List<ProsjektDeltakelseGrensesnitt> finnAlle(){
            EntityManager em = emf.createEntityManager();

            try{
                TypedQuery<ProsjektDeltakelseGrensesnitt> tq = em.createQuery("SELECT p from ProsjektDeltakelseGrensesnitt p", ProsjektDeltakelseGrensesnitt.class);
                return tq.getResultList();
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
            finally {
                em.close();
            }
        }
        public void addProsjektDeltakelse(int ansId, int prosjId, String rolle, int timer){
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try{
                tx.begin();
                ProsjektGrensesnitt prosjektGrensesnitt =em.find(ProsjektGrensesnitt.class,prosjId);
                AnsattGrensesnitt ansattGrensesnitt = em.find(AnsattGrensesnitt.class,ansId);
//                ProsjektDeltakelseGrensesnitt ny = new ProsjektDeltakelseGrensesnitt(ansattGrensesnitt, prosjektGrensesnitt,rolle,timer);
                ProsjektDeltakelseGrensesnitt ny = new ProsjektDeltakelseGrensesnitt();
                ny.setAnsatt(ansattGrensesnitt);
                ny.setProsjekt(prosjektGrensesnitt);
                ny.setRolle(rolle);
                ny.setTimer(timer);
                ansattGrensesnitt.getProsjektDeltakelser().add(ny);
                prosjektGrensesnitt.getProsjektDeltakelser().add(ny);
                tx.commit();

            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                em.close();
            }
        }

    public void slett(Integer prosjektDeltakelseId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            ProsjektDeltakelseGrensesnitt pd = em.find(ProsjektDeltakelseGrensesnitt.class,prosjektDeltakelseId);
            pd.getAnsatt().getProsjektDeltakelser().remove(pd);
            em.remove(pd);
            tx.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public void endreRolle(Integer prosjektDeltakelseId, String nyRolle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            ProsjektDeltakelseGrensesnitt pd = em.find(ProsjektDeltakelseGrensesnitt.class,prosjektDeltakelseId);
            pd.setRolle(nyRolle);
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public void endreTimer(Integer prosjektDeltakelseId, Integer nyTimer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            ProsjektDeltakelseGrensesnitt pd = em.find(ProsjektDeltakelseGrensesnitt.class,prosjektDeltakelseId);
            pd.setTimer(nyTimer);
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
