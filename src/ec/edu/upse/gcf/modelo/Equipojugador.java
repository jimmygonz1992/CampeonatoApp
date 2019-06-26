package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the equipojugador database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Equipojugador.findAll", query="SELECT e FROM Equipojugador e"),
@NamedQuery(name="Equipojugador.BuscarJugadorEquipoCampeonato", 
query="SELECT ej FROM Equipojugador ej WHERE ej.campeonato.idCampeonato = :campeonato and ej.equipo.idEquipo = :equipo "),
@NamedQuery(name="Equipojugador.BuscarJugadorEquipouno", 
query="SELECT ej FROM Equipojugador ej WHERE ej.campeonato.nombreC = :campeonato and ej.equipo.nombre = :equipo "),
@NamedQuery(name="Equipojugador.BuscarJugadorEquipodos", 
query="SELECT ej FROM Equipojugador ej WHERE ej.campeonato.nombreC = :campeonato and ej.equipo.nombre = :equipo "),
@NamedQuery(name="Equipojugador.buscarPorPatron", 
query="SELECT e FROM Equipojugador e WHERE LOWER(e.jugador.nombres) LIKE LOWER(:patron) ORDER BY e.jugador.nombres"),
@NamedQuery(name="Equipojugador.BuscarJugadorGuarda", 
query="SELECT ej FROM Equipojugador ej WHERE ej.campeonato.idCampeonato = :campeonato and ej.equipo.idEquipo = :equipo and ej.jugador.idJugador = :jugador and ej.estado IS NULL ")
})

@AdditionalCriteria("this.estado IS NULL")
public class Equipojugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_equipojugador")
	private int idEquipojugador;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private String observacion;

	//bi-directional many-to-one association to Detallepartido
	@OneToMany(mappedBy="equipojugador")
	private List<Detallepartido> detallepartidos;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipo")
	private Equipo equipo;

	//bi-directional many-to-one association to Jugador
	@ManyToOne
	@JoinColumn(name="id_jugador")
	private Jugador jugador;

	//bi-directional many-to-one association to Campeonato
	@ManyToOne
	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;

	//bi-directional many-to-one association to Tarjetajugador
	@OneToMany(mappedBy="equipojugador")
	private List<Tarjetajugador> tarjetajugadors;

	public Equipojugador() {
	}

	public int getIdEquipojugador() {
		return this.idEquipojugador;
	}

	public void setIdEquipojugador(int idEquipojugador) {
		this.idEquipojugador = idEquipojugador;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<Detallepartido> getDetallepartidos() {
		return this.detallepartidos;
	}

	public void setDetallepartidos(List<Detallepartido> detallepartidos) {
		this.detallepartidos = detallepartidos;
	}

	public Detallepartido addDetallepartido(Detallepartido detallepartido) {
		getDetallepartidos().add(detallepartido);
		detallepartido.setEquipojugador(this);

		return detallepartido;
	}

	public Detallepartido removeDetallepartido(Detallepartido detallepartido) {
		getDetallepartidos().remove(detallepartido);
		detallepartido.setEquipojugador(null);

		return detallepartido;
	}

	public Equipo getEquipo() {
		return this.equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Jugador getJugador() {
		return this.jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public Campeonato getCampeonato() {
		return this.campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public List<Tarjetajugador> getTarjetajugadors() {
		return this.tarjetajugadors;
	}

	public void setTarjetajugadors(List<Tarjetajugador> tarjetajugadors) {
		this.tarjetajugadors = tarjetajugadors;
	}

	public Tarjetajugador addTarjetajugador(Tarjetajugador tarjetajugador) {
		getTarjetajugadors().add(tarjetajugador);
		tarjetajugador.setEquipojugador(this);

		return tarjetajugador;
	}

	public Tarjetajugador removeTarjetajugador(Tarjetajugador tarjetajugador) {
		getTarjetajugadors().remove(tarjetajugador);
		tarjetajugador.setEquipojugador(null);

		return tarjetajugador;
	}

}