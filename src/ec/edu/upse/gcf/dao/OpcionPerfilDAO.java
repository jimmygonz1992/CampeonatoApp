package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Opcionperfil;

@SuppressWarnings("unchecked")
public class OpcionPerfilDAO extends ClaseDAO{
	public List<Opcionperfil> getOpcionPerfiles(String value) {
		List<Opcionperfil> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Opcionperfil.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Opcionperfil>) query.getResultList();
		return resultado;
	}
	public Opcionperfil getOpcionPerfil(String nombreOpcionPerfil) {
		Opcionperfil opcionperfil; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Opcionperfil.buscaOpcionPerfil");
		consulta.setParameter("nombreOpcionPerfil", nombreOpcionPerfil);
		
		opcionperfil = (Opcionperfil) consulta.getSingleResult();
		
		return opcionperfil;
	}
	
	public Integer getOpcionPorPerfil(String nombreOpcion) {
		Integer resultado = 0;
		Query consulta = null;
		try {
			consulta = getEntityManager().createNamedQuery("OpcionPerfil.buscaOpcionPerfil");
			consulta.setParameter("nombreOpcion", nombreOpcion);
			resultado = (Integer) consulta.getSingleResult();		
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public List<Opcionperfil> getOpcionperfiles(Integer value) {
		List<Opcionperfil> resultado; 
		Integer codigo = value;	
		Query query = getEntityManager().createNamedQuery("Opcionperfil.buscarPorCodigo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		resultado = (List<Opcionperfil>) query.getResultList();
		return resultado;
	}
	
	public Opcionperfil getAcceso(Integer perfil,Integer opcion) { 
		try {  
			Query query = getEntityManager().createNamedQuery("Opcionperfil.BuscarOpcionPerfil");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("perfil", perfil);
			query.setParameter("menu", opcion);	 
			return (Opcionperfil) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
		
	}
	
	public List<Opcionperfil> getAccesoPerfil(Integer perfil) {
		List<Opcionperfil> resultado;  
		Query query = getEntityManager().createNamedQuery("Opcionperfil.BuscarOpcionIdPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("perfil", perfil);
		resultado = (List<Opcionperfil>) query.getResultList();
		return resultado;
	}
	
	public List<Opcionperfil> getOpcionesPorPerfiles(Integer id) {
		List<Opcionperfil> resultado; 
		try {			
			Query query = getEntityManager().createNamedQuery("Opcionperfil.opcionPorPerfil");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("perfil", id);
			resultado = (List<Opcionperfil>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
