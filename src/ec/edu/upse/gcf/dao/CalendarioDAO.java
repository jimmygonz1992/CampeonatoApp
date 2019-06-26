package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Calendario; 

@SuppressWarnings("unchecked")
public class CalendarioDAO extends ClaseDAO{

	public List<Calendario> getListaCalendario() {
		List<Calendario> resultado; 
		Query query = getEntityManager().createNamedQuery("Calendario.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Calendario>) query.getResultList();
		return resultado;
	}
	
}
