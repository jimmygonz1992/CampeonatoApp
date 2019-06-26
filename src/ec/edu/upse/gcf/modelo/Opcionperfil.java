package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the opcionperfil database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Opcionperfil.findAll", query="SELECT o FROM Opcionperfil o"),
	@NamedQuery(name="Opcionperfil.buscarPorCodigo", 
	query="SELECT o FROM Opcionperfil o WHERE LOWER(o.idOpcionPerfil) LIKE LOWER(:codigo)"),
	@NamedQuery(name="OpcionPerfil.buscaOpcionPerfil", query="SELECT o.perfil.idPerfil FROM Opcionperfil o WHERE o.opcion.titulo = :nombreOpcion"),
	@NamedQuery(name="Opcionperfil.buscarPorPatron", 
	query="SELECT o FROM Opcionperfil o WHERE LOWER(o.perfil.descripcion) LIKE LOWER(:patron)"),
	@NamedQuery(name="Opcionperfil.BuscarOpcionPerfil", 
	query="SELECT o FROM Opcionperfil o WHERE o.perfil.idPerfil = :perfil and o.opcion.idOpcion = :menu and o.estado IS NULL"),
	@NamedQuery(name="Opcionperfil.opcionPorPerfil", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NULL "
			+ " AND o.idOpcion IN (SELECT a.opcion.idOpcion FROM Opcionperfil a WHERE a.perfil.idPerfil = :perfil) ORDER BY o.titulo"),
	@NamedQuery(name="Opcionperfil.BuscarOpcionIdPerfil", 
	query="SELECT o FROM Opcionperfil o WHERE o.perfil.idPerfil = :perfil")
})
@AdditionalCriteria("this.estado IS NULL")
public class Opcionperfil implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_opcion_perfil")
	private int idOpcionPerfil;

	private String estado;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_opcion")
	private Opcion opcion;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Opcionperfil() {
	}

	public int getIdOpcionPerfil() {
		return this.idOpcionPerfil;
	}

	public void setIdOpcionPerfil(int idOpcionPerfil) {
		this.idOpcionPerfil = idOpcionPerfil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Opcion getOpcion() {
		return this.opcion;
	}

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

}