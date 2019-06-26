package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the tipocancha database table.
 * 
 */
@Entity 
@NamedQueries({
	@NamedQuery(name="Tipocancha.findAll", query="SELECT t FROM Tipocancha t"),
	@NamedQuery(name="Tipocancha.buscarPorPatron", 
	query="SELECT t FROM Tipocancha t WHERE LOWER(t.descripcion) LIKE (:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")
public class Tipocancha implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_tipoC;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Lugarpartido
	@OneToMany(mappedBy="tipocancha")
	private List<Lugarpartido> lugarpartidos;

	public Tipocancha() {
	}

	public int getId_tipoC() {
		return this.id_tipoC;
	}

	public void setId_tipoC(int id_tipoC) {
		this.id_tipoC = id_tipoC;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Lugarpartido> getLugarpartidos() {
		return this.lugarpartidos;
	}

	public void setLugarpartidos(List<Lugarpartido> lugarpartidos) {
		this.lugarpartidos = lugarpartidos;
	}

	public Lugarpartido addLugarpartido(Lugarpartido lugarpartido) {
		getLugarpartidos().add(lugarpartido);
		lugarpartido.setTipocancha(this);

		return lugarpartido;
	}

	public Lugarpartido removeLugarpartido(Lugarpartido lugarpartido) {
		getLugarpartidos().remove(lugarpartido);
		lugarpartido.setTipocancha(null);

		return lugarpartido;
	}

}