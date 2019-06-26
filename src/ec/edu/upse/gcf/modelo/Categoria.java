package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the categoria database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Categoria.BuscarTipo", query="SELECT CONCAT(c.nombre, ' ' , c.tipo) AS sub FROM Categoria c"),
	@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c WHERE c.estado IS NULL"),
	@NamedQuery(name="Categorias.buscarPorPatron", 
	query="SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(:patron)"),
	@NamedQuery(name="Categoria.BuscarTipoC", 
	query="SELECT c FROM Categoria c WHERE c.tipo = :tipo"),
})
@AdditionalCriteria("this.estado IS NULL")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_categoria")
	private int idCategoria;

	private String descripcion;

	private String tipo;

	private String estado;

	private String nombre;

	//bi-directional many-to-one association to Detallecampeonato
	@OneToMany(mappedBy="categoria")
	private List<Categoriajugador> categoriajugadores;

	//bi-directional many-to-one association to Detallecampeonato
	@OneToMany(mappedBy="categoria")
	private List<Detallecampeonato> detallecampeonatos;

	public Categoria() {
	}

	public int getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Categoriajugador> getCategoriajugadores() {
		return categoriajugadores;
	}

	public List<Detallecampeonato> getDetallecampeonatos() {
		return detallecampeonatos;
	}

	public void setDetallecampeonatos(List<Detallecampeonato> detallecampeonatos) {
		this.detallecampeonatos = detallecampeonatos;
	}

	public void setCategoriajugadores(List<Categoriajugador> categoriajugadores) {
		this.categoriajugadores = categoriajugadores;
	}	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}