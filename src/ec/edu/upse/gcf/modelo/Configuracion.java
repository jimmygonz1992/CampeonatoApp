package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.sql.Time;


/**
 * The persistent class for the configuracion database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Configuracion.findAll", query="SELECT c FROM Configuracion c"),
@NamedQuery(name="Configuracion.buscarPorPatron", 
query="SELECT c FROM Configuracion c WHERE LOWER(c.diasJuegoInicio) LIKE LOWER(:patron)"),

})
@AdditionalCriteria("this.estado IS NULL")
public class Configuracion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_configuracion")
	private int idConfiguracion;

	@Column(name="dias_juego_fin")
	private String diasJuegoFin;

	@Column(name="dias_juego_inicio")
	private String diasJuegoInicio;

	private String estado;

	@Column(name="hora_fin")
	private Time horaFin;

	@Column(name="hora_inicio")
	private Time horaInicio;

	@Column(name="partido_por_dia")
	private int partidoPorDia;

	@Column(name="tiempo_duracion")
	private Time tiempoDuracion;

	public Configuracion() {
	}

	public int getIdConfiguracion() {
		return this.idConfiguracion;
	}

	public void setIdConfiguracion(int idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}

	public String getDiasJuegoFin() {
		return this.diasJuegoFin;
	}

	public void setDiasJuegoFin(String diasJuegoFin) {
		this.diasJuegoFin = diasJuegoFin;
	}

	public String getDiasJuegoInicio() {
		return this.diasJuegoInicio;
	}

	public void setDiasJuegoInicio(String diasJuegoInicio) {
		this.diasJuegoInicio = diasJuegoInicio;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public int getPartidoPorDia() {
		return this.partidoPorDia;
	}

	public void setPartidoPorDia(int partidoPorDia) {
		this.partidoPorDia = partidoPorDia;
	}

	public Time getTiempoDuracion() {
		return this.tiempoDuracion;
	}

	public void setTiempoDuracion(Time tiempoDuracion) {
		this.tiempoDuracion = tiempoDuracion;
	}

}