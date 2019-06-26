package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.DetallecalendarioDAO;
import ec.edu.upse.gcf.modelo.Detallecalendario;

public class DetallecalendarioPLista {

	@Wire private Window winDetalleCEditar;
	@Wire private Listbox lstDetalleC;

	private String textoBuscar;
	private DetallecalendarioDAO detallecalendarioDao = new DetallecalendarioDAO();
	private List<Detallecalendario> detacalendario;
	private Detallecalendario detallecalendarioSeleccionada;	

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";		
	}

	@GlobalCommand("Detallecalendario.buscarPorEquipo")
	@Command
	@NotifyChange({"detacalendario", "detallecalendarioSeleccionada"})
	public void buscar(){
		if (detacalendario != null) {
			detacalendario = null; 
		}
		detacalendario = detallecalendarioDao.getDetallecalendarioEquipo(textoBuscar);
		if(detacalendario.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			detallecalendarioSeleccionada = null; 
		}		
	}	

	@Command
	public void obtenerDato(){
		if(detallecalendarioSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		detallecalendarioDao.getEntityManager().refresh(detallecalendarioSeleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Detallecalendario", detallecalendarioSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/partidos/ingresoResultado.zul", null, params);		
		ventanaCargar.doModal();
		salir();
	}
	

	@Command
	public void salir(){
		winDetalleCEditar.detach();
	}


	//Getter and Setter	

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public DetallecalendarioDAO getDetallecalendarioDao() {
		return detallecalendarioDao;
	}

	public void setDetallecalendarioDao(DetallecalendarioDAO detallecalendarioDao) {
		this.detallecalendarioDao = detallecalendarioDao;
	}

	public List<Detallecalendario> getDetacalendario() {
		return detacalendario;
	}

	public void setDetacalendario(List<Detallecalendario> detacalendario) {
		this.detacalendario = detacalendario;
	}

	public Detallecalendario getDetallecalendarioSeleccionada() {
		return detallecalendarioSeleccionada;
	}

	public void setDetallecalendarioSeleccionada(Detallecalendario detallecalendarioSeleccionada) {
		this.detallecalendarioSeleccionada = detallecalendarioSeleccionada;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

}