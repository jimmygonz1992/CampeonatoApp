package ec.edu.upse.gcf.dao;
import java.util.List;
import javax.persistence.Query;
import ec.edu.upse.gcf.modelo.Campeonato;

@SuppressWarnings("unchecked")
public class CampeonatoDAO extends ClaseDAO {
	public List<Campeonato> getCampeonatos(String value) {
		List<Campeonato> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Campeonatos.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Campeonato>) query.getResultList();		
		return resultado;
	}
	
	public List<Campeonato> getListaCampeonato() {
		List<Campeonato> resultado; 
		Query query = getEntityManager().createNamedQuery("Campeonato.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Campeonato>) query.getResultList();		
		return resultado;
	}
	
	public Campeonato getCampeonato(String nombreCampeonato) {
		Campeonato campeonato; 
		Query consulta;		
		consulta = getEntityManager().createNamedQuery("Campeonato.buscaCampeonato");
		consulta.setParameter("nombreCampeonato", nombreCampeonato);		
		campeonato = (Campeonato) consulta.getSingleResult();		
		return campeonato;
	}
}
