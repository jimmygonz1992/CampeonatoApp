package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Perfil;
@SuppressWarnings("unchecked")
public class PerfilDAO extends ClaseDAO {
	public List<Perfil> getPerfiles(String value) {
		List<Perfil> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Perfiles.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Perfil>) query.getResultList();
		
		return resultado;
	}
	public Perfil getPerfil(String nombrePerfil) {
		Perfil perfil; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Perfil.buscaPerfil");
		consulta.setParameter("nombrePerfil", nombrePerfil);
		
		perfil = (Perfil) consulta.getSingleResult();
		
		return perfil;
	}
	
	public List<Perfil> getPerfil() {
		List<Perfil> retorno = new ArrayList<Perfil>();  
		Query query = getEntityManager().createNamedQuery("Perfil.findAll");
		retorno = (List<Perfil>) query.getResultList();
		return retorno;
	}
}
