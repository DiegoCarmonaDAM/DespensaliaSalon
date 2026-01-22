import Entity.*;
import Service.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class DespensaliaSalon {
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("DespensaliaSalonPU");
    private static Scanner sc = new Scanner(System.in);

    private static ClienteService clienteService;
    private static ProductoService productoService;
    private static PedidoService pedidoService;

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();

        // Inicializar servicios
        clienteService = new ClienteService(em);
        productoService = new ProductoService(em);
        pedidoService = new PedidoService(em, clienteService, productoService);

        // Crear encargado si no existe
        clienteService.crearEncargadoSiNoExiste();

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
                case 1 -> menuAltaCliente();
                case 2 -> menuBorrarCliente();
                case 3 -> menuListarClientes();
                case 4 -> menuAltaProducto();
                case 5 -> menuBorrarProducto();
                case 6 -> menuCrearPedido();
                case 7 -> menuListarPedidosEncargado();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida");
            }

        } while (opcion != 0);

        em.close();
        emf.close();
    }

    private static void menuAltaCliente() {
        try {
            System.out.println("\n--- Alta Cliente ---");
            System.out.print("ID Cliente: ");
            String id = sc.nextLine();

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

            clienteService.altaCliente(id, pass, nombre, apellidos, tel, email);
            System.out.println("✅ Cliente creado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void menuBorrarCliente() {
        try {
            System.out.println("\n--- Borrar Cliente ---");
            System.out.print("ID Cliente a borrar: ");
            String id = sc.nextLine();

            clienteService.borrarCliente(id);
            System.out.println("✅ Cliente borrado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void menuListarClientes() {
        System.out.println("\n--- LISTADO DE CLIENTES ---");

        List<Cliente> clientes = clienteService.listarClientes();

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

    private static void menuAltaProducto() {
        try {
            System.out.println("\n--- Alta Producto ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

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

            Producto producto = productoService.altaProducto(nombre, tipo, desc, precio, disp);
            System.out.println("✅ Producto creado con ID: " + producto.getIdProducto());

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void menuBorrarProducto() {
        try {
            System.out.println("\n--- Borrar Producto ---");
            System.out.print("ID Producto a borrar: ");
            int id = Integer.parseInt(sc.nextLine());

            productoService.borrarProducto(id);
            System.out.println("✅ Producto borrado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void menuCrearPedido() {
        try {
            System.out.println("\n--- Crear Pedido ---");

            // Seleccionar cliente
            System.out.print("ID Cliente: ");
            String idCliente = sc.nextLine();

            // Fecha y hora
            System.out.print("Fecha reserva (yyyy-mm-dd): ");
            LocalDate fecha = LocalDate.parse(sc.nextLine());

            System.out.print("Hora reserva (hh:mm): ");
            LocalTime hora = LocalTime.parse(sc.nextLine());

            System.out.print("Observaciones: ");
            String obs = sc.nextLine();

            // Crear pedido
            Pedido pedido = pedidoService.crearPedido(idCliente, fecha, hora, obs);

            // Añadir líneas
            boolean mas = true;
            while (mas) {
                System.out.print("ID Producto: ");
                int idProd = Integer.parseInt(sc.nextLine());

                System.out.print("Cantidad: ");
                short cantidad = Short.parseShort(sc.nextLine());

                pedidoService.agregarLineaPedido(pedido, idProd, cantidad);

                System.out.print("¿Añadir otro producto? (s/n): ");
                mas = sc.nextLine().equalsIgnoreCase("s");
            }

            // Guardar pedido
            pedidoService.guardarPedido(pedido);

            System.out.println("✅ Pedido creado con ID: " + pedido.getIdPedido());
            System.out.println("Importe total: " + pedido.getImporte() + " €");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void menuListarPedidosEncargado() {
        System.out.println("\n--- ACCESO ENCARGADO ---");

        System.out.print("ID Encargado: ");
        String id = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (!pedidoService.validarAccesoEncargado(id, pass)) {
            System.out.println("❌ Acceso denegado.");
            return;
        }

        System.out.println("✅ Acceso concedido. Listando pedidos del día...");

        List<Pedido> pedidos = pedidoService.listarPedidosDelDia();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos para hoy.");
            return;
        }

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
}