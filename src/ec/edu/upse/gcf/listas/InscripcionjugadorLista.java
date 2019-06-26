package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.EquipojugadorDAO;
import ec.edu.upse.gcf.modelo.Equipojugador;

public class InscripcionjugadorLista {
	public String textoBuscar;
	
	public EquipojugadorDAO equipojugadorDao = new EquipojugadorDAO();
	private List<Equipojugador> equipojugador;
	private Equipojugador equipojugadorSeleccionado;	

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}	

	@GlobalCommand("Equipojugador.buscarPorPatron")
	@Command
	@NotifyChange({"equipojugador", "equipojugadorSeleccionado"})
	public void buscar(){
		if (equipojugador != null) {
			equipojugador = null; 
		}
		equipojugador = equipojugadorDao.getEquipojugadores(textoBuscar);
		if(equipojugador.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			equipojugadorSeleccionado = null;		
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipojugador", new Equipojugador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/inscripciones/inscripcionJugador.zul", null, params);
		ventanaCargar.doModal();
	}	


	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public EquipojugadorDAO getEquipojugadorDao() {
		return equipojugadorDao;
	}

	public void setEquipojugadorDao(EquipojugadorDAO equipojugadorDao) {
		this.equipojugadorDao = equipojugadorDao;
	}

	public List<Equipojugador> getEquipojugador() {
		return equipojugador;
	}

	public void setEquipojugador(List<Equipojugador> equipojugador) {
		this.equipojugador = equipojugador;
	}

	public Equipojugador getEquipojugadorSeleccionado() {
		return equipojugadorSeleccionado;
	}

	public void setEquipojugadorSeleccionado(Equipojugador equipojugadorSeleccionado) {
		this.equipojugadorSeleccionado = equipojugadorSeleccionado;
	}	
	
}
