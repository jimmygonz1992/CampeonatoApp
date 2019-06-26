package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the detallecalendario database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Detallecalendario.findAll", query="SELECT d FROM Detallecalendario d"),
	@NamedQuery(name="Detallecalendario.buscarPorPatron", 
	query="SELECT d FROM Detallecalendario d WHERE LOWER(d.calendario.campeonato.nombreC) LIKE LOWER(:patron) ORDER BY d.calendario.campeonato.nombreC"),
	@NamedQuery(name="Detallecalendario.buscarPorEquipo", 
	query="SELECT d FROM Detallecalendario d WHERE LOWER(d.equipo1.nombre) LIKE LOWER(:patron) ORDER BY d.equipo1.nombre")
})
@AdditionalCriteria("this.estado IS NULL")
public class Detallecalendario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detallecalendario")
	private int idDetallecalendario;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Time hora;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="id_calendario")
	private Calendario calendario;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipolocal")
	private Equipo equipo1;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipovisitante")
	private Equipo equipo2;

	//bi-directional many-to-one association to Lugarpartido
	@ManyToOne
	@JoinColumn(name="id_lugarP")
	private Lugarpartido lugarpartido;

	//bi-directional many-to-one association to Partido
	@OneToMany(mappedBy="detallecalendario")
	private List<Partido> partidos;

	public Detallecalendario() {
	}

	public int getIdDetallecalendario() {
		return this.idDetallecalendario;
	}

	public void setIdDetallecalendario(int idDetallecalendario) {
		this.idDetallecalendario = idDetallecalendario;
	}

	public List<Partido> getPartidos() {
		return partidos;
	}

	public void setPartidos(List<Partido> partidos) {
		this.partidos = partidos;
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

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Calendario getCalendario() {
		return this.calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Equipo getEquipo1() {
		return this.equipo1;
	}

	public void setEquipo1(Equipo equipo1) {
		this.equipo1 = equipo1;
	}

	public Equipo getEquipo2() {
		return this.equipo2;
	}

	public void setEquipo2(Equipo equipo2) {
		this.equipo2 = equipo2;
	}

	public Lugarpartido getLugarpartido() {
		return this.lugarpartido;
	}

	public void setLugarpartido(Lugarpartido lugarpartido) {
		this.lugarpartido = lugarpartido;
	}

}