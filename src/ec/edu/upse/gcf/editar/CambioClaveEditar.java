package ec.edu.upse.gcf.editar;

import java.security.MessageDigest;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.security.SecurityUtil;

public class CambioClaveEditar {

	@Wire private Textbox claveNueva;	
	@Wire private Textbox confirmaClave;	

	@Wire private Button btnGrabando;

	private UsuarioDao usuarioDao = new UsuarioDao();
	private Usuario usuario;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		usuario = (Usuario) Executions.getCurrent().getArg().get("Usuario");	
		deshabilitaBoton();
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

	private void deshabilitaBoton() {
		btnGrabando.setDisabled(true);		
	}

	/** Validar Datos */
	@Command
	public void isValidarV() {
		try {
			//Boolean retorna = true;
			if(!claveNueva.getText().isEmpty() && !confirmaClave.getText().isEmpty()
					&& claveNueva.getText().equals(confirmaClave.getText())) {
				btnGrabando.setDisabled(false);
			}else {
				Clients.showNotification("Las claves no coinciden");
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return false;
	}


	@Command
	@NotifyChange({"confirmaClave"})
	public void grabando(){
		try {				
			UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();
			Usuario obj = usuarioPerfilDao.getUsuario(SecurityUtil.getUser().getUsername().trim());
			usuarioDao.getEntityManager().getTransaction().begin();				
			if(obj.isCambioClave() == false) {
				obj.setClave(encriptar(confirmaClave.getText()));
				obj.setCambioClave(true);
				obj = (Usuario) usuarioDao.getEntityManager().merge(obj);

			}
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Proceso Ejecutado con exito.");
			Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
			System.out.println(obj.isCambioClave());


		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			usuarioDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Command
	public void saliendo(){
		Executions.getCurrent().sendRedirect("/login.zul");
	}
	public Textbox getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(Textbox claveNueva) {
		this.claveNueva = claveNueva;
	}

	public Textbox getConfirmaClave() {
		return confirmaClave;
	}

	public void setConfirmaClave(Textbox confirmaClave) {
		this.confirmaClave = confirmaClave;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
