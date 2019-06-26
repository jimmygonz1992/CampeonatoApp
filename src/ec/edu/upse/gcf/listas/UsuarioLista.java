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

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.modelo.Usuario;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UsuarioLista {
	@Wire Button btnEditar,btnEliminar;
	
	public String textoBuscar;
	private UsuarioDao usuarioDao = new UsuarioDao();
	private List<Usuario> usuarios;
	private Usuario usuarioSeleccionado;
	
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

	@GlobalCommand("Usuario.buscaPatron")
	@Command
	@NotifyChange({"usuarios", "Usuario.buscaPatron"})
	public void buscar(){
		if (usuarios != null) {
			usuarios = null; 
		}
		usuarios = usuarioDao.getListaUsuarios(textoBuscar);
		if(usuarios.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			usuarioSeleccionado = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", new Usuario());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/usuarios/usuarioEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if(usuarioSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		usuarioDao.getEntityManager().refresh(usuarioSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", usuarioSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/usuarios/usuarioEditar.zul", null, params);
		ventanaCargar.doModal();
	}


	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (usuarioSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						usuarioDao.getEntityManager().getTransaction().begin();
						usuarioSeleccionado.setEstado("1");
						usuarioSeleccionado = usuarioDao.getEntityManager().merge(usuarioSeleccionado);
						usuarioDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Usuario.buscaPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						usuarioDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	
	//Getter and Setter
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}	
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}