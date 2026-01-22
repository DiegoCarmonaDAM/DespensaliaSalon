package Service;

import Entity.*;
import Repository.PedidoRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PedidoService {
    private PedidoRepository pedidoRepository;
    private ClienteService clienteService;
    private ProductoService productoService;
    private EntityManager em;

    public PedidoService(EntityManager em, ClienteService clienteService, ProductoService productoService) {
        this.em = em;
        this.pedidoRepository = new PedidoRepository(em);
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    public Pedido crearPedido(String idCliente, LocalDate fechaReserva, LocalTime horaReserva,
                              String observaciones) {
        // Buscar cliente
        Cliente cliente = clienteService.buscarCliente(idCliente);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado.");
        }

        // Convertir fecha y hora a Date
        Date fhReserva = Date.from(
                LocalDateTime.of(fechaReserva, horaReserva)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        // Crear pedido
        Pedido pedido = new Pedido(cliente, fhReserva, observaciones);

        return pedido;
    }

    public void agregarLineaPedido(Pedido pedido, int idProducto, short cantidad) {
        Producto producto = productoService.buscarProducto(idProducto);
        if (producto == null) {
            throw new RuntimeException("Producto no existe.");
        }

        PedidoLinea linea = PedidoLinea.crearLinea(producto, cantidad);
        pedido.addLinea(linea);
    }

    public void guardarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosDelDia() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(17, 0);

        Date desde = Date.from(inicioDia.atZone(ZoneId.systemDefault()).toInstant());
        Date hasta = Date.from(finDia.atZone(ZoneId.systemDefault()).toInstant());

        return pedidoRepository.findByDateRange(desde, hasta);
    }

    public boolean validarAccesoEncargado(String id, String password) {
        return id.equals("encargado") && password.equals("1234");
    }
}