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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.modelo.Categoria;

public class CategoriaEditar {

	@Wire private Window winCategoriaEditar;
	@Wire private Textbox nombre;
	@Wire private Textbox descripcion;
	@Wire private Combobox cboTipoC;
	
	private CategoriaDAO categoriaDao = new CategoriaDAO();
	private Categoria categoria;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		categoria = (Categoria) Executions.getCurrent().getArg().get("Categoria");	
	}
	
	public List<String> getTipoCategoria() {		
		List<String> tipoCategoria = new ArrayList<String>();	
		tipoCategoria.add("F");				
		tipoCategoria.add("M");   
		return tipoCategoria;					
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(nombre.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese el nombre de la categor�a.");
				nombre.focus();
				return retorna;
			}
			if(descripcion.getText().isEmpty()) {
				//Clients.showNotification("Por ingrese la descripci�n de la categor�a.");
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
			if (isValidarDatos() == false) {
				categoriaDao.getEntityManager().getTransaction().begin();			
				if (categoria.getIdCategoria() == 0) {
					categoriaDao.getEntityManager().persist(categoria);
				}else{
					categoria = (Categoria) categoriaDao.getEntityManager().merge(categoria);
				}			
				categoriaDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				BindUtils.postGlobalCommand(null, null, "Categorias.buscarPorPatron", null);
				salir();
			}else {				
				Clients.showNotification("No hay datos para guardar.!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			categoriaDao.getEntityManager().getTransaction().rollback();
		}		
	}	

	@Command
	public void salir(){
		winCategoriaEditar.detach();
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}