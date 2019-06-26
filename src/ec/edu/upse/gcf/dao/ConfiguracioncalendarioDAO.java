package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Configuracion;

@SuppressWarnings("unchecked")
public class ConfiguracioncalendarioDAO extends ClaseDAO {

	public List<Configuracion> getListaConfiguracioncalendario() {
		List<Configuracion> retorno = new ArrayList<Configuracion>();

		Query query = getEntityManager().createNamedQuery("Configuracion.findAll");
		retorno = (List<Configuracion>) query.getResultList();

		return retorno;
	}		
	
	public List<Configuracion> getConfiguracion(String value) {
		List<Configuracion> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Configuracion.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Configuracion>) query.getResultList();
		return resultado;
	}
	
}
