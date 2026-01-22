package Service;

import Entity.Cliente;
import Repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(EntityManager em) {
        this.clienteRepository = new ClienteRepository(em);
    }

    public void altaCliente(String id, String password, String nombre,
                            String apellidos, String telefono, String email) {
        // Verificar si ya existe
        Cliente existente = clienteRepository.findById(id);
        if (existente != null) {
            throw new RuntimeException("Ya existe un cliente con ese ID.");
        }
        // Verificar ID no vacío
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cliente no puede estar vacío.");
        }

        Cliente cliente = new Cliente(id, password, nombre, apellidos, telefono, email);
        clienteRepository.save(cliente);
    }

    public void borrarCliente(String id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado.");
        }
        clienteRepository.delete(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarCliente(String id) {
        return clienteRepository.findById(id);
    }

    public void crearEncargadoSiNoExiste() {
        Cliente encargado = clienteRepository.findById("encargado");
        if (encargado == null) {
            Cliente nuevoEncargado = new Cliente(
                    "encargado",
                    "1234",
                    "Encargado",
                    "Restaurante",
                    "000000000",
                    "encargado@local.com"
            );
            clienteRepository.save(nuevoEncargado);
        }
    }
}