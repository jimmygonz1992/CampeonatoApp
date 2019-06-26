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

import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.modelo.Perfil;

public class PerfilEditar {

	@Wire private Window winPerfilEditar;
	@Wire private Textbox nombre;
	@Wire private Textbox descripcion;

	private PerfilDAO perfilDao = new PerfilDAO();
	private Perfil perfil;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		perfil = (Perfil) Executions.getCurrent().getArg().get("Perfil");	
	}
		

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(nombre.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese el nombre de la categor�a.");
				nombre.focus();
				return retorna;
			}
			if(descripcion.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese la descripci�n de la categor�a.");
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
			if (isValidarDatos() == false) {
				perfilDao.getEntityManager().getTransaction().begin();			
				if (perfil.getIdPerfil() == 0) {
					perfilDao.getEntityManager().persist(perfil);
				}else{
					perfil = (Perfil) perfilDao.getEntityManager().merge(perfil);
				}			
				perfilDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Perfiles.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			perfilDao.getEntityManager().getTransaction().rollback();
		}		
	}	

	@Command
	public void salir(){
		winPerfilEditar.detach();
	}


	public Perfil getPerfil() {
		return perfil;
	}


	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}	
}