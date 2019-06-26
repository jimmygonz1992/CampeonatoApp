package ec.edu.upse.gcf.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the categoriajugador database table.
 * 
 */

@Entity
@NamedQueries({
	@NamedQuery(name="Categoriajugador.findAll", query="SELECT c FROM Categoriajugador c ORDER BY c.categoria.nombre"),
	@NamedQuery(name="Categoriajugador.BuscarJugadorIdCategoria", 
	query="SELECT cj FROM Categoriajugador cj WHERE cj.categoria.idCategoria = :categoria"),
	@NamedQuery(name="Categoriajugador.buscarPorPatron", 
	query="SELECT c FROM Categoriajugador c WHERE LOWER(c.jugador.nombres) LIKE LOWER(:patron) ORDER BY c.categoria.nombre"),
	@NamedQuery(name="Categoriajugador.BuscarJugadorCategoria", 
	query="SELECT cj FROM Categoriajugador cj WHERE cj.categoria.idCategoria = :categoria and cj.jugador.idJugador = :jugador and cj.estado IS NULL")	
})
@AdditionalCriteria("this.estado IS NULL")
public class Categoriajugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_categoriajugdor")
	private int idCategoriajugdor;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name="id_jugador")
	private Jugador jugador;

	private String estado;

	public Categoriajugador() {
	}

	public int getIdCategoriajugdor() {
		return this.idCategoriajugdor;
	}

	public void setIdCategoriajugdor(int idCategoriajugdor) {
		this.idCategoriajugdor = idCategoriajugdor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}