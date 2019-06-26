package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Partido;

@SuppressWarnings("unchecked")
public class PartidoDAO extends ClaseDAO {
	public List<Partido> getPartidos(String value) {
		List<Partido> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Partido.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Partido>) query.getResultList();
		
		return resultado;
	}
	public List<Partido> getPartidos() {
		List<Partido> retorno = new ArrayList<Partido>();  
		Query query = getEntityManager().createNamedQuery("Partido.findAll");
		retorno = (List<Partido>) query.getResultList();
		return retorno;
	}
}
