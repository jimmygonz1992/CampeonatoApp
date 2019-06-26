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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriajugadorDAO;
import ec.edu.upse.gcf.modelo.Categoriajugador;

public class CategoriajugadorLista {
	public String textoBuscar;
	public CategoriajugadorDAO categoriajugadorDao = new CategoriajugadorDAO();
	private List<Categoriajugador> categoriajugador;
	private Categoriajugador categoriajugadorSeleccionada;

	@Wire
	Button btnEditar,btnEliminar;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}
	

	@GlobalCommand("Categoriajugador.buscarPorPatron")
	@Command
	@NotifyChange({"categoriajugador", "categoriajugadorSeleccionada."})
	public void buscar(){
		if (categoriajugador != null) {
			categoriajugador = null; 
		}
		categoriajugador = categoriajugadorDao.getCategoriajugadores(textoBuscar);
		if(categoriajugador.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			categoriajugadorSeleccionada = null;		
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Categoriajugador", new Categoriajugador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/categoriajugador/asignarCategoria.zul", null, params);
		ventanaCargar.doModal();
	}	


	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Categoriajugador> getCategoriajugador() {
		return categoriajugador;
	}

	public void setCategoriajugador(List<Categoriajugador> categoriajugador) {
		this.categoriajugador = categoriajugador;
	}

	public Categoriajugador getCategoriajugadorSeleccionada() {
		return categoriajugadorSeleccionada;
	}

	public void setCategoriajugadorSeleccionada(Categoriajugador categoriajugadorSeleccionada) {
		this.categoriajugadorSeleccionada = categoriajugadorSeleccionada;
	}

	
}
