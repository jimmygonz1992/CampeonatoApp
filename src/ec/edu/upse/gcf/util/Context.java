package ec.edu.upse.gcf.util;

import ec.edu.upse.gcf.editar.PartidoEditar;
import ec.edu.upse.gcf.modelo.Detallecalendario;

public class Context {
	private final static Context instance = new Context();
	private PartidoEditar partidoDato;
	private Detallecalendario detallecalendarioSeleccionada;	
		
	private Integer idUsuarioLogeado;
	public static Context getInstance() {
		return instance;
	}

	public Integer getIdUsuarioLogeado() {
		return idUsuarioLogeado;
	}

	public void setIdUsuarioLogeado(Integer idUsuarioLogeado) {
		this.idUsuarioLogeado = idUsuarioLogeado;
	}

	public PartidoEditar getPartidoDato() {
		return partidoDato;
	}

	public void setPartidoDato(PartidoEditar partidoDato) {
		this.partidoDato = partidoDato;
	}

	public Detallecalendario getDetallecalendarioSeleccionada() {
		return detallecalendarioSeleccionada;
	}

	public void setDetallecalendarioSeleccionada(Detallecalendario detallecalendarioSeleccionada) {
		this.detallecalendarioSeleccionada = detallecalendarioSeleccionada;
	}
	
}
