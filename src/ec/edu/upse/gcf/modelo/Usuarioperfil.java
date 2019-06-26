package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the usuarioperfil database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Usuarioperfil.findAll", query="SELECT u FROM Usuarioperfil u"),
@NamedQuery(name="UsuarioPerfil.buscaUsuarioRol", query="SELECT u.perfil.idPerfil FROM Usuarioperfil u WHERE u.usuario.usuario = :nombreUsuario"),
@NamedQuery(name="UsuarioPerfil.UsuarioRolClave", query="SELECT u.usuario FROM Usuarioperfil u WHERE u.usuario.usuario = :nombreUsuario"),
@NamedQuery(name="UsuarioPerfil.buscarPorPatron", query="SELECT u FROM Usuarioperfil u WHERE lower(u.usuario.usuario) like lower(:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")public class Usuarioperfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usu_perfil")
	private int idUsuPerfil;

	private String estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Usuarioperfil() {
	}

	public int getIdUsuPerfil() {
		return this.idUsuPerfil;
	}

	public void setIdUsuPerfil(int idUsuPerfil) {
		this.idUsuPerfil = idUsuPerfil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}