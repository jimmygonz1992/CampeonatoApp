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

import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
import ec.edu.upse.gcf.modelo.Detallecampeonato;

public class InscripcionequipoLista {
	public String textoBuscar;
	
	public DetalleCampeonatoDAO detallecampeonatoDao = new DetalleCampeonatoDAO();
	private List<Detallecampeonato> detallecampeonato;
	private Detallecampeonato detallecampeonatoSeleccionado;	

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}	

	@GlobalCommand("Detallecampeonato.buscarPorPatron")
	@Command
	@NotifyChange({"detallecampeonato", "detallecampeonatoSeleccionado."})
	public void buscar(){
		if (detallecampeonato != null) {
			detallecampeonato = null; 
		}
		detallecampeonato = detallecampeonatoDao.getDetalleCampeonatos(textoBuscar);
		if(detallecampeonato.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			detallecampeonatoSeleccionado = null;		
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Detallecampeonato", new Detallecampeonato());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/inscripciones/inscripcionEquipo.zul", null, params);
		ventanaCargar.doModal();
	}	


	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public DetalleCampeonatoDAO getDetallecampeonatoDao() {
		return detallecampeonatoDao;
	}

	public void setDetallecampeonatoDao(DetalleCampeonatoDAO detallecampeonatoDao) {
		this.detallecampeonatoDao = detallecampeonatoDao;
	}

	public List<Detallecampeonato> getDetallecampeonato() {
		return detallecampeonato;
	}

	public void setDetallecampeonato(List<Detallecampeonato> detallecampeonato) {
		this.detallecampeonato = detallecampeonato;
	}

	public Detallecampeonato getDetallecampeonatoSeleccionado() {
		return detallecampeonatoSeleccionado;
	}

	public void setDetallecampeonatoSeleccionado(Detallecampeonato detallecampeonatoSeleccionado) {
		this.detallecampeonatoSeleccionado = detallecampeonatoSeleccionado;
	}
	
}
