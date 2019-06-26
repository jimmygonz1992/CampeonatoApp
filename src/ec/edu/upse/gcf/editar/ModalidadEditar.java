package ec.edu.upse.gcf.editar;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.ModalidadDAO;
import ec.edu.upse.gcf.modelo.Modalidad;

public class ModalidadEditar {

	@Wire private Window winModalidadEditar;
	@Wire private Textbox descripcion;

	private ModalidadDAO modalidadDao = new ModalidadDAO();		
	private Modalidad modalidad;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		modalidad = (Modalidad) Executions.getCurrent().getArg().get("Modalidad");	
	}

	public boolean isValidar() {
		try {
			Boolean retorna = true;
			if(descripcion.getText().isEmpty()) {
				descripcion.focus();
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Command
	public void grabar(){			
		try {				
			if (isValidar() == false) {				
				modalidadDao.getEntityManager().getTransaction().begin();		
				if (modalidad.getIdModalidad() == 0) {					
					modalidadDao.getEntityManager().persist(modalidad);
				}else{
					modalidad = (Modalidad) modalidadDao.getEntityManager().merge(modalidad);
				}
				modalidadDao.getEntityManager().getTransaction().commit();				
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "ModalidadLista.buscar", null);
				salir();
			}else {
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			modalidadDao.getEntityManager().getTransaction().rollback();
		}

	}

	@Command
	public void salir(){
		winModalidadEditar.detach();
	}
	public Modalidad getModalidad() {
		return modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}
}