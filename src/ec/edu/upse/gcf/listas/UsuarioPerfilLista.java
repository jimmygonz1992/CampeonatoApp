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

import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Usuarioperfil;

public class UsuarioPerfilLista {
	public String textoBuscar;	
	private UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();	
	private Usuarioperfil usuarioperfilSeleccionada;
	private List<Usuarioperfil> usuarioperfiles;
	
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
	
	@GlobalCommand("UsuarioPerfilLista.buscar")
	@Command
	@NotifyChange({"usuarioperfiles", "usuarioperfilSeleccionada"})
	public void buscar(){

		if (usuarioperfiles != null) {
			usuarioperfiles = null; 
		}
		usuarioperfiles = usuarioPerfilDao.getUsuarioPerfiles(textoBuscar);
		if(usuarioperfiles.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
		usuarioperfilSeleccionada = null; 
		btnEditar.setDisabled(false);
		btnEliminar.setDisabled(false);
	}
	
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuarioperfil", new Usuarioperfil());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/accesos/usuarioperfilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(){
		if (usuarioperfilSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opci�n de la lista.");
			return; 
		}
		usuarioPerfilDao.getEntityManager().refresh(usuarioperfilSeleccionada);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuarioperfil", usuarioperfilSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/accesos/usuarioperfilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(){
		if (usuarioperfilSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opci�n de la lista.");
			return; 
		}
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						usuarioPerfilDao.getEntityManager().getTransaction().begin();
						usuarioperfilSeleccionada.setEstado("1");
						usuarioperfilSeleccionada = usuarioPerfilDao.getEntityManager().merge(usuarioperfilSeleccionada);
						usuarioPerfilDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						BindUtils.postGlobalCommand(null, null, "UsuarioPerfilLista.buscar", null);
					} catch (Exception e) {
						e.printStackTrace();
						usuarioPerfilDao.getEntityManager().getTransaction().rollback();
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
	public Usuarioperfil getUsuarioperfilSeleccionada() {
		return usuarioperfilSeleccionada;
	}
	public void setUsuarioperfilSeleccionada(Usuarioperfil usuarioperfilSeleccionada) {
		this.usuarioperfilSeleccionada = usuarioperfilSeleccionada;
	}
	public List<Usuarioperfil> getUsuarioperfiles() {
		return usuarioperfiles;
	}
	public void setUsuarioperfiles(List<Usuarioperfil> usuarioperfiles) {
		this.usuarioperfiles = usuarioperfiles;
	}
	
}
