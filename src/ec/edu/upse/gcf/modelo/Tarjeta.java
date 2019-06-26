package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the tarjeta database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Tarjeta.findAll", query="SELECT t FROM Tarjeta t"),
	@NamedQuery(name="Tarjetas.buscarPorPatron", 
	            query="SELECT t FROM Tarjeta t WHERE LOWER(t.descripcion) LIKE LOWER(:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")
public class Tarjeta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tarjeta")
	private int idTarjeta;

	private String descripcion;

	private String estado;

	private float valor;

	//bi-directional many-to-one association to Tarjetajugador
	@OneToMany(mappedBy="tarjeta")
	private List<Tarjetajugador> tarjetajugadors;

	public Tarjeta() {
	}

	public int getIdTarjeta() {
		return this.idTarjeta;
	}

	public void setIdTarjeta(int idTarjeta) {
		this.idTarjeta = idTarjeta;
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

	public float getValor() {
		return this.valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public List<Tarjetajugador> getTarjetajugadors() {
		return this.tarjetajugadors;
	}

	public void setTarjetajugadors(List<Tarjetajugador> tarjetajugadors) {
		this.tarjetajugadors = tarjetajugadors;
	}

	public Tarjetajugador addTarjetajugador(Tarjetajugador tarjetajugador) {
		getTarjetajugadors().add(tarjetajugador);
		tarjetajugador.setTarjeta(this);

		return tarjetajugador;
	}

	public Tarjetajugador removeTarjetajugador(Tarjetajugador tarjetajugador) {
		getTarjetajugadors().remove(tarjetajugador);
		tarjetajugador.setTarjeta(null);

		return tarjetajugador;
	}

}