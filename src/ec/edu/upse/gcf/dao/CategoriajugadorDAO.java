package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Categoriajugador;

@SuppressWarnings("unchecked")
public class CategoriajugadorDAO extends ClaseDAO{
	public List<Categoriajugador> getJugadorCategoria(Integer categoria) {
		List<Categoriajugador> resultado;  
		Query query = getEntityManager().createNamedQuery("Categoriajugador.BuscarJugadorIdCategoria");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("categoria", categoria);
		resultado = (List<Categoriajugador>) query.getResultList();
		return resultado;
	}

	public Categoriajugador getJugador(Integer categoria,Integer jugador) { 
		try {  
			Query query = getEntityManager().createNamedQuery("Categoriajugador.BuscarJugadorCategoria");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("categoria", categoria);
			query.setParameter("jugador", jugador);	 
			return (Categoriajugador) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}

	}
	
	public List<Categoriajugador> getCategoriaJugadorC() {
		List<Categoriajugador> retorno = new ArrayList<Categoriajugador>();  
		Query query = getEntityManager().createNamedQuery("Categoriajugador.findAll");
		retorno = (List<Categoriajugador>) query.getResultList();
		return retorno;
	}
	
	public List<Categoriajugador> getCategoriajugadores(String value) {
		List<Categoriajugador> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Categoriajugador.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Categoriajugador>) query.getResultList();
		return resultado;
	}
}
