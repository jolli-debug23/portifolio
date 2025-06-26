package ifma.labbd_jpa_transportadora.util;

import jakarta.persistence.*;
import java.util.List;

public class DAOGenerico<T> {
    private final Class<T> classe;

    public DAOGenerico(Class<T> classe) {
        this.classe = classe;
    }

    public void salvar(T entidade) {
        EntityManager em = EMFactory.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entidade);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PersistenceException("Erro ao salvar entidade", e);
        } finally {
            em.close();
        }
    }

    public T buscarPorId(Long id) {
        EntityManager em = EMFactory.getEntityManager();
        try {
            return em.find(classe, id);
        } finally {
            em.close();
        }
    }
    
    public List<T> buscarTodos() {
        EntityManager em = EMFactory.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + classe.getSimpleName() + " e";
            if (hasField("nome")) {
                jpql += " ORDER BY e.nome";
            }
            return em.createQuery(jpql, classe).getResultList();
        } finally {
            em.close();
        }
    }

    public void atualizar(T entidade) {
        EntityManager em = EMFactory.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(entidade);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PersistenceException("Erro ao atualizar entidade", e);
        } finally {
            em.close();
        }
    }

    public void remover(T entidade) {
        EntityManager em = EMFactory.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            entidade = em.merge(entidade); 
            em.remove(entidade);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PersistenceException("Erro ao remover entidade", e);
        } finally {
            em.close();
        }
    }

    private boolean hasField(String fieldName) {
        try {
            classe.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}