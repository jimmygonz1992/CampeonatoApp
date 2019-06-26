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

import ec.edu.upse.gcf.dao.ConfiguracionjugadorDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Configuracionjugador;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfiguracionjugadorLista {
	public String textoBuscar;

	public ConfiguracionjugadorDAO configuracionjugadorDao= new ConfiguracionjugadorDAO();
	private List<Configuracionjugador> confjugador;
	private Configuracionjugador confjugadorseleccionada;
	private Campeonato campeonatoSeleccionado;

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

	@GlobalCommand("Configuracionjugador.buscarPorPatron")
	@Command
	@NotifyChange({"confjugador", "confjugadorseleccionada."})
	public void buscar(){
		if (confjugador != null) {
			confjugador = null; 
		}
		confjugador = configuracionjugadorDao.getConfiguracion(textoBuscar);
		if(confjugador.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			confjugadorseleccionada = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Configuracionjugador", new Configuracionjugador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/configuraciones/fechainscripcionjugadores.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if(confjugadorseleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		configuracionjugadorDao.getEntityManager().refresh(confjugadorseleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("campeonato", campeonatoSeleccionado);
		params.put("Configuracionjugador", confjugadorseleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/configuraciones/fechainscripcionjugadores.zul", null, params);
		ventanaCargar.doModal();
	}


	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (confjugadorseleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						configuracionjugadorDao.getEntityManager().getTransaction().begin();
						confjugadorseleccionada.setEstado("1");
						confjugadorseleccionada = configuracionjugadorDao.getEntityManager().merge(confjugadorseleccionada);
						configuracionjugadorDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Configuracionjugador.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						configuracionjugadorDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Configuracionjugador> getConfjugador() {
		return confjugador;
	}

	public void setConfjugador(List<Configuracionjugador> confjugador) {
		this.confjugador = confjugador;
	}

	public Configuracionjugador getConfjugadorseleccionada() {
		return confjugadorseleccionada;
	}

	public void setConfjugadorseleccionada(Configuracionjugador confjugadorseleccionada) {
		this.confjugadorseleccionada = confjugadorseleccionada;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}
}
