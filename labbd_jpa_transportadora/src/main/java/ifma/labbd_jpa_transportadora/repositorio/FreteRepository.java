package ifma.labbd_jpa_transportadora.repositorio;

import ifma.labbd_jpa_transportadora.modelo.*;
import ifma.labbd_jpa_transportadora.util.DAOGenerico;
import ifma.labbd_jpa_transportadora.util.EMFactory;
import jakarta.persistence.*;
import java.util.List;

public class FreteRepository extends DAOGenerico<Frete> {
    
    public FreteRepository() {
        super(Frete.class);
    }

    public List<Frete> buscarFretesPorCliente(Cliente cliente) {
        EntityManager em = EMFactory.getEntityManager();
        try {
            return em.createQuery(
                "SELECT f FROM Frete f WHERE f.cliente = :cliente ORDER BY f.id DESC", 
                Frete.class)
                .setParameter("cliente", cliente)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Frete> buscarFretesPorCidade(Cidade cidade) {
        EntityManager em = EMFactory.getEntityManager();
        try {
            return em.createQuery(
                "SELECT f FROM Frete f WHERE f.cidadeOrigem = :cidade OR f.cidadeDestino = :cidade",
                Frete.class)
                .setParameter("cidade", cidade)
                .getResultList();
        } finally {
            em.close();
        }
    }
}