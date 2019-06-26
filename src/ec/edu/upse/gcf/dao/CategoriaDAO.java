package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Categoria;

@SuppressWarnings("unchecked")
public class CategoriaDAO extends ClaseDAO {
	public List<Categoria> getCategorias(String value) {
		List<Categoria> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Categorias.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	public Categoria getCategoria(String nombreCategoria) {
		Categoria categoria; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Categoria.buscaCategoria");
		consulta.setParameter("nombreCategoria", nombreCategoria);
		categoria = (Categoria) consulta.getSingleResult();
		return categoria;
	}

	public List<Categoria> getListaCategorias() {
		List<Categoria> retorno = new ArrayList<Categoria>();

		Query query = getEntityManager().createNamedQuery("Categoria.findAll");
		retorno = (List<Categoria>) query.getResultList();

		return retorno;
	}	
	
	public List<Categoria> getListaTipoCategorias() {
		List<Categoria> retorno = new ArrayList<Categoria>();

		Query query = getEntityManager().createNamedQuery("Categoria.BuscarTipo");
		retorno = (List<Categoria>) query.getResultList();

		return retorno;
	}	
	

	//Lista las categorias de acuerdo al tipocampeonato seleccionado
	public List<Categoria> getCategoriaTipoCampeonato(String tipo) {
		List<Categoria> resultado;  
		Query query = getEntityManager().createNamedQuery("Categoria.BuscarTipoC");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("tipo", tipo);
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	
}
