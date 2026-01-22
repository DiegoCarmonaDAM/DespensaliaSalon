package Repository;

import Entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteRepository {
    private EntityManager em;

    public ClienteRepository(EntityManager em) {
        this.em = em;
    }

    public Cliente findById(String id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> findAll() {
        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
        return query.getResultList();
    }

    public void save(Cliente cliente) {
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
    }

    public void delete(Cliente cliente) {
        em.getTransaction().begin();
        em.remove(cliente);
        em.getTransaction().commit();
    }
}