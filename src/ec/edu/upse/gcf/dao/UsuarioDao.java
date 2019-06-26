package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Usuario;

@SuppressWarnings("unchecked")
public class UsuarioDao extends ClaseDAO{
	/**
	 * RETORNA LA LISTA DE USUARIO DE ACUERDO A LA BUSQUEDA
	 * @param value
	 * @return
	 */
	public List<Usuario> getListaUsuarios(String value) {
		List<Usuario> resultado; 
		String patron = value;
		try {
			if (value == null || value.length() == 0) {
				patron = "%";
			}else{
				patron = "%" + patron.toLowerCase() + "%";
			}
			Query query = getEntityManager().createNamedQuery("Usuario.buscaPatron");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("patron", patron);
			resultado = (List<Usuario>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Usuario> getListaUsuariosPorBusqueda(String value) {
		List<Usuario> resultado; 
		String patron = value;
		try {
			if (value == null || value.length() == 0) {
				patron = "%";
			}else{
				patron = "%" + patron.toLowerCase() + "%";
			}
			Query query = getEntityManager().createNamedQuery("Usuario.buscaUsuario");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setParameter("nombreUsuario", patron);
			resultado = (List<Usuario>) query.getResultList();
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	

	/**
	 * Retorna al usuario en base a su nombre de usuario.
	 * @param nombreUsuario
	 * @return
	 */
	public Usuario getUsuario(String nombreUsuario) {
		Usuario usuario = null; 
		Query consulta = null;
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.buscaUsuario");
			consulta.setParameter("nombreUsuario", nombreUsuario);
			usuario = (Usuario) consulta.getSingleResult();			
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

	public List<Usuario> getUsuario() {
		List<Usuario> retorno = new ArrayList<Usuario>();  
		Query query = getEntityManager().createNamedQuery("Usuario.findAll");
		retorno = (List<Usuario>) query.getResultList();
		return retorno;
	}

	public Usuario getUsuarioCorreo(String correo) {
		Usuario usuario = null; 
		Query consulta = null;
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.buscaCorreo");
			consulta.setParameter("correo", correo);
			usuario = (Usuario) consulta.getSingleResult();			
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

	public List<Usuario> getValidarNombreUsuario(String usuario) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.validarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}

	public List<Usuario> getValidarUsuarioExistente(String cedulaUsuario) {
		List<Usuario> resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.validaUsuarioExiste");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedulaUsuario", cedulaUsuario);
		resultado = (List<Usuario>) query.getResultList();
		return resultado;
	}

	public Usuario getValidarCorreoExistente(String correoUsuario) {
		Usuario resultado; 
		Query query = getEntityManager().createNamedQuery("Usuario.validaCorreoExiste");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("correoUsuario", correoUsuario);
		resultado = (Usuario) query.getSingleResult();
		return resultado;
	}
}
