package wrk.db.jpadao;

import exceptions.MyDBException;
import helpers.SystemLib;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * Implémentation de la couche DAO d'après l'API JpaDaoAPI. Equivalent à un
 * Worker JPA générique pour gérer n'importe quel entity-bean.
 *
 * @author Jean-Claude Stritt et Pierre-Alain Mettraux
 * @param <E>
 * @param <PK>
 */
public class JpaDao<E, PK> {

    private final Class<E> cl;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction et;

    /**
     * Constructeur.
     *
     * @param pu
     * @param cl
     * @throws MyDBException
     */
    public JpaDao(String pu, Class<E> cl) throws MyDBException {
        this.cl = cl;
        try {
            emf = Persistence.createEntityManagerFactory(pu);
            em = emf.createEntityManager();
            et = em.getTransaction();
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    /**
     * Ajoute un objet.
     *
     * @param e l'objet à persister dans la BD
     * @throws MyDBException
     */
    public void creer(E e) throws MyDBException {
        try {
            et.begin();
            em.persist(e);
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    /**
     * Lit un objet d'après sa PK.
     *
     * @param pk l'identifiant de l'objet à lire
     * @return l'objet lu
     * @throws MyDBException
     */
    public E lire(PK pk) throws MyDBException {
        E e = null;
        try {
            e = em.find(cl, pk);
            if (e != null) {
                em.refresh(e);
            }
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return e;
    }

    /**
     * Modifie un objet dans la BD.
     *
     * @param e l'objet à modifier
     * @throws MyDBException
     */
    public void modifier(E e) throws MyDBException {
        try {
            et.begin();
            em.merge(e);
            et.commit();
        } catch (OptimisticLockException ex) {
            et.rollback();
            throw new MyDBException(SystemLib.getFullMethodName(), "OptimisticLockException: " + ex.getMessage());
        } catch (RollbackException ex) {
            et.rollback();
            throw new MyDBException(SystemLib.getFullMethodName(), "RollbackException: " + ex.getMessage());
        } catch (Exception ex) {
            et.rollback();
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    /**
     * Efface un objet d'après son identifiant (PK).
     *
     * @param pk l'identifiant de l'objet à lire
     * @throws MyDBException
     */
    public void effacer(PK pk) throws MyDBException {
        E e = lire(pk);
        if (e != null) {
            try {
                et.begin();
                em.remove(e);
                et.commit();
            } catch (OptimisticLockException ex) {
                et.rollback();
                throw new MyDBException(SystemLib.getFullMethodName(), "OptimisticLockException: " + ex.getMessage());
            } catch (Exception ex) {
                et.rollback();
                throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
            }
        }
    }

    /**
     * Retourne le nombre d'objets actuellement dans une table de la DB.
     *
     * @return le nombre d'objets
     * @throws MyDBException
     */
    public long compter() throws MyDBException {
        long nb = 0;
        try {
            String jpql = "SELECT count(e) FROM " + cl.getSimpleName() + " e";
            Query query = em.createQuery(jpql);
            nb = (Long) query.getSingleResult();
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return nb;
    }

    /**
     * Rechercher un objet d'après la valeur d'une propriété spécifiée.
     *
     * @param prop la propriété sur laquelle faire la recherche
     * @param valeur la valeur de cette propriété
     * @return l'objet recherché ou null
     * @throws MyDBException
     */
    @SuppressWarnings("unchecked")
    public E rechercher(String prop, Object valeur) throws MyDBException {
        E e = null;
        try {
            String jpql = "SELECT e FROM " + cl.getSimpleName() + " e WHERE e." + prop + "=?1";
            Query query = em.createQuery(jpql);
            query.setParameter(1, valeur);
            e = (E) query.getSingleResult();
        } catch (NoResultException ex) {
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return e;
    }

    /**
     * Récupère une liste avec des éléments filtrés
     *
     * @return une liste d'objets.
     * @throws MyDBException
     */
    public List<E> filtrer(String prop, Object valeur) throws MyDBException {
        List<E> liste = new ArrayList<>();
        try {
            String jpql = "SELECT e FROM " + cl.getSimpleName() + " e WHERE e." + prop + " = ?1";
            Query query = em.createQuery(jpql);
            query.setParameter(1, valeur);
            liste = query.getResultList();
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return liste;

    }

    /**
     * Récupère une liste avec tous les objets de la table.
     *
     * @return une liste d'objets.
     * @throws MyDBException
     */
    @SuppressWarnings("unchecked")
    public List<E> lireListe() throws MyDBException {
        List<E> liste = new ArrayList<>();
        try {
            String jpql = "SELECT e FROM " + cl.getSimpleName() + " e";
            Query query = em.createQuery(jpql);
            liste = query.getResultList();
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return liste;
    }

    @SuppressWarnings("unchecked")
    public List<E> lireListe(String order, boolean isASC) throws MyDBException {
        String filter = isASC ? "ASC" : "DESC";
        List<E> liste = new ArrayList<>();
        if (!order.isEmpty()) {
            try {
                String jpql = "SELECT e FROM " + cl.getSimpleName() + " e ORDER BY e." + order.toLowerCase() + " " + filter;
                Query query = em.createQuery(jpql);
                liste = query.getResultList();
            } catch (Exception ex) {
                throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
            }
        }

        return liste;
    }

    /**
     * Efface complètement tout le contenu d'une entité en une seule
     * transaction.
     *
     * @return le nombre d'objets effacés
     * @throws MyDBException
     */
    public int effacerListe() throws MyDBException {
        int nb;
        try {
            String jpql = "DELETE FROM " + cl.getSimpleName() + " e";
            Query q = em.createQuery(jpql);
            nb = q.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return nb;
    }

    /**
     * Sauve une liste globale dans une seule transaction.
     *
     * @param list
     * @return TRUE si l'opération a pu se dérouler correctement
     * @throws MyDBException
     */
    public int sauverListe(List<E> list) throws MyDBException {
        int nb = 0;
        if (list != null && !list.isEmpty()) {
            try {
                for (E e : list) {
                    em.persist(e);
                    nb++;
                }
                et.commit();

            } catch (Exception ex) {
                et.rollback();
                throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
            }
        }
        return nb;
    }

    /**
     * Déconnexion
     *
     * @throws MyDBException
     */
    public void deconnecter() throws MyDBException {
        if (estConnectee()){
            em.close();
            emf.close();
            em = null;
        }
    }

    public boolean estConnectee() {
        return (em != null) && em.isOpen();
    }

}
