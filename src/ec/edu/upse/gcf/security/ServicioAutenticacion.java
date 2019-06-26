package ec.edu.upse.gcf.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.modelo.Usuarioperfil;
import ec.edu.upse.gcf.util.Context;

/**
 * clase para la configuracion de seguridad del usuario
 * @author Dayana Tigrero
 *
 */
public class ServicioAutenticacion implements UserDetailsService {

	/**
	 * Este metodo es invocado en el momento de la autenticacion paraa recuperar 
	 * los datos del usuario.
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		try {
			UsuarioDao usuarioDAO = new UsuarioDao();
			Usuario usuario; 
			User usuarioSpring;
			List<GrantedAuthority> privilegios; 

			usuario = usuarioDAO.getUsuario(nombreUsuario);
			
			Context.getInstance().setIdUsuarioLogeado(usuario.getIdUsuario()); //* tengo duda aqui
			
			privilegios = obtienePrivilegios(usuario);

			// Construye un objeto de Spring en base a los datos del usuario de la base de datos.
			usuarioSpring = new User(usuario.getUsuario(), usuario.getClave(), privilegios);

			return usuarioSpring;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}

	/**
	 * Elabora una lista de objetos del tipo GrantedAuthority en base a los privilegios
	 * del usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	private List<GrantedAuthority> obtienePrivilegios(Usuario usuario) {
		List<GrantedAuthority> listaPrivilegios = new ArrayList<GrantedAuthority>(); 

		for(Usuarioperfil usuarioPrivilegio  : usuario.getUsuarioperfils()){
			listaPrivilegios.add(new SimpleGrantedAuthority(usuarioPrivilegio.getPerfil().getNombre()));
		}

		return listaPrivilegios;
	}

}
