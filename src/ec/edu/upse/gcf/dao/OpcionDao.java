package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Opcion;
import ec.edu.upse.gcf.modelo.Perfil;

@SuppressWarnings("unchecked")
public class OpcionDao extends ClaseDAO {
	public List<Opcion> getOpciones(String value) {
		List<Opcion> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Opciones.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Opcion>) query.getResultList();

		return resultado;
	}
	/**
	 * RETORNA LA LISTA DE OPCIONES PADRES  DE ACUERDO AL ROL DEL USUARIO
	 * @param value
	 * @return
	 */
	public List<Opcion> getOpcionPadrePorPerfil(Integer perfil) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcionPorPerfilPadre");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("perfil", perfil);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Opcion> getOpcionPadrePorHijo(Integer idOpcionPadre) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcionPadrePorHijo");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("idOpcionPadre", idOpcionPadre);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Opcion> getOpcionPadreHijo(Integer idOpcionPadre) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcionPadreHijo");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("idOpcionPadre", idOpcionPadre);
			//query.setParameter("perfil", perfil);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * RETORNA LA LISTA DE OPCIONES HIJOS DE ACUERDO AL ROL DEL USUARIO
	 * @param value
	 * @return
	 */
	public List<Opcion> getOpcionHijoPorPerfil(Integer opcionPadre,Integer perfil) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcionPorPerfilHijo");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("opcionPadre", opcionPadre);
			query.setParameter("perfil", perfil);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	public List<Opcion> getOpcion() {
		List<Opcion> retorno = new ArrayList<Opcion>();  
		Query query = getEntityManager().createNamedQuery("Opcion.findAll");
		retorno = (List<Opcion>) query.getResultList();
		return retorno;
	}

	public List<Opcion> getOpcionPadre() {
		List<Opcion> retorno = new ArrayList<Opcion>();  
		Query query = getEntityManager().createNamedQuery("Opcion.opcionPadre");
		retorno = (List<Opcion>) query.getResultList();
		return retorno;
	}

	public List<Opcion> getOpcionesDisponibles(Perfil perfil) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcionesDisponibles");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("perfil", perfil);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Opcion> getOpcDisp(Opcion opc) {
		List<Opcion> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcion.opcDisp");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("opc", opc);
			resultado = (List<Opcion>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Opcion> getOpcionMenu() {
		List<Opcion> retorno = new ArrayList<Opcion>();  
		Query query = getEntityManager().createNamedQuery("Opcion.BuscaOpcionMenu");
		retorno = (List<Opcion>) query.getResultList();
		return retorno;
	}
	
	public List<Opcion> getSubmenu(Integer opcionmenu) {
		List<Opcion> resultado;  
		Query query = getEntityManager().createNamedQuery("Opcion.BuscarSubmenu");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("opcionmenu", opcionmenu);
		resultado = (List<Opcion>) query.getResultList();
		return resultado;
	}
}
