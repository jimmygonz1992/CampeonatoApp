package ec.edu.upse.gcf.editar;

import java.util.List;

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

import ec.edu.upse.gcf.dao.LugarPartidoDAO;
import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.modelo.Lugarpartido;
import ec.edu.upse.gcf.modelo.Tipocancha;

public class LugarPartidoEditar {
	@Wire private Textbox descripcion;
	@Wire private Window winLugarPartidoEditar;

	private LugarPartidoDAO lugarpartidoDao = new LugarPartidoDAO();
	private Lugarpartido lugarpartido;	
	private TipocanchaDAO tipocanchaDao = new TipocanchaDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		lugarpartido = (Lugarpartido) Executions.getCurrent().getArg().get("Lugarpartido");	
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
				lugarpartidoDao.getEntityManager().getTransaction().begin();			
				if (lugarpartido.getId_lugarP() == 0) {
					lugarpartidoDao.getEntityManager().persist(lugarpartido);
				}else{
					lugarpartido = (Lugarpartido) lugarpartidoDao.getEntityManager().merge(lugarpartido);
				}			
				lugarpartidoDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Lugarpartido.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lugarpartidoDao.getEntityManager().getTransaction().rollback();
		}		
	}

	@Command
	public void salir(){
		winLugarPartidoEditar.detach();
	}

	public Lugarpartido getLugarpartido() {
		return lugarpartido;
	}

	public void setLugarpartido(Lugarpartido lugarpartido) {
		this.lugarpartido = lugarpartido;
	}

	public List<Tipocancha> getTipocanchas() {
		return tipocanchaDao.getListaTipocanchas(); 
	}
}
