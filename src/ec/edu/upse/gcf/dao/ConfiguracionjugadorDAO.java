package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Configuracionjugador;

@SuppressWarnings("unchecked")
public class ConfiguracionjugadorDAO extends ClaseDAO {

	public List<Configuracionjugador> getListaConfiguracionjugadores() {
		List<Configuracionjugador> retorno = new ArrayList<Configuracionjugador>();

		Query query = getEntityManager().createNamedQuery("Configuracionjugador.findAll");
		retorno = (List<Configuracionjugador>) query.getResultList();

		return retorno;
	}		
	
	public List<Configuracionjugador> getConfiguracion(String value) {
		List<Configuracionjugador> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Configuracionjugador.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Configuracionjugador>) query.getResultList();
		return resultado;
	}
	
}
