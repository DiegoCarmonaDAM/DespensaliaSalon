/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

/**
 *
 * @author DAM2
 */
@Entity
@Table(name = "tpedidos")
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT p FROM Pedido p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "Pedido.findByFHPedido", query = "SELECT p FROM Pedido p WHERE p.fHPedido = :fHPedido"),
    @NamedQuery(name = "Pedido.findByFHReserva", query = "SELECT p FROM Pedido p WHERE p.fHReserva = :fHReserva"),
    @NamedQuery(name = "Pedido.findByImporte", query = "SELECT p FROM Pedido p WHERE p.importe = :importe"),
    @NamedQuery(name = "Pedido.findByObservaciones", query = "SELECT p FROM Pedido p WHERE p.observaciones = :observaciones")})
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPedido")
    private Integer idPedido;
    @Basic(optional = false)
    @Column(name = "FHPedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fHPedido;
    @Basic(optional = false)
    @Column(name = "FHReserva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fHReserva;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "Importe")
    private BigDecimal importe;
    @Column(name = "Observaciones")
    private String observaciones;
    @JoinColumn(name = "IdCliente", referencedColumnName = "IdCliente")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cliente idCliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPedido", fetch = FetchType.EAGER)
    private List<PedidoLinea> pedidoLineaList;

    public Pedido() {
    }

    public Pedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Pedido(Integer idPedido, Date fHPedido, Date fHReserva, BigDecimal importe) {
        this.idPedido = idPedido;
        this.fHPedido = fHPedido;
        this.fHReserva = fHReserva;
        this.importe = importe;
    }

    public Pedido(Cliente cliente, Date fhReserva, String observaciones, PedidoLinea... lineas) {
        this.idCliente = cliente;
        this.fHPedido = new Date(); // Fecha de creación
        this.fHReserva = fhReserva;
        this.observaciones = observaciones;
        this.pedidoLineaList = new ArrayList<>();

        for (PedidoLinea linea : lineas) {
            this.addLinea(linea);
        }
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFHPedido() {
        return fHPedido;
    }

    public void setFHPedido(Date fHPedido) {
        this.fHPedido = fHPedido;
    }

    public Date getFHReserva() {
        return fHReserva;
    }

    public void setFHReserva(Date fHReserva) {
        this.fHReserva = fHReserva;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public List<PedidoLinea> getPedidoLineaList() {
        return pedidoLineaList;
    }

    public void setPedidoLineaList(List<PedidoLinea> pedidoLineaList) {
        this.pedidoLineaList = pedidoLineaList;
    }

    // Añadir línea al pedido
    public void addLinea(PedidoLinea linea) {
        if (pedidoLineaList == null) {
            pedidoLineaList = new ArrayList<>();
        }
        pedidoLineaList.add(linea);
        linea.setIdPedido(this);
        recalcularImporte();
    }

    // Recalcular importe total
    public void recalcularImporte() {
        BigDecimal total = BigDecimal.ZERO;
        if (pedidoLineaList != null) {
            for (PedidoLinea l : pedidoLineaList) {
                BigDecimal subtotal = l.getPrecio()
                        .multiply(BigDecimal.valueOf(l.getCantidad()));
                total = total.add(subtotal);
            }
        }
        this.importe = total;
    }

    // Regla: ¿puede modificarse o cancelarse?
    // Hasta las 12:00 del día de la reserva
    /*
    public boolean puedeModificarse() {
        LocalDateTime ahora = LocalDateTime.now();

        LocalDate fechaReserva = fHReserva.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDateTime limite = fechaReserva.atTime(12, 0);

        return ahora.isBefore(limite);
    }
    */


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.despensaliasql.Pedido[ idPedido=" + idPedido + " ]";
    }
    
}
