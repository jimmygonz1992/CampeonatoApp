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

import ec.edu.upse.gcf.dao.LugarPartidoDAO;
import ec.edu.upse.gcf.modelo.Lugarpartido;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LugarPartidoLista {
	public String textoBuscar;
	public LugarPartidoDAO lugarpartidoDao = new LugarPartidoDAO();
	private List<Lugarpartido> lugarpartido;
	private Lugarpartido lugarpartidoSeleccionado;

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

	@GlobalCommand("Lugarpartido.buscarPorPatron")
	@Command
	@NotifyChange({"lugarpartido", "lugarpartidoSeleccionado."})
	public void buscar(){
		if (lugarpartido != null) {
			lugarpartido = null; 
		}
		lugarpartido = lugarpartidoDao.getLugarpartido(textoBuscar);
		if(lugarpartido.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			lugarpartidoSeleccionado = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Lugarpartido", new Lugarpartido());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/lugarpartido/lugarPartidoEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if(lugarpartidoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		lugarpartidoDao.getEntityManager().refresh(lugarpartidoSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Lugarpartido", lugarpartidoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/lugarpartido/lugarPartidoEditar.zul", null, params);
		ventanaCargar.doModal();
	}


	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (lugarpartidoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						lugarpartidoDao.getEntityManager().getTransaction().begin();
						lugarpartidoSeleccionado.setEstado("1");
						lugarpartidoSeleccionado = lugarpartidoDao.getEntityManager().merge(lugarpartidoSeleccionado);
						lugarpartidoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Lugarpartido.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						lugarpartidoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	//Getter and Setter
	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Lugarpartido> getLugarpartido() {
		return lugarpartido;
	}

	public void setLugarpartido(List<Lugarpartido> lugarpartido) {
		this.lugarpartido = lugarpartido;
	}

	public Lugarpartido getLugarpartidoSeleccionado() {
		return lugarpartidoSeleccionado;
	}

	public void setLugarpartidoSeleccionado(Lugarpartido lugarpartidoSeleccionado) {
		this.lugarpartidoSeleccionado = lugarpartidoSeleccionado;
	}

}
