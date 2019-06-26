package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.modelo.Jugador;


@SuppressWarnings("unchecked")
public class JugadorDAO extends ClaseDAO {
	public List<Jugador> getJugadores(String value) {
		List<Jugador> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Jugadores.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Jugador>) query.getResultList();		
		return resultado;
	}
	public Jugador getJugador(String nombreJugador) {
		Jugador jugador; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Jugador.buscaJugador");
		consulta.setParameter("nombreJugador", nombreJugador);
		
		jugador = (Jugador) consulta.getSingleResult();
		
		return jugador;
	}
	
	public List<Jugador> getJugador() {
		List<Jugador> retorno = new ArrayList<Jugador>();  
		Query query = getEntityManager().createNamedQuery("Jugador.findAll");
		retorno = (List<Jugador>) query.getResultList();
		return retorno;
	}
	
	/**Consulta para verificar si existe ususario a traves de la c�dula */
	public Jugador getValidarJugadorExistente(String cedulaJugador) {
		Jugador resultado; 
		Query query = getEntityManager().createNamedQuery("Jugador.validaJugadorExistente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedulaJugador", cedulaJugador);
		resultado = (Jugador) query.getSingleResult();
		return resultado;
	}
	
	/*CONSULTA JUGADORES DISPONIBLES*/
	public List<Jugador> getJugadoresDisponibles(Categoria categoria) {
		List<Jugador> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Jugador.jugadoresDisponibles");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("categoria", categoria);
			resultado = (List<Jugador>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Jugador> getJugadoresDisponiblesEquipoCampeonatos(Campeonato campeonato, Equipo equipo) {
		List<Jugador> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Jugador.jugadoresDisponiblesEquipoCampeonatos");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("campeonato", campeonato);
			query.setParameter("equipo", equipo);
			resultado = (List<Jugador>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
