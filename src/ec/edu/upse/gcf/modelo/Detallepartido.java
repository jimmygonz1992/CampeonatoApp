package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the detallepartido database table.
 * 
 */
@Entity
@NamedQuery(name="Detallepartido.findAll", query="SELECT d FROM Detallepartido d")
public class Detallepartido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_detallePartido;

	private String estado;

	private int golpt;

	private int golst;

	//bi-directional many-to-one association to Partido
	@ManyToOne
	@JoinColumn(name="id_partido")
	private Partido partido;

	//bi-directional many-to-one association to Equipojugador
	@ManyToOne
	@JoinColumn(name="id_equipojugador")
	private Equipojugador equipojugador;

	//bi-directional many-to-one association to Tablaposicione
	@OneToMany(mappedBy="detallepartido")
	private List<Tablaposicione> tablaposiciones;

	public Detallepartido() {
	}

	public int getId_detallePartido() {
		return this.id_detallePartido;
	}

	public void setId_detallePartido(int id_detallePartido) {
		this.id_detallePartido = id_detallePartido;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getGolpt() {
		return this.golpt;
	}

	public void setGolpt(int golpt) {
		this.golpt = golpt;
	}

	public int getGolst() {
		return this.golst;
	}

	public void setGolst(int golst) {
		this.golst = golst;
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

	public List<Tablaposicione> getTablaposiciones() {
		return this.tablaposiciones;
	}

	public void setTablaposiciones(List<Tablaposicione> tablaposiciones) {
		this.tablaposiciones = tablaposiciones;
	}

	public Tablaposicione addTablaposicione(Tablaposicione tablaposicione) {
		getTablaposiciones().add(tablaposicione);
		tablaposicione.setDetallepartido(this);

		return tablaposicione;
	}

	public Tablaposicione removeTablaposicione(Tablaposicione tablaposicione) {
		getTablaposiciones().remove(tablaposicione);
		tablaposicione.setDetallepartido(null);

		return tablaposicione;
	}

}