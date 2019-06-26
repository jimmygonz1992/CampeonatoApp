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

import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.modelo.Tarjeta;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TarjetaLista {
	public String textoBuscar;
	public TarjetaDAO tarjetaDao = new TarjetaDAO();
	private List<Tarjeta> tarjeta;
	private Tarjeta tarjetaSeleccionada;

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

	@GlobalCommand("Tarjetas.buscarPorPatron")
	@Command
	@NotifyChange({"tarjeta", "tarjetaSeleccionada."})
	public void buscar(){
		if (tarjeta != null) {
			tarjeta = null; 
		}
		tarjeta = tarjetaDao.getTarjetas(textoBuscar);
		if(tarjeta.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			tarjetaSeleccionada = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjeta", new Tarjeta());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tarjetas/tarjetaEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(){
		if(tarjetaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		tarjetaDao.getEntityManager().refresh(tarjetaSeleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjeta", tarjetaSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tarjetas/tarjetaEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void eliminar(){

		if (tarjetaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tarjetaDao.getEntityManager().getTransaction().begin();
						tarjetaSeleccionada.setEstado("1");
						tarjetaSeleccionada = tarjetaDao.getEntityManager().merge(tarjetaSeleccionada);
						tarjetaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Categorias.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						tarjetaDao.getEntityManager().getTransaction().rollback();
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

	public List<Tarjeta> getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(List<Tarjeta> tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Tarjeta getTarjetaSeleccionada() {
		return tarjetaSeleccionada;
	}

	public void setTarjetaSeleccionada(Tarjeta tarjetaSeleccionada) {
		this.tarjetaSeleccionada = tarjetaSeleccionada;
	}	
}
