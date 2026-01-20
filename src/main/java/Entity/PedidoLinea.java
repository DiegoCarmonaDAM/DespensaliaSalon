/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;

/**
 *
 * @author DAM2
 */
@Entity
@Table(name = "tpedidolineas")
@NamedQueries({
    @NamedQuery(name = "PedidoLinea.findAll", query = "SELECT p FROM PedidoLinea p"),
    @NamedQuery(name = "PedidoLinea.findByIdLinea", query = "SELECT p FROM PedidoLinea p WHERE p.idLinea = :idLinea"),
    @NamedQuery(name = "PedidoLinea.findByCantidad", query = "SELECT p FROM PedidoLinea p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "PedidoLinea.findByPrecio", query = "SELECT p FROM PedidoLinea p WHERE p.precio = :precio")})
public class PedidoLinea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdLinea")
    private Integer idLinea;
    @Basic(optional = false)
    @Column(name = "Cantidad")
    private short cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "Precio")
    private BigDecimal precio;
    @JoinColumn(name = "IdPedido", referencedColumnName = "IdPedido")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Pedido idPedido;
    @JoinColumn(name = "IdProducto", referencedColumnName = "IdProducto")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Producto idProducto;

    public PedidoLinea() {
    }

    public PedidoLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public PedidoLinea(Integer idLinea, short cantidad, BigDecimal precio) {
        this.idLinea = idLinea;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public static PedidoLinea crearLinea(Producto producto, short cantidad) {
        PedidoLinea linea = new PedidoLinea();
        linea.setIdProducto(producto);
        linea.setCantidad(cantidad);
        linea.setPrecio(producto.getPrecio());
        return linea;
    }

    public Integer getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public short getCantidad() {
        return cantidad;
    }

    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Pedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedido idPedido) {
        this.idPedido = idPedido;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLinea != null ? idLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoLinea)) {
            return false;
        }
        PedidoLinea other = (PedidoLinea) object;
        if ((this.idLinea == null && other.idLinea != null) || (this.idLinea != null && !this.idLinea.equals(other.idLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.despensaliasql.PedidoLinea[ idLinea=" + idLinea + " ]";
    }
    
}
