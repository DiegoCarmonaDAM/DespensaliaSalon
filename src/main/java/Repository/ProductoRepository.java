package Repository;

import Entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProductoRepository {
    private EntityManager em;

    public ProductoRepository(EntityManager em) {
        this.em = em;
    }

    public Producto findById(int id) {
        return em.find(Producto.class, id);
    }

    public List<Producto> findAll() {
        TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p", Producto.class);
        return query.getResultList();
    }

    public void save(Producto producto) {
        em.getTransaction().begin();
        em.persist(producto);
        em.getTransaction().commit();
    }

    public void delete(Producto producto) {
        em.getTransaction().begin();
        em.remove(producto);
        em.getTransaction().commit();
    }
}