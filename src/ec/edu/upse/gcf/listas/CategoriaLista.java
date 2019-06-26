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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.modelo.Categoria;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CategoriaLista {
	public String textoBuscar;
	public CategoriaDAO categoriaDao = new CategoriaDAO();
	private List<Categoria> categoria;
	private Categoria categoriaSeleccionada;

	@Wire
	Button btnEditar,btnEliminar;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		deshabilitarBoton();
	}

	private void deshabilitarBoton() {
		btnEditar.setDisabled(true);
		btnEliminar.setDisabled(true);
	}

	@GlobalCommand("Categorias.buscarPorPatron")
	@Command
	@NotifyChange({"categoria", "categoriaSeleccionada."})
	public void buscar(){
		if (categoria != null) {
			categoria = null; 
		}
		categoria = categoriaDao.getCategorias(textoBuscar);
		if(categoria.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			categoriaSeleccionada = null;
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}		
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Categoria", new Categoria());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/categorias/categoriaEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if(categoriaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		categoriaDao.getEntityManager().refresh(categoriaSeleccionada);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Categoria", categoriaSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/categorias/categoriaEditar.zul", null, params);
		ventanaCargar.doModal();
	}


	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (categoriaSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						categoriaDao.getEntityManager().getTransaction().begin();
						categoriaSeleccionada.setEstado("1");
						categoriaSeleccionada = categoriaDao.getEntityManager().merge(categoriaSeleccionada);
						categoriaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Categorias.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						categoriaDao.getEntityManager().getTransaction().rollback();
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

	public List<Categoria> getCategoria() {
		return categoria;
	}

	public void setCategoria(List<Categoria> categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoriaSeleccionada() {
		return categoriaSeleccionada;
	}

	public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
		this.categoriaSeleccionada = categoriaSeleccionada;
	}
}
