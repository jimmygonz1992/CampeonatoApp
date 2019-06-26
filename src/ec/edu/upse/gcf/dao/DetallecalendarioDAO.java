package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Detallecalendario;

@SuppressWarnings("unchecked")
public class DetallecalendarioDAO extends ClaseDAO{
	
	public List<Detallecalendario> getDetalleCalendarios(String value) {
		List<Detallecalendario> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Detallecalendario.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Detallecalendario>) query.getResultList();		
		return resultado;
	}		
	
	public List<Detallecalendario> getListaDetalleCalendarios() {
		List<Detallecalendario> retorno = new ArrayList<Detallecalendario>();
		Query query = getEntityManager().createNamedQuery("Detallecalendario.findAll");		
		retorno = (List<Detallecalendario>) query.getResultList();
		return retorno;
	}
	
	public List<Detallecalendario> getDetallecalendarioEquipo(String value) {
		List<Detallecalendario> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Detallecalendario.buscarPorEquipo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Detallecalendario>) query.getResultList();		
		return resultado;
	}		
	
}
