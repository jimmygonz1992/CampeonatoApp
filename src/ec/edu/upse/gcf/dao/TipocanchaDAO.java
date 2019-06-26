package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Tipocancha;

@SuppressWarnings("unchecked")
public class TipocanchaDAO extends ClaseDAO {
	
	public List<Tipocancha> getTipoCanchas(String value) {
		List<Tipocancha> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Tipocancha.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Tipocancha>) query.getResultList();
		System.out.println(resultado.size());
		return resultado;
	}
	public Tipocancha getTipoCancha(String nombreTipocancha) {
		Tipocancha tipocancha; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Tipocancha.buscaTipocancha");
		consulta.setParameter("nombreTipocancha", nombreTipocancha);
		
		tipocancha = (Tipocancha) consulta.getSingleResult();
		
		return tipocancha;
	}
	public List<Tipocancha> getListaTipocanchas() {
		List<Tipocancha> retorno = new ArrayList<Tipocancha>();
		Query query = getEntityManager().createNamedQuery("Tipocancha.findAll");
		retorno = (List<Tipocancha>) query.getResultList();		
		return retorno;
	}
}
