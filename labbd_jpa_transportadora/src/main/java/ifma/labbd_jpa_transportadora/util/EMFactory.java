package ifma.labbd_jpa_transportadora.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMFactory {
    private static final EntityManagerFactory factory;
    
    static {
        factory = Persistence.createEntityManagerFactory("lab04_jpa");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (factory != null && factory.isOpen()) {
                factory.close();
            }
        }));
    }

    private EMFactory() {} // Construtor privado para evitar instanciação

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}