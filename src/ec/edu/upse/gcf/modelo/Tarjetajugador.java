package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the tarjetajugador database table.
 * 
 */
@Entity
@NamedQuery(name="Tarjetajugador.findAll", query="SELECT t FROM Tarjetajugador t")
public class Tarjetajugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tarjeta_jugador")
	private int idTarjetaJugador;

	private int cantidad;

	private String estado;

	private String motivo;

	private Time tiempo;

	//bi-directional many-to-one association to Tarjeta
	@ManyToOne
	@JoinColumn(name="id_tarjeta")
	private Tarjeta tarjeta;

	//bi-directional many-to-one association to Partido
	@ManyToOne
	@JoinColumn(name="id_partido")
	private Partido partido;

	//bi-directional many-to-one association to Equipojugador
	@ManyToOne
	@JoinColumn(name="id_jugador")
	private Equipojugador equipojugador;

	public Tarjetajugador() {
	}

	public int getIdTarjetaJugador() {
		return this.idTarjetaJugador;
	}

	public void setIdTarjetaJugador(int idTarjetaJugador) {
		this.idTarjetaJugador = idTarjetaJugador;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Time getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(Time tiempo) {
		this.tiempo = tiempo;
	}

	public Tarjeta getTarjeta() {
		return this.tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Partido getPartido() {
		return this.partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public Equipojugador getEquipojugador() {
		return this.equipojugador;
	}

	public void setEquipojugador(Equipojugador equipojugador) {
		this.equipojugador = equipojugador;
	}

}