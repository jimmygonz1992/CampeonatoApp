package ec.edu.upse.gcf.listas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.OpcionPerfilDAO;
import ec.edu.upse.gcf.modelo.Opcionperfil;

public class OpcionPerfilLista {
	public String textoBuscar;

	private OpcionPerfilDAO opcionPerfilDao = new OpcionPerfilDAO();

	private Opcionperfil opcionperfilSeleccionada;
	private List<Opcionperfil> opcionperfiles;
	
	@GlobalCommand("Opcionperfil.buscarPorPatron")
	@Command
	@NotifyChange({"opcionperfiles", "opcionperfilSeleccionada"})
	public void buscar(){

		if (opcionperfiles != null) {
			opcionperfiles = null; 
		}
		opcionperfiles = opcionPerfilDao.getOpcionPerfiles(textoBuscar);

		// Limpia os objetos de trabajo
		opcionperfilSeleccionada = null; 
		opcionperfilSeleccionada = null; 
	}
	
	/**
	 * Crea nuevo
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Opcionperfil", new Opcionperfil());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/accesos/opcperfilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	/**
	 * Edita
	 */
	@Command
	public void editar(){
		if (opcionperfilSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		opcionPerfilDao.getEntityManager().refresh(opcionperfilSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Opcionperfil", opcionperfilSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/accesos/opcionperfilEditar.zul", null, params);
		ventanaCargar.doModal();

	}
	/**
	 * Borra "logicamente" un registro
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(){

		if (opcionperfilSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "ConfirmaciÃ³n de EliminaciÃ³n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						opcionPerfilDao.getEntityManager().getTransaction().begin();
						opcionperfilSeleccionada.setEstado("1");
						opcionperfilSeleccionada = opcionPerfilDao.getEntityManager().merge(opcionperfilSeleccionada);
						opcionPerfilDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "OpcionPerfilLista.buscar", null);

					} catch (Exception e) {
						e.printStackTrace();
						opcionPerfilDao.getEntityManager().getTransaction().rollback();
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

	public Opcionperfil getOpcionperfilSeleccionada() {
		return opcionperfilSeleccionada;
	}

	public void setOpcionperfilSeleccionada(Opcionperfil opcionperfilSeleccionada) {
		this.opcionperfilSeleccionada = opcionperfilSeleccionada;
	}

	public List<Opcionperfil> getOpcionperfiles() {
		return opcionperfiles;
	}

	public void setOpcionperfiles(List<Opcionperfil> opcionperfiles) {
		this.opcionperfiles = opcionperfiles;
	}	
}
