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

import ec.edu.upse.gcf.dao.ModalidadDAO;
import ec.edu.upse.gcf.modelo.Modalidad;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class ModalidadLista {

	private String textoBuscar;
	private ModalidadDAO modalidadDao = new ModalidadDAO();
	private List<Modalidad> modalidades;
	private Modalidad modalidadSeleccionado;
	
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

	@GlobalCommand("ModalidadLista.buscar")
	@Command
	@NotifyChange({"modalidades", "modalidadSeleccionado"})
	public void buscar(){
		if (modalidades != null) {
			modalidades = null; 
		}
		modalidades = modalidadDao.getModalidades(textoBuscar);
		if(modalidades.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			modalidadSeleccionado = null; 
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Modalidad", new Modalidad());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/modalidades/modalidadEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(){
		if (modalidadSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una modalidad.");
			return; 
		}		
		modalidadDao.getEntityManager().refresh(modalidadSeleccionado);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Modalidad", modalidadSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/modalidades/modalidadEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void eliminar(){

		if (modalidadSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una modalidad.");
			return; 
		}
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						modalidadDao.getEntityManager().getTransaction().begin();
						modalidadSeleccionado.setEstado("1");
						modalidadSeleccionado = modalidadDao.getEntityManager().merge(modalidadSeleccionado);
						modalidadDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						BindUtils.postGlobalCommand(null, null, "ModalidadLista.buscar", null);
					} catch (Exception e) {
						e.printStackTrace();
						modalidadDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	
	//Getter and Setter

	public List<Modalidad> getModalidades() {
		return modalidades;
	}

	public void setModalidades(List<Modalidad> modalidades) {
		this.modalidades = modalidades;
	}
	public Modalidad getModalidadSeleccionado() {
		return modalidadSeleccionado;
	}

	public void setModalidadSeleccionado(Modalidad modalidadSeleccionado) {
		this.modalidadSeleccionado = modalidadSeleccionado;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

}