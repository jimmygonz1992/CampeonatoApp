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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;

public class CampeonatoEditar {
	@Wire private Window winCampeonatoEditar;
	@Wire private Combobox tipoCampeonato;
	@Wire private Textbox nombreC;
	@Wire private Datebox fechaInicio;

	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	private Campeonato campeonato;

	private CategoriaDAO categoriaDao = new CategoriaDAO();
	private Categoria categoriaseleccionada;
	private Categoria categoria;

	private List<Categoria> listacategoria = new ArrayList<>();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);		
		campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");	
		categoria = (Categoria) Executions.getCurrent().getArg().get("Categoria");
	}

	public List<String> getEstado() {		
		List<String> estado = new ArrayList<String>();	
		estado.add("F");
		estado.add("D");
		return estado;					
	}

	public List<String> getTipoCampeonato() {		
		List<String> tipoCampeonato = new ArrayList<String>();	
		tipoCampeonato.add("F");
		tipoCampeonato.add("M");
		return tipoCampeonato;					
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(nombreC.getText().isEmpty()) {
				nombreC.focus();
				return retorna;
			}
			if(tipoCampeonato.getValue().isEmpty()) {
				tipoCampeonato.focus();
				return retorna;
			}
			if(fechaInicio.getText().isEmpty()) {
				fechaInicio.focus();
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			if(isValidarDatos() == false) {	
				campeonatoDao.getEntityManager().getTransaction().begin();
				if (campeonato.getIdCampeonato() == 0) {
					campeonatoDao.getEntityManager().persist(campeonato);
				}else{
					campeonato = (Campeonato) campeonatoDao.getEntityManager().merge(campeonato);
				}
				campeonatoDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");	
				BindUtils.postGlobalCommand(null, null, "Campeonatos.buscarPorPatron", null);
				salir();
			}else {
				Clients.showNotification("Por favor llenes los campos obligatorios '*'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			campeonatoDao.getEntityManager().getTransaction().rollback();
		}		
	}	

	@Command
	public void salir(){
		winCampeonatoEditar.detach();
	}

	public List<Categoria> getCategorias() {
		return categoriaDao.getListaCategorias();
	}

	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}


	public Categoria getCategoriaseleccionada() {
		return categoriaseleccionada;
	}

	public void setCategoriaseleccionada(Categoria categoriaseleccionada) {
		this.categoriaseleccionada = categoriaseleccionada;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public CampeonatoDAO getCampeonatoDao() {
		return campeonatoDao;
	}

	public void setCampeonatoDao(CampeonatoDAO campeonatoDao) {
		this.campeonatoDao = campeonatoDao;
	}

	public CategoriaDAO getCategoriaDao() {
		return categoriaDao;
	}

	public void setCategoriaDao(CategoriaDAO categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

	public List<Categoria> getListacategoria() {
		return listacategoria;
	}

	public void setListacategoria(List<Categoria> listacategoria) {
		this.listacategoria = listacategoria;
	}

}