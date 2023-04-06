package DAO;

import entities.Ansatt;
import entities.Prosjekt;
import entities.ProsjektDeltakelse;
import jakarta.persistence.*;

import java.util.List;

public class prosjektDeltakelseDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oblig_3");

    public ProsjektDeltakelse finnProsjektDeltakelse(int deltId){
        EntityManager em = emf.createEntityManager();

        try{
            return em.find(ProsjektDeltakelse.class, deltId);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }

    public List<ProsjektDeltakelse> finnProsjektDeltakelser(){
            EntityManager em = emf.createEntityManager();

            try{
                TypedQuery<ProsjektDeltakelse> tq = em.createQuery("SELECT p from ProsjektDeltakelse p", ProsjektDeltakelse.class);
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
                Prosjekt prosjekt=em.find(Prosjekt.class,prosjId);
                Ansatt ansatt = em.find(Ansatt.class,ansId);
                ProsjektDeltakelse ny = new ProsjektDeltakelse(ansatt,prosjekt,rolle,timer);
                ansatt.getProsjekter().add(ny);
                prosjekt.getDeltakelser().add(ny);
                tx.commit();

            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                em.close();
            }
        }
}
