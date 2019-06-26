package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the configuracionjugador database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Configuracionjugador.findAll", query="SELECT c FROM Configuracionjugador c"),
@NamedQuery(name="Configuracionjugador.buscarPorPatron", 
query="SELECT c FROM Configuracionjugador c WHERE LOWER(c.campeonato.nombreC) LIKE LOWER(:patron)"),

})
@AdditionalCriteria("this.estado IS NULL")
public class Configuracionjugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_config_jugadores")
	private int idConfigJugadores;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	@Column(name="hora_fin")
	private Time horaFin;

	@Column(name="hora_inicio")
	private Time horaInicio;

	@ManyToOne
	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;


	public Configuracionjugador() {
	}

	public int getIdConfigJugadores() {
		return this.idConfigJugadores;
	}

	public void setIdConfigJugadores(int idConfigJugadores) {
		this.idConfigJugadores = idConfigJugadores;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Time getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}

	public Time getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}
}