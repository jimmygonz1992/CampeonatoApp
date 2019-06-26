package ec.edu.upse.gcf.editar;

import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.modelo.Usuario;

public class UsuarioEditar{
	@Wire private Window winUsuarioEditar;
	@Wire private Textbox cedula;	
	@Wire private Textbox clave;
	@Wire private Textbox correo;
	@Wire private Textbox nombUsuario;
	@Wire private Textbox nombres;
	@Wire private Textbox apellidos;

	@Wire private Button btnGrabar;
	

	private UsuarioDao usuarioDao = new UsuarioDao();
	private Usuario usuario;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		usuario = (Usuario) Executions.getCurrent().getArg().get("Usuario");	
		deshabilitaBoton();
	}

	//valida cedula
	public boolean validarDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validaci�n c�dula
					// El decimo digito se lo considera d�gito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}

					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validacion");
			cedulaCorrecta = false;
		}
		if (!cedulaCorrecta) {
			System.out.println("La C�dula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}

	/**Funcion encriptar*/
	public String encriptar(String clave) throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(clave.getBytes());

		int size = b.length;
		StringBuilder h = new StringBuilder(size);
		for (int i = 0; i < size; i++) {

			int u = b[i] & 255;

			if (u < 16)
			{
				h.append("0").append(Integer.toHexString(u));
			}
			else
			{
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}

	/** M�todo para validar email*/
	boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
	/** Validar usuario */
	boolean validarUsuario() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDao.getValidarNombreUsuario(nombUsuario.getText());
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	/** Validar si el usuario existe a trav�s de la cedula */
	boolean validarUsuarioExistente() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDao.getValidarUsuarioExistente(cedula.getText());
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			return false;
		}
	}

	/** Validar si el correo del usuario ya se encuentra registrado */
	public boolean isValidarCorreoExistente() {
		try {
			Usuario objeto = usuarioDao.getValidarCorreoExistente(correo.getText());  
			if(objeto != null)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	/** Validar Datos */
	@Command
	public void isValidarV() {
		try {
			//Boolean retorna = true;
			if(!cedula.getText().isEmpty() && !nombres.getText().isEmpty()
					&& !apellidos.getText().isEmpty() && !correo.getText().isEmpty()
					&& !nombUsuario.getText().isEmpty() && !clave.getText().isEmpty()) {
				btnGrabar.setDisabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return false;
	}

	private void deshabilitaBoton() {
		btnGrabar.setDisabled(true);		
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;		
			if(validarDeCedula(cedula.getText())== false) {
				Clients.showNotification("La cédula ingresada no es válida!!");
				cedula.focus();				
				return resultado;
			}
			if(ValidarEmail(correo.getText()) == false) {
				Clients.showNotification("El correo ingresado no es válido!!");
				correo.focus();			
				return resultado;
			}
			if(validarUsuario() == true) {
				Clients.showNotification("El nombre de usuario ingresado ya existe!!");
				nombUsuario.focus();				
				return resultado;
			}
			if(validarUsuarioExistente() == true) {
				Clients.showNotification("El usuario ingresado ya existe!!");
				cedula.focus();				
				return resultado;
			}
			if(isValidarCorreoExistente()) {
				Clients.showNotification("El correo de usuario ingresado ya exite, ingrese su correo personal.!!");
				correo.focus();				
				return resultado;
			}		
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return false;
	}

	@Command
	public void grabar(){
		try {
			usuarioDao.getEntityManager().getTransaction().begin();			
			if (usuario.getIdUsuario() == 0) {
				if(isValidarDatos() == true) {
					return;
				}	
				usuario.setClave(encriptar(clave.getText()));
				usuarioDao.getEntityManager().persist(usuario);
			}else {					
				usuario = (Usuario) usuarioDao.getEntityManager().merge(usuario);
				usuario.setClave(encriptar(clave.getText()));				
			}
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Proceso Ejecutado con exito.");
			BindUtils.postGlobalCommand(null, null, "Usuario.buscaPatron", null);
			salir();


		} catch (Exception e) {
			e.printStackTrace();			
			usuarioDao.getEntityManager().getTransaction().rollback();
		}		
	}	
	
	


	@Command
	public void salir(){
		winUsuarioEditar.detach();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}