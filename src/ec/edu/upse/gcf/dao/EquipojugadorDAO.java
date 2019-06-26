package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Equipojugador;

@SuppressWarnings("unchecked")
public class EquipojugadorDAO extends ClaseDAO {
	public List<Equipojugador> getJugadorEquipoCampeonato(Integer campeonato, Integer equipo) {
		List<Equipojugador> resultado;  
		Query query = getEntityManager().createNamedQuery("Equipojugador.BuscarJugadorEquipoCampeonato");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("campeonato", campeonato);
		query.setParameter("equipo", equipo);
		resultado = (List<Equipojugador>) query.getResultList();
		return resultado;
	}
	
	public List<Equipojugador> getJugadorEquipoCampeonato(String campeonato, String equipo) {
		List<Equipojugador> resultado;  
		Query query = getEntityManager().createNamedQuery("Equipojugador.BuscarJugadorEquipouno");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("campeonato", campeonato);
		query.setParameter("equipo", equipo);
		resultado = (List<Equipojugador>) query.getResultList();
		return resultado;
	}
	
	public List<Equipojugador> getJugadorEquipoDos(String campeonato, String equipo) {
		List<Equipojugador> resultado;  
		Query query = getEntityManager().createNamedQuery("Equipojugador.BuscarJugadorEquipodos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("campeonato", campeonato);
		query.setParameter("equipo", equipo);
		resultado = (List<Equipojugador>) query.getResultList();
		return resultado;
	}
	
	public Equipojugador getJugadorGuarda(Integer campeonato,Integer equipo,Integer jugador) { 
		try {  
			Query query = getEntityManager().createNamedQuery("Equipojugador.BuscarJugadorGuarda");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("campeonato", campeonato);
			query.setParameter("equipo", equipo);	
			query.setParameter("jugador", jugador);	 
			return (Equipojugador) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}

	}
	
	public List<Equipojugador> getEquipojugadores(String value) {
		List<Equipojugador> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Equipojugador.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Equipojugador>) query.getResultList();		
		return resultado;
	}	
}
