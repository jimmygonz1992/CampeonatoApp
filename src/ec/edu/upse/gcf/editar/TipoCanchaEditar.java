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

import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.modelo.Tipocancha;

public class TipoCanchaEditar {

	@Wire private Window winTipoCanchaEditar;
	@Wire private Textbox descripcion;

	private TipocanchaDAO tipoCanchaDao = new TipocanchaDAO();
	private Tipocancha tipoCancha;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		tipoCancha = (Tipocancha) Executions.getCurrent().getArg().get("Tipocancha");	
	}
	
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
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
				tipoCanchaDao.getEntityManager().getTransaction().begin();			
				if (tipoCancha.getId_tipoC() == 0) {
					tipoCanchaDao.getEntityManager().persist(tipoCancha);
				}else{
					tipoCancha = (Tipocancha) tipoCanchaDao.getEntityManager().merge(tipoCancha);
				}			
				tipoCanchaDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Tipocancha.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tipoCanchaDao.getEntityManager().getTransaction().rollback();
		}		
	}	
	
	@Command
	public void salir(){
		winTipoCanchaEditar.detach();
	}

	public Tipocancha getTipoCancha() {
		return tipoCancha;
	}

	public void setTipoCancha(Tipocancha tipoCancha) {
		this.tipoCancha = tipoCancha;
	}	
}