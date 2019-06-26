package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the institucion database table.
 * 
 */
@Entity
@NamedQuery(name="Institucion.findAll", query="SELECT i FROM Institucion i")
public class Institucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_institucion")
	private int idInstitucion;

	private String afiliada;

	private String correo;

	private String direccion;

	private String estado;

	private String fundacion;

	private String img;

	private String lugar;

	private String nombre;

	private String telefonos;

	public Institucion() {
	}

	public int getIdInstitucion() {
		return this.idInstitucion;
	}

	public void setIdInstitucion(int idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getAfiliada() {
		return this.afiliada;
	}

	public void setAfiliada(String afiliada) {
		this.afiliada = afiliada;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFundacion() {
		return this.fundacion;
	}

	public void setFundacion(String fundacion) {
		this.fundacion = fundacion;
	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLugar() {
		return this.lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefonos() {
		return this.telefonos;
	}

	public void setTelefonos(String telefonos) {
		this.telefonos = telefonos;
	}

}