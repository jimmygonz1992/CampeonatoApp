package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Tarjeta;

@SuppressWarnings("unchecked")
public class TarjetaDAO extends ClaseDAO {
	public List<Tarjeta> getTarjetas(String value) {
		List<Tarjeta> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Tarjetas.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Tarjeta>) query.getResultList();
		return resultado;
	}
	public Tarjeta getTarjeta(String nombreTarjeta) {
		Tarjeta tarjeta; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Tarjeta.buscaTarjeta");
		consulta.setParameter("nombrePerfil", nombreTarjeta);
		
		tarjeta = (Tarjeta) consulta.getSingleResult();
		
		return tarjeta;
	}
	
	public List<Tarjeta> getTarjetas() {
		List<Tarjeta> retorno = new ArrayList<Tarjeta>();  
		Query query = getEntityManager().createNamedQuery("Tarjeta.findAll");
		retorno = (List<Tarjeta>) query.getResultList();
		return retorno;
	}
}
