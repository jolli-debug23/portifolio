package ifma.labbd_jpa_transportadora.repositorio;

import ifma.labbd_jpa_transportadora.modelo.Cliente;
import ifma.labbd_jpa_transportadora.util.DAOGenerico;

public class ClienteRepository extends DAOGenerico<Cliente> {
    public ClienteRepository() {
        super(Cliente.class);
    }
}

