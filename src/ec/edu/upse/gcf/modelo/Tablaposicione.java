package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tablaposiciones database table.
 * 
 */
@Entity
@Table(name="tablaposiciones")
@NamedQuery(name="Tablaposicione.findAll", query="SELECT t FROM Tablaposicione t")
public class Tablaposicione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_taplapo")
	private int idTaplapo;

	private String estado;

	private int gc;

	private int gd;

	private int gf;

	private int pe;

	private int pg;

	private int pj;

	private int pp;

	private int ptos;

	//bi-directional many-to-one association to Detallepartido
	@ManyToOne
	@JoinColumn(name="id_detallePartido")
	private Detallepartido detallepartido;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="id_calendario")
	private Calendario calendario;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipo")
	private Equipo equipo;

	public Tablaposicione() {
	}

	public int getIdTaplapo() {
		return this.idTaplapo;
	}

	public void setIdTaplapo(int idTaplapo) {
		this.idTaplapo = idTaplapo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getGc() {
		return this.gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}

	public int getGd() {
		return this.gd;
	}

	public void setGd(int gd) {
		this.gd = gd;
	}

	public int getGf() {
		return this.gf;
	}

	public void setGf(int gf) {
		this.gf = gf;
	}

	public int getPe() {
		return this.pe;
	}

	public void setPe(int pe) {
		this.pe = pe;
	}

	public int getPg() {
		return this.pg;
	}

	public void setPg(int pg) {
		this.pg = pg;
	}

	public int getPj() {
		return this.pj;
	}

	public void setPj(int pj) {
		this.pj = pj;
	}

	public int getPp() {
		return this.pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getPtos() {
		return this.ptos;
	}

	public void setPtos(int ptos) {
		this.ptos = ptos;
	}

	public Detallepartido getDetallepartido() {
		return this.detallepartido;
	}

	public void setDetallepartido(Detallepartido detallepartido) {
		this.detallepartido = detallepartido;
	}

	public Calendario getCalendario() {
		return this.calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Equipo getEquipo() {
		return this.equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

}