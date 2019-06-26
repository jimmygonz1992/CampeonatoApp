package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PartidoDAO;
import ec.edu.upse.gcf.modelo.Partido;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class PartidoLista {
	public String textoBuscar;

	private PartidoDAO partidoDao = new PartidoDAO();

	private List<Partido> partidos ;
	private Partido partidoSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}
	
	@GlobalCommand("Partido.buscarPorPatron")
	@Command
	@NotifyChange({"partidos", "partidoSeleccionado."})
	public void buscar(){
		
		if (partidos != null) {
			partidos = null; 
		}
		
		partidos = partidoDao.getPartidos(textoBuscar);
	
		// Limpia os objetos de trabajo
		partidoSeleccionado = null; 
	}
	
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Partido", new Partido());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/partidos/partidoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(){
		if (partidoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opci�n de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		partidoDao.getEntityManager().refresh(partidoSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Partido", partidoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/partidos/partidoEditar.zul", null, params);
		ventanaCargar.doModal();		
	}
	
	@Command
	public void eliminar(){
		
		if (partidoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opci�n de la lista.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						partidoDao.getEntityManager().getTransaction().begin();
						partidoSeleccionado.setEstado("1");
						partidoSeleccionado = partidoDao.getEntityManager().merge(partidoSeleccionado);
						partidoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Partido.buscarPorPatron", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						partidoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Partido> getPartidos() {
		return partidos;
	}

	public void setPartidos(List<Partido> partidos) {
		this.partidos = partidos;
	}

	public Partido getPartidoSeleccionado() {
		return partidoSeleccionado;
	}

	public void setPartidoSeleccionado(Partido partidoSeleccionado) {
		this.partidoSeleccionado = partidoSeleccionado;
	}	
}
