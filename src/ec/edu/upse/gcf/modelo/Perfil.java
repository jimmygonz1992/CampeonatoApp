package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the perfil database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Perfil.findAll", query="SELECT p FROM Perfil p"),
	@NamedQuery(name="Perfiles.buscarPorPatron", 
	query="SELECT p FROM Perfil p WHERE LOWER(p.nombre) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL ")
public class Perfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_perfil")
	private int idPerfil;

	private String descripcion;

	private String estado;

	private String nombre;

	//bi-directional many-to-one association to Opcionperfil
	@OneToMany(mappedBy="perfil")
	private List<Opcionperfil> opcionperfils;

	//bi-directional many-to-one association to Usuarioperfil
	@OneToMany(mappedBy="perfil")
	private List<Usuarioperfil> usuarioperfils;

	public Perfil() {
	}

	public int getIdPerfil() {
		return this.idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
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

	public List<Opcionperfil> getOpcionperfils() {
		return this.opcionperfils;
	}

	public void setOpcionperfils(List<Opcionperfil> opcionperfils) {
		this.opcionperfils = opcionperfils;
	}

	public Opcionperfil addOpcionperfil(Opcionperfil opcionperfil) {
		getOpcionperfils().add(opcionperfil);
		opcionperfil.setPerfil(this);

		return opcionperfil;
	}

	public Opcionperfil removeOpcionperfil(Opcionperfil opcionperfil) {
		getOpcionperfils().remove(opcionperfil);
		opcionperfil.setPerfil(null);

		return opcionperfil;
	}

	public List<Usuarioperfil> getUsuarioperfils() {
		return this.usuarioperfils;
	}

	public void setUsuarioperfils(List<Usuarioperfil> usuarioperfils) {
		this.usuarioperfils = usuarioperfils;
	}

	public Usuarioperfil addUsuarioperfil(Usuarioperfil usuarioperfil) {
		getUsuarioperfils().add(usuarioperfil);
		usuarioperfil.setPerfil(this);

		return usuarioperfil;
	}

	public Usuarioperfil removeUsuarioperfil(Usuarioperfil usuarioperfil) {
		getUsuarioperfils().remove(usuarioperfil);
		usuarioperfil.setPerfil(null);

		return usuarioperfil;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}

}