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

import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.modelo.Tipocancha;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TipoCanchaLista{

	public TipocanchaDAO tipoCanchaDao = new TipocanchaDAO();
	private Tipocancha tipoCanchaSeleccionada;
	private List<Tipocancha> tipoCancha;
	
	public String textoBuscar;
	
	@Wire Button btnEditar, btnEliminar;

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

	@GlobalCommand("Tipocancha.buscarPorPatron")
	@Command
	@NotifyChange({"tipoCancha", "tipoCanchaSeleccionada."})
	public void buscar(){
		if (tipoCancha != null) {
			tipoCancha = null; 
		}
		tipoCancha = tipoCanchaDao.getTipoCanchas(textoBuscar);
		if(tipoCancha.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			tipoCanchaSeleccionada = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tipocancha", new Tipocancha());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocanchas/tipoCanchaEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(){
		if(tipoCanchaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		tipoCanchaDao.getEntityManager().refresh(tipoCanchaSeleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tipocancha", tipoCanchaSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocanchas/tipoCanchaEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void eliminar(){

		if (tipoCanchaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoCanchaDao.getEntityManager().getTransaction().begin();
						tipoCanchaSeleccionada.setEstado("1");
						tipoCanchaSeleccionada = tipoCanchaDao.getEntityManager().merge(tipoCanchaSeleccionada);
						tipoCanchaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Categorias.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						tipoCanchaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public Tipocancha getTipoCanchaSeleccionada() {
		return tipoCanchaSeleccionada;
	}

	public void setTipoCanchaSeleccionada(Tipocancha tipoCanchaSeleccionada) {
		this.tipoCanchaSeleccionada = tipoCanchaSeleccionada;
	}

	public List<Tipocancha> getTipoCancha() {
		return tipoCancha;
	}

	public void setTipoCancha(List<Tipocancha> tipoCancha) {
		this.tipoCancha = tipoCancha;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	
}
