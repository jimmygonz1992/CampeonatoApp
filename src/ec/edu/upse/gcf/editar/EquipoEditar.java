package ec.edu.upse.gcf.editar;

import java.io.IOException;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.util.FileUtil;

public class EquipoEditar {

	@Wire private Window winEquipoEditar;
	@Wire private Textbox nombreEquipo;
	@Wire private Textbox representanteEquipo;

	private EquipoDAO equipoDao = new EquipoDAO();
	private Equipo equipo;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false); 
		equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");	
	}

	public boolean isValida() {
		try {
			Boolean retorna = true;
			if(nombreEquipo.getText().isEmpty()) {
				nombreEquipo.focus();
				return retorna;
			}
			if(representanteEquipo.getText().isEmpty()) {
				representanteEquipo.focus();
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
			if (isValida() == false) {
				equipoDao.getEntityManager().getTransaction().begin();			
				if (equipo.getIdEquipo() == 0) {
					equipoDao.getEntityManager().persist(equipo);
				}else{
					equipo = (Equipo) equipoDao.getEntityManager().merge(equipo);
				}			
				equipoDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Lugarpartido.buscarPorPatron", null);
				salir();				
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			equipoDao.getEntityManager().getTransaction().rollback();
		}		
	}

	@Command
	public void salir(){
		winEquipoEditar.detach();
	}

	@Command
	@NotifyChange("imagenEscudo")
	public void subir(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento){
		String imagen = FileUtil.loadFile(evento.getMedia());
		if (imagen != null) {
			equipo.setEscudo(imagen);
		}	   
	}

	public AImage getImagenEscudo() {
		AImage retorno = null;
		if (equipo.getEscudo() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(equipo.getEscudo()), 163, 163);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
}