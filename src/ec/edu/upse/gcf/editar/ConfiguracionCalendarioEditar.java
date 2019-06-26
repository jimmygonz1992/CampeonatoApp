package ec.edu.upse.gcf.editar;

import java.util.ArrayList;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.ConfiguracioncalendarioDAO;
import ec.edu.upse.gcf.modelo.Configuracion;

public class ConfiguracionCalendarioEditar {
	@Wire private Window winConfiguracion;
	@Wire private Combobox cboCampeonato;
	@Wire private Datebox fechaInicio;
	@Wire private Datebox fechaFin;
	@Wire private Timer horaInicio;
	@Wire private Timer horaFin;
	
	private ConfiguracioncalendarioDAO configuracionDao = new ConfiguracioncalendarioDAO();	
	private Configuracion configuracion;	
	private Configuracion configuracionSeleccionado;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		configuracion = (Configuracion) Executions.getCurrent().getArg().get("Configuracion");			
	}
	
	public List<String> getDiaInicio() {		
		List<String> dias = new ArrayList<String>();	
		dias.add("LUNES");
		dias.add("MARTES");
		dias.add("MIERCOLES");
		dias.add("JUEVES");
		dias.add("VIERNES");
		return dias;					
	}
	
	public List<String> getDiaFin() {		
		List<String> dias = new ArrayList<String>();	
		dias.add("LUNES");
		dias.add("MARTES");
		dias.add("MIERCOLES");
		dias.add("JUEVES");
		dias.add("VIERNES");
		return dias;					
	}
	
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(cboCampeonato.getValue().isEmpty()) {
				//Clients.showNotification("Por ingrese el nombre de la categoría.");
				cboCampeonato.focus();
				return retorna;
			}
			if(fechaInicio.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese la descripción de la categoría.");
				fechaInicio.focus();
				return retorna;
			}
			if(fechaFin.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese la descripción�n de la categor�a.");
				fechaFin.focus();
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
				configuracionDao.getEntityManager().getTransaction().begin();			
				if (configuracion.getIdConfiguracion() == 0) {
					configuracionDao.getEntityManager().persist(configuracion);
				}else{
					configuracion = (Configuracion) configuracionDao.getEntityManager().merge(configuracion);
				}			
				configuracionDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Configuracion.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			configuracionDao.getEntityManager().getTransaction().rollback();
		}		
	}	
	
	@Command
	public void salir(){
		winConfiguracion.detach();
	}
	
	public Configuracion getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}

	public Configuracion getConfiguracionSeleccionado() {
		return configuracionSeleccionado;
	}

	public void setConfiguracionSeleccionado(Configuracion configuracionSeleccionado) {
		this.configuracionSeleccionado = configuracionSeleccionado;
	}
}
