package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Equipo;


@SuppressWarnings("unchecked")
public class EquipoDAO extends ClaseDAO {
	public List<Equipo> getEquipos(String value) {
		List<Equipo> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Equipos.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Equipo>) query.getResultList();		
		return resultado;
	}	
	public Equipo getEquipo(String nombreEquipo) {
		Equipo equipo; 
		Query consulta;		
		consulta = getEntityManager().createNamedQuery("Equipo.buscaPerfil");
		consulta.setParameter("nombreEquipo", nombreEquipo);		
		equipo = (Equipo) consulta.getSingleResult();		
		return equipo;
	}
	public List<Equipo> getListaEquipos() {
		List<Equipo> retorno = new ArrayList<Equipo>();
		Query query = getEntityManager().createNamedQuery("Equipo.findAll");
		retorno = (List<Equipo>) query.getResultList();		
		return retorno;
	}	
	public List<Equipo> getEquiposDisponibles(Campeonato campeonato, Categoria categoria) {
		List<Equipo> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Equipo.equiposDisponibles");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("campeonato", campeonato);
			query.setParameter("categoria", categoria);
			resultado = (List<Equipo>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
