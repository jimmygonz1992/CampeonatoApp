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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.ConfiguracionjugadorDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Configuracionjugador;

public class ConfiguracionEditar {
	@Wire private Window winConfiguracionjugador;
	@Wire private Combobox cboCampeonato;
	@Wire private Datebox fechaInicio;
	@Wire private Datebox fechaFin;
	@Wire private Timer horaInicio;
	@Wire private Timer horaFin;
	
	private ConfiguracionjugadorDAO configuracionjugadorDao = new ConfiguracionjugadorDAO();
	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	
	private Configuracionjugador configuracionjugador;
	private Campeonato campeonato;
	private Campeonato campeonatoSeleccionado;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		configuracionjugador = (Configuracionjugador) Executions.getCurrent().getArg().get("Configuracionjugador");	
		campeonato = (Campeonato) Executions.getCurrent().getArg().get("campeonato");
		if(campeonatoSeleccionado != null){
			cboCampeonato.setDisabled(true);	
		}
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
				configuracionjugadorDao.getEntityManager().getTransaction().begin();			
				if (configuracionjugador.getIdConfigJugadores() == 0) {
					configuracionjugadorDao.getEntityManager().persist(configuracionjugador);
				}else{
					configuracionjugador = (Configuracionjugador) configuracionjugadorDao.getEntityManager().merge(configuracionjugador);
				}			
				configuracionjugadorDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Configuracionjugador.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			configuracionjugadorDao.getEntityManager().getTransaction().rollback();
		}		
	}	
	
	@Command
	public void salir(){
		winConfiguracionjugador.detach();
	}
	
	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}

	public Configuracionjugador getConfiguracionjugador() {
		return configuracionjugador;
	}

	public void setConfiguracionjugador(Configuracionjugador configuracionjugador) {
		this.configuracionjugador = configuracionjugador;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}
}
