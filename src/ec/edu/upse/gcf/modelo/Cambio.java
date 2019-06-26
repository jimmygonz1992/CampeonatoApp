package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the cambios database table.
 * 
 */
@Entity
@Table(name="cambios")
@NamedQuery(name="Cambio.findAll", query="SELECT c FROM Cambio c")
public class Cambio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cambio")
	private int idCambio;

	private String estado;

	private Time tiempo;

	//bi-directional many-to-one association to Partido
	@ManyToOne
	@JoinColumn(name="id_partido")
	private Partido partido;

	//bi-directional many-to-one association to Jugador
	@ManyToOne
	@JoinColumn(name="id_equipojugadorentra")
	private Jugador jugador1;

	//bi-directional many-to-one association to Jugador
	@ManyToOne
	@JoinColumn(name="id_equipojugadorsale")
	private Jugador jugador2;

	public Cambio() {
	}

	public int getIdCambio() {
		return this.idCambio;
	}

	public void setIdCambio(int idCambio) {
		this.idCambio = idCambio;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Time getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(Time tiempo) {
		this.tiempo = tiempo;
	}

	public Partido getPartido() {
		return this.partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public Jugador getJugador1() {
		return this.jugador1;
	}

	public void setJugador1(Jugador jugador1) {
		this.jugador1 = jugador1;
	}

	public Jugador getJugador2() {
		return this.jugador2;
	}

	public void setJugador2(Jugador jugador2) {
		this.jugador2 = jugador2;
	}

}