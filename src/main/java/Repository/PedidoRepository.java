package Repository;

import Entity.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class PedidoRepository {
    private EntityManager em;

    public PedidoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Pedido pedido) {
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
    }

    public List<Pedido> findByDateRange(Date desde, Date hasta) {
        TypedQuery<Pedido> query = em.createQuery(
                "SELECT p FROM Pedido p WHERE p.fHReserva BETWEEN :desde AND :hasta ORDER BY p.fHReserva",
                Pedido.class);
        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
        return query.getResultList();
    }
}