package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Lugarpartido;

@SuppressWarnings("unchecked")
public class LugarPartidoDAO  extends ClaseDAO {

	
	public List<Lugarpartido> getLugarEquipo() {
		List<Lugarpartido> retorno = new ArrayList<Lugarpartido>();
		Query query = getEntityManager().createNamedQuery("Lugarpartido.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		retorno = (List<Lugarpartido>) query.getResultList();		
		return retorno;
	}
	
	public List<Lugarpartido> getLugarpartido(String value) {
		List<Lugarpartido> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Lugarpartido.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Lugarpartido>) query.getResultList();
		System.out.println(resultado.size());
		return resultado;
	}
}
