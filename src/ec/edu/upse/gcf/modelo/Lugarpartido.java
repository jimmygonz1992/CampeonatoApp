package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the lugarpartido database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Lugarpartido.findAll", query="SELECT l FROM Lugarpartido l"),
	@NamedQuery(name="Lugarpartido.buscarPorPatron", query="SELECT l FROM Lugarpartido l WHERE LOWER(l.nombreCancha) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Lugarpartido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_lugarP;

	private String estado;

	private String nombreCancha;

	//bi-directional many-to-one association to Detallecalendario
	@OneToMany(mappedBy="lugarpartido")
	private List<Detallecalendario> detallecalendarios;

	//bi-directional many-to-one association to Tipocancha
	@ManyToOne
	@JoinColumn(name="id_tipoC")
	private Tipocancha tipocancha;

	public Lugarpartido() {
	}

	public int getId_lugarP() {
		return this.id_lugarP;
	}

	public void setId_lugarP(int id_lugarP) {
		this.id_lugarP = id_lugarP;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreCancha() {
		return this.nombreCancha;
	}

	public void setNombreCancha(String nombreCancha) {
		this.nombreCancha = nombreCancha;
	}

	public List<Detallecalendario> getDetallecalendarios() {
		return this.detallecalendarios;
	}

	public void setDetallecalendarios(List<Detallecalendario> detallecalendarios) {
		this.detallecalendarios = detallecalendarios;
	}

	public Detallecalendario addDetallecalendario(Detallecalendario detallecalendario) {
		getDetallecalendarios().add(detallecalendario);
		detallecalendario.setLugarpartido(this);

		return detallecalendario;
	}

	public Detallecalendario removeDetallecalendario(Detallecalendario detallecalendario) {
		getDetallecalendarios().remove(detallecalendario);
		detallecalendario.setLugarpartido(null);

		return detallecalendario;
	}

	public Tipocancha getTipocancha() {
		return this.tipocancha;
	}

	public void setTipocancha(Tipocancha tipocancha) {
		this.tipocancha = tipocancha;
	}
	
	@Override
	public String toString() {
		return this.nombreCancha;
	}

}