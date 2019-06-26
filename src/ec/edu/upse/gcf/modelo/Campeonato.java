package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the campeonato database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Campeonato.findAll", query="SELECT c FROM Campeonato c WHERE c.estadocamp = 'D' "),
	@NamedQuery(name="Campeonatos.buscarPorPatron", 
	query="SELECT c FROM Campeonato c WHERE LOWER(c.nombreC) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Campeonato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_campeonato")
	private int idCampeonato;

	private String estado;

	private String estadocamp;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	private String nombreC;

	private String tipoCampeonato;
	
	//bi-directional many-to-one association to Calendario
	@OneToMany(mappedBy="campeonato")
	private List<Calendario> calendarios;

	//bi-directional many-to-one association to Detallecampeonato
	@OneToMany(mappedBy="campeonato",cascade = CascadeType.ALL)
	private List<Detallecampeonato> detallecampeonatos;

	//bi-directional many-to-one association to Equipojugador
	@OneToMany(mappedBy="campeonato")
	private List<Equipojugador> equipojugadors;	

	//bi-directional many-to-one association to Equipojugador
	@OneToMany(mappedBy="campeonato")
	private List<Configuracionjugador> configuracionjugadores;	

	public Campeonato() {
	}

	public int getIdCampeonato() {
		return this.idCampeonato;
	}

	public void setIdCampeonato(int idCampeonato) {
		this.idCampeonato = idCampeonato;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadocamp() {
		return this.estadocamp;
	}

	public void setEstadocamp(String estadocamp) {
		this.estadocamp = estadocamp;
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

	public String getNombreC() {
		return this.nombreC;
	}


	public String getTipoCampeonato() {
		return tipoCampeonato;
	}

	public void setTipoCampeonato(String tipoCampeonato) {
		this.tipoCampeonato = tipoCampeonato;
	}

	public void setNombreC(String nombreC) {
		this.nombreC = nombreC;
	}

	public List<Calendario> getCalendarios() {
		return this.calendarios;
	}

	public void setCalendarios(List<Calendario> calendarios) {
		this.calendarios = calendarios;
	}

	public Calendario addCalendario(Calendario calendario) {
		getCalendarios().add(calendario);
		calendario.setCampeonato(this);

		return calendario;
	}

	public Calendario removeCalendario(Calendario calendario) {
		getCalendarios().remove(calendario);
		calendario.setCampeonato(null);

		return calendario;
	}

	public List<Detallecampeonato> getDetallecampeonatos() {
		return this.detallecampeonatos;
	}

	public void setDetallecampeonatos(List<Detallecampeonato> detallecampeonatos) {
		this.detallecampeonatos = detallecampeonatos;
	}

	public Detallecampeonato addDetallecampeonato(Detallecampeonato detallecampeonato) {
		getDetallecampeonatos().add(detallecampeonato);
		detallecampeonato.setCampeonato(this);

		return detallecampeonato;
	}

	public Detallecampeonato removeDetallecampeonato(Detallecampeonato detallecampeonato) {
		getDetallecampeonatos().remove(detallecampeonato);
		detallecampeonato.setCampeonato(null);

		return detallecampeonato;
	}

	public List<Equipojugador> getEquipojugadors() {
		return this.equipojugadors;
	}

	public void setEquipojugadors(List<Equipojugador> equipojugadors) {
		this.equipojugadors = equipojugadors;
	}

	public Equipojugador addEquipojugador(Equipojugador equipojugador) {
		getEquipojugadors().add(equipojugador);
		equipojugador.setCampeonato(this);

		return equipojugador;
	}

	public Equipojugador removeEquipojugador(Equipojugador equipojugador) {
		getEquipojugadors().remove(equipojugador);
		equipojugador.setCampeonato(null);

		return equipojugador;
	}

	public List<Configuracionjugador> getConfiguracionjugadores() {
		return configuracionjugadores;
	}

	public void setConfiguracionjugadores(List<Configuracionjugador> configuracionjugadores) {
		this.configuracionjugadores = configuracionjugadores;
	}

	@Override
	public String toString() {
		return this.nombreC;
	}

}