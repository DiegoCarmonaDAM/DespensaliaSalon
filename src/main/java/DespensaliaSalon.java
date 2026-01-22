import Entity.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public class DespensaliaSalon {

    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("DespensaliaSalonPU");

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();

        int opcion;

        do {
            System.out.println("\n===== DESPENSALIA =====");
            System.out.println("1. Dar de alta cliente");
            System.out.println("2. Borrar cliente");
            System.out.println("3. Listar clientes");
            System.out.println("4. Dar de alta producto");
            System.out.println("5. Borrar producto");
            System.out.println("6. Crear pedido");
            System.out.println("7. Listar pedidos (encargado)");

            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> altaCliente(em);
                case 2 -> borrarCliente(em);
                case 3 -> listarClientes(em);
                case 4 -> altaProducto(em);
                case 5 -> borrarProducto(em);
                case 6 -> crearPedido(em);
                case 7 -> listarPedidosEncargado(em);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida");
            }

        } while (opcion != 0);

        em.close();
        emf.close();
    }

    // =======================
    // 1. Alta Cliente
    // =======================
    private static void altaCliente(EntityManager em) {
        try {
            System.out.println("\n--- Alta Cliente ---");
            System.out.print("ID Cliente: ");
            String id = sc.nextLine();

            // 🔎 Comprobar si ya existe
            Cliente existente = em.find(Cliente.class, id);
            if (existente != null) {
                System.out.println("❌ Ya existe un cliente con ese ID.");
                return;
            }

            System.out.print("Password: ");
            String pass = sc.nextLine();

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Apellidos: ");
            String apellidos = sc.nextLine();

            System.out.print("Teléfono: ");
            String tel = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            Cliente c = new Cliente(id, pass, nombre, apellidos, tel, email);

            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();

            System.out.println("✅ Cliente creado correctamente.");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            System.out.println("Error al crear cliente: " + e.getMessage());
        }
    }

    // =======================
    // 2. Alta Producto
    // =======================
    private static void altaProducto(EntityManager em) {
        try {
            System.out.println("\n--- Alta Producto ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            // ============================
            // Validación del tipo
            // ============================
            String tipo;
            while (true) {
                System.out.print("Tipo (ENTRANTE / PLATO / POSTRE): ");
                tipo = sc.nextLine().toUpperCase();

                if (tipo.equals("ENTRANTE") || tipo.equals("PLATO") || tipo.equals("POSTRE")) {
                    break;
                } else {
                    System.out.println("❌ Tipo incorrecto. Solo se permite ENTRANTE, PLATO o POSTRE.");
                }
            }

            System.out.print("Descripción: ");
            String desc = sc.nextLine();

            System.out.print("Precio: ");
            BigDecimal precio = new BigDecimal(sc.nextLine());

            System.out.print("Disponible (1 = sí, 0 = no): ");
            short disp = Short.parseShort(sc.nextLine());

            Producto p = new Producto(nombre, tipo, desc, precio, disp);

            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();

            System.out.println("✅ Producto creado con ID: " + p.getIdProducto());

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            System.out.println("Error al crear producto: " + e.getMessage());
        }
    }

    // =======================
    // 3. Crear Pedido
    // =======================
    private static void crearPedido(EntityManager em) {
        try {
            System.out.println("\n--- Crear Pedido ---");

            // Seleccionar cliente
            System.out.print("ID Cliente: ");
            String idCliente = sc.nextLine();

            Cliente c = em.find(Cliente.class, idCliente);
            if (c == null) {
                System.out.println("Cliente no encontrado.");
                return;
            }

            // Fecha reserva
            System.out.print("Fecha reserva (yyyy-mm-dd): ");
            String fecha = sc.nextLine();

            System.out.print("Hora reserva (hh:mm): ");
            String hora = sc.nextLine();

            LocalDate ld = LocalDate.parse(fecha);
            LocalTime lt = LocalTime.parse(hora);

            Date fhReserva = Date.from(
                    LocalDateTime.of(ld, lt)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );

            System.out.print("Observaciones: ");
            String obs = sc.nextLine();

            Pedido pedido = new Pedido(c, fhReserva, obs);

            // Añadir líneas
            boolean mas = true;
            while (mas) {
                System.out.print("ID Producto: ");
                int idProd = Integer.parseInt(sc.nextLine());

                Producto prod = em.find(Producto.class, idProd);
                if (prod == null) {
                    System.out.println("Producto no existe.");
                    continue;
                }

                System.out.print("Cantidad: ");
                short cantidad = Short.parseShort(sc.nextLine());

                PedidoLinea linea = PedidoLinea.crearLinea(prod, cantidad);
                pedido.addLinea(linea);

                System.out.print("¿Añadir otro producto? (s/n): ");
                mas = sc.nextLine().equalsIgnoreCase("s");
            }

            em.getTransaction().begin();
            em.persist(pedido);
            em.getTransaction().commit();

            System.out.println("Pedido creado con ID: " + pedido.getIdPedido());
            System.out.println("Importe total: " + pedido.getImporte() + " €");

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error al crear pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Listar clientes
    private static void listarClientes(EntityManager em) {
        System.out.println("\n--- LISTADO DE CLIENTES ---");

        List<Cliente> clientes = em
                .createQuery("SELECT c FROM Cliente c", Cliente.class)
                .getResultList();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println("ID: " + c.getIdCliente()
                    + " | Nombre: " + c.getNombre()
                    + " " + c.getApellidos()
                    + " | Email: " + c.getEmail());
        }
    }
    // Borrar cliente
    private static void borrarCliente(EntityManager em) {
        try {
            System.out.println("\n--- BORRAR CLIENTE ---");
            System.out.print("ID Cliente a borrar: ");
            String id = sc.nextLine();

            Cliente c = em.find(Cliente.class, id);

            if (c == null) {
                System.out.println("❌ Cliente no encontrado.");
                return;
            }

            em.getTransaction().begin();
            em.remove(c);
            em.getTransaction().commit();

            System.out.println("✅ Cliente borrado correctamente.");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            System.out.println("Error al borrar cliente: " + e.getMessage());
        }
    }
    // Borrar producto
    private static void borrarProducto(EntityManager em) {
        try {
            System.out.println("\n--- BORRAR PRODUCTO ---");
            System.out.print("ID Producto a borrar: ");
            int id = Integer.parseInt(sc.nextLine());

            Producto p = em.find(Producto.class, id);

            if (p == null) {
                System.out.println("❌ Producto no encontrado.");
                return;
            }

            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();

            System.out.println("✅ Producto borrado correctamente.");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            System.out.println("Error al borrar producto: " + e.getMessage());
        }
    }

    private static void listarPedidosEncargado(EntityManager em) {

        System.out.println("\n--- ACCESO ENCARGADO ---");

        System.out.print("ID Encargado: ");
        String id = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        // Buscar "encargado" como si fuera un cliente
        Cliente encargado = em.find(Cliente.class, id);

        if (encargado == null || !pass.equals("1234")) {
            System.out.println("❌ Acceso denegado.");
            return;
        }

        System.out.println("✅ Acceso concedido. Listando pedidos del día...");

        // =============================
        // Calcular rango de hoy hasta 17:00
        // =============================

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(17, 0);

        Date desde = Date.from(inicioDia.atZone(ZoneId.systemDefault()).toInstant());
        Date hasta = Date.from(finDia.atZone(ZoneId.systemDefault()).toInstant());

        // =============================
        // Consulta de pedidos
        // =============================

        List<Pedido> pedidos = em.createQuery(
                        "SELECT p FROM Pedido p WHERE p.fHReserva BETWEEN :desde AND :hasta ORDER BY p.fHReserva",
                        Pedido.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos para hoy.");
            return;
        }

        // =============================
        // Mostrar pedidos
        // =============================

        for (Pedido p : pedidos) {
            System.out.println("-----------------------------------");
            System.out.println("Pedido ID: " + p.getIdPedido());
            System.out.println("Cliente: " + p.getIdCliente().getIdCliente());
            System.out.println("Reserva: " + p.getFHReserva());
            System.out.println("Observaciones: " + p.getObservaciones());
            System.out.println("Importe: " + p.getImporte() + " €");

            for (PedidoLinea l : p.getPedidoLineaList()) {
                System.out.println("   - " + l.getIdProducto().getNombre()
                        + " x" + l.getCantidad()
                        + " -> " + l.getSubtotal() + " €");
            }
        }
    }
    // Crear el cliente "encargado" si no existe
    private static void crearEncargadoSiNoExiste(EntityManager em) {

        Cliente enc = em.find(Cliente.class, "encargado");

        if (enc == null) {
            Cliente encargado = new Cliente(
                    "encargado",
                    "1234",
                    "Encargado",
                    "Restaurante",
                    "000000000",
                    "encargado@local.com"
            );

            em.getTransaction().begin();
            em.persist(encargado);
            em.getTransaction().commit();

            System.out.println("✔ Encargado creado automáticamente");
        }
    }

}
