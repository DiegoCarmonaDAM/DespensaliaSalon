package Service;

import Entity.Producto;
import Repository.ProductoRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ProductoService {
    private ProductoRepository productoRepository;

    public ProductoService(EntityManager em) {
        this.productoRepository = new ProductoRepository(em);
    }

    public Producto altaProducto(String nombre, String tipo, String descripcion,
                                 BigDecimal precio, short disponible) {
        // Validar tipo
        if (!tipo.equals("ENTRANTE") && !tipo.equals("PLATO") && !tipo.equals("POSTRE")) {
            throw new RuntimeException("Tipo incorrecto. Solo se permite ENTRANTE, PLATO o POSTRE.");
        }

        Producto producto = new Producto(nombre, tipo, descripcion, precio, disponible);
        productoRepository.save(producto);
        return producto;
    }

    public void borrarProducto(int id) {
        Producto producto = productoRepository.findById(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado.");
        }
        productoRepository.delete(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarProducto(int id) {
        return productoRepository.findById(id);
    }
}