
import Entity.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.Date;


import Entity.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.Date;

public class DespensaliaSalon {
    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("DespensaliaSalonPU");

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // ===== Cliente 1 =====
            Cliente c1 = new Cliente();
            c1.setIdCliente("Diego");
            c1.setPassword("1234");
            c1.setNombre("Diego");
            c1.setApellidos("Carmona De Haro");
            c1.setEmail("diego@email.com");
            em.persist(c1);

            // ===== Cliente 2 =====
            Cliente c2 = new Cliente("maria01", "abcd", "María", "López Pérez", "maria@email.com");
            em.persist(c2);

            // ===== Cliente 3 =====
            Cliente c3 = new Cliente("juan01", "pass123", "Juan", "Martínez García", "juan@email.com");
            em.persist(c3);

            // ===== Cliente 4 =====
            Cliente c4 = new Cliente("laura01", "xyz789", "Laura", "Fernández Ruiz", "laura@email.com");
            em.persist(c4);

            // ===== Cliente 5 =====
            Cliente c5 = new Cliente("pablo01", "qwerty", "Pablo", "Sánchez Díaz", "pablo@email.com");
            em.persist(c5);


// ===== Productos =====
            Producto prod1 = new Producto("Ensalada", "ENTRANTE", "Ensalada mixta", new BigDecimal("5.50"), (short)1);
            Producto prod2 = new Producto("Lasaña", "PLATO", "Lasaña de carne", new BigDecimal("9.90"), (short)1);
            Producto prod3 = new Producto("Sopa", "ENTRANTE", "Sopa de verduras", new BigDecimal("4.20"), (short)1);
            Producto prod4 = new Producto("Pizza", "PLATO", "Pizza margarita", new BigDecimal("8.50"), (short)1);

            em.persist(prod1);
            em.persist(prod2);
            em.persist(prod3);
            em.persist(prod4);

// ===== Pedidos =====
            Pedido pedido1 = new Pedido(
                    c1,
                    Date.from(LocalDate.now().atTime(14,30).atZone(ZoneId.systemDefault()).toInstant()),
                    "Mesa interior",
                    PedidoLinea.crearLinea(prod1, (short)2),
                    PedidoLinea.crearLinea(prod2, (short)1)
            );
            em.persist(pedido1);

            Pedido pedido2 = new Pedido(
                    c2,
                    Date.from(LocalDate.now().atTime(13,0).atZone(ZoneId.systemDefault()).toInstant()),
                    "Ventana",
                    PedidoLinea.crearLinea(prod3, (short)1),
                    PedidoLinea.crearLinea(prod4, (short)2)
            );
            em.persist(pedido2);

            Pedido pedido3 = new Pedido(
                    c3,
                    Date.from(LocalDate.now().atTime(15,0).atZone(ZoneId.systemDefault()).toInstant()),
                    "Exterior",
                    PedidoLinea.crearLinea(prod2, (short)1)
            );
            em.persist(pedido3);

            Pedido pedido4 = new Pedido(
                    c4,
                    Date.from(LocalDate.now().atTime(12,30).atZone(ZoneId.systemDefault()).toInstant()),
                    "Cerca barra",
                    PedidoLinea.crearLinea(prod1, (short)1),
                    PedidoLinea.crearLinea(prod4, (short)1)
            );
            em.persist(pedido4);

            Pedido pedido5 = new Pedido(
                    c5,
                    Date.from(LocalDate.now().atTime(16,0).atZone(ZoneId.systemDefault()).toInstant()),
                    "Mesa grande",
                    PedidoLinea.crearLinea(prod3, (short)3)
            );
            em.persist(pedido5);

            em.getTransaction().commit();


            // ===== Mostrar algunos resultados =====
            System.out.println("Pedido 1 guardado con ID: " + pedido1.getIdPedido() + ", total: " + pedido1.getImporte());
            System.out.println("Pedido 2 guardado con ID: " + pedido2.getIdPedido() + ", total: " + pedido2.getImporte());
            System.out.println("Pedido 3 guardado con ID: " + pedido3.getIdPedido() + ", total: " + pedido3.getImporte());
            System.out.println("Pedido 4 guardado con ID: " + pedido4.getIdPedido() + ", total: " + pedido4.getImporte());
            System.out.println("Pedido 5 guardado con ID: " + pedido5.getIdPedido() + ", total: " + pedido5.getImporte());

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
