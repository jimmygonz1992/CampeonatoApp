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

import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.modelo.Tarjeta;

public class TarjetaEditar {

	@Wire private Window winTarjetaEditar;
	@Wire private Textbox descripcion;

	private TarjetaDAO tarjetaDao = new TarjetaDAO();
	private Tarjeta tarjeta;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		tarjeta = (Tarjeta) Executions.getCurrent().getArg().get("Tarjeta");	
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
				tarjetaDao.getEntityManager().getTransaction().begin();			
				if (tarjeta.getIdTarjeta() == 0) {
					tarjetaDao.getEntityManager().persist(tarjeta);
				}else{
					tarjeta = (Tarjeta) tarjetaDao.getEntityManager().merge(tarjeta);
				}			
				tarjetaDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Tarjetas.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tarjetaDao.getEntityManager().getTransaction().rollback();
		}		
	}	

	@Command
	public void salir(){
		winTarjetaEditar.detach();
	}

	public Tarjeta getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}
}