package ifma.labbd_jpa_transportadora.repositorio;

import ifma.labbd_jpa_transportadora.modelo.Cidade;
import ifma.labbd_jpa_transportadora.util.DAOGenerico;

public class CidadeRepository extends DAOGenerico<Cidade> {
    public CidadeRepository() {
        super(Cidade.class);
    }
}
