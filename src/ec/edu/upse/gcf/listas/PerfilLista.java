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

import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.modelo.Perfil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PerfilLista {
	
	public String textoBuscar;
	@Wire
	Button btnEditar,btnEliminar;
	
	private PerfilDAO perfilDao = new PerfilDAO();
	private List<Perfil> perfiles;
	private Perfil perfilSeleccionado;

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
	
	@GlobalCommand("Perfiles.buscarPorPatron")
	@Command
	@NotifyChange({"perfiles", "perfilSeleccionado"})
	public void buscar(){
		if (perfiles != null) {
			perfiles = null; 
		}
		perfiles =  perfilDao.getPerfiles(textoBuscar);
		if(perfiles.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			perfilSeleccionado = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Perfil", new Perfil());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/perfiles/perfilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(){
		if(perfilSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		perfilDao.getEntityManager().refresh(perfilSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Perfil", perfilSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/perfiles/perfilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void eliminar(){

		if (perfilSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						perfilDao.getEntityManager().getTransaction().begin();
						perfilSeleccionado.setEstado("1");
						perfilSeleccionado = perfilDao.getEntityManager().merge(perfilSeleccionado);
						perfilDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						BindUtils.postGlobalCommand(null, null, "Categorias.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						perfilDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	//Getter and Setter

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	
	public Perfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
