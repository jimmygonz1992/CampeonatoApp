package ec.edu.upse.gcf.editar;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

public class PartidoEditar {	

	private PartidoEditar partido;
	//private Detallecalendario detallecalendarioSeleccionada;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		partido = (PartidoEditar) Executions.getCurrent().getArg().get("Partido");	
	}
	
	@Command
	public void buscarPartido(){
		System.out.println("buscando ventana");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Detallecalendario", null);
		//Context.getInstance().setGrupoEditar(this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/partidos/buscarPartido.zul", null, params);
		ventanaCargar.doModal();
	}

	public PartidoEditar getPartido() {
		return partido;
	}

	public void setPartido(PartidoEditar partido) {
		this.partido = partido;
	}	
	
}
