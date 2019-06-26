package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.ConfiguracioncalendarioDAO;
import ec.edu.upse.gcf.modelo.Configuracion;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfiguracioncalendarioLista {
	public String textoBuscar;

	public ConfiguracioncalendarioDAO configuracioncalendarioDao= new ConfiguracioncalendarioDAO();
	private List<Configuracion> configuracion;
	private Configuracion configuracionseleccionada;
	
	@Wire
	Button btnEditar,btnEliminar;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		deshabilitarBoton();
	}

	private void deshabilitarBoton() {
		btnEditar.setDisabled(true);
		btnEliminar.setDisabled(true);
	}

	@GlobalCommand("Configuracion.buscarPorPatron")
	@Command
	@NotifyChange({"configuracion", "configuracionseleccionada."})
	public void buscar(){
		if (configuracion != null) {
			configuracion = null; 
		}
		configuracion = configuracioncalendarioDao.getConfiguracion(textoBuscar);
		if(configuracion.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			configuracionseleccionada = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Configuracion", new Configuracion());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/configuraciones/configuracionCalendarioEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if(configuracionseleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		configuracioncalendarioDao.getEntityManager().refresh(configuracionseleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("campeonato", campeonatoSeleccionado);
		params.put("Configuracion", configuracionseleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/configuraciones/configuracionCalendarioEditar.zul", null, params);
		ventanaCargar.doModal();
	}


	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (configuracionseleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						configuracioncalendarioDao.getEntityManager().getTransaction().begin();
						configuracionseleccionada.setEstado("1");
						configuracionseleccionada = configuracioncalendarioDao.getEntityManager().merge(configuracionseleccionada);
						configuracioncalendarioDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Configuracionjugador.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						configuracioncalendarioDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public List<Configuracion> getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(List<Configuracion> configuracion) {
		this.configuracion = configuracion;
	}

	public Configuracion getConfiguracionseleccionada() {
		return configuracionseleccionada;
	}

	public void setConfiguracionseleccionada(Configuracion configuracionseleccionada) {
		this.configuracionseleccionada = configuracionseleccionada;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	
}
