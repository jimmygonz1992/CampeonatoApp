package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Modalidad;

@SuppressWarnings("unchecked")
public class ModalidadDAO extends ClaseDAO {
	public List<Modalidad> getModalidades(String value) {
		List<Modalidad> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Modalidades.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Modalidad>) query.getResultList();
		
		return resultado;
	}
	
	public List<Modalidad> getListaModalidad() {
		List<Modalidad> resultado; 
		Query query = getEntityManager().createNamedQuery("Modalidad.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH"); 
		resultado = (List<Modalidad>) query.getResultList();		
		return resultado;
	}
	
	public Modalidad getModalidad(String nombreModalidad) {
		Modalidad modalidad; 
		Query consulta;		
		consulta = getEntityManager().createNamedQuery("Modalidad.buscaModalidad");
		consulta.setParameter("nombreModalidad", nombreModalidad);
		
		modalidad = (Modalidad) consulta.getSingleResult();
		
		return modalidad;
	}
}
