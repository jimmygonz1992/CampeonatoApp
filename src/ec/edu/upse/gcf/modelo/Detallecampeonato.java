package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the detallecampeonato database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Detallecampeonato.findAll", query="SELECT d FROM Detallecampeonato d"),
	@NamedQuery(name="Detallecampeonato.BuscaPorCategoria", query="SELECT d FROM Detallecampeonato d WHERE d.campeonato = (:patron) " ),
	@NamedQuery(name="Detallecampeonato.buscarPorPatron", 
	query="SELECT d FROM Detallecampeonato d WHERE LOWER(d.campeonato.nombreC) LIKE LOWER(:patron) ORDER BY d.campeonato.nombreC"),
	@NamedQuery(name="Detallecampeonato.BuscarEquipoCampeonato", 
	query="SELECT d FROM Detallecampeonato d WHERE d.campeonato.idCampeonato = :campeonato and d.categoria.idCategoria = :categoria "),
	@NamedQuery(name="Detallecampeonato.BuscarEquipoCamp", 
	query="SELECT d FROM Detallecampeonato d WHERE d.campeonato.idCampeonato = :campeonato "),
	@NamedQuery(name="Detallecampeonato.BuscarEquipos", 
	query="SELECT d FROM Detallecampeonato d WHERE d.campeonato.idCampeonato = :campeonato and d.categoria.idCategoria = :categoria and d.equipo.idEquipo = :equipo and d.estado IS NULL")
})
@AdditionalCriteria("this.estado IS NULL")
public class Detallecampeonato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_detalleC;

	private String estado;

	private String observacion;

	//bi-directional many-to-one association to Campeonato
	@ManyToOne
	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipo")
	private Equipo equipo;

	public Detallecampeonato() {
	}

	public int getId_detalleC() {
		return this.id_detalleC;
	}

	public void setId_detalleC(int id_detalleC) {
		this.id_detalleC = id_detalleC;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Campeonato getCampeonato() {
		return this.campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}