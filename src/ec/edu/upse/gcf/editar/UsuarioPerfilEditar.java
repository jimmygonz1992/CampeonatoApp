package ec.edu.upse.gcf.editar;

import java.util.List;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Perfil;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.modelo.Usuarioperfil;

public class UsuarioPerfilEditar {

		@Wire private Window winUsuarioPerfilEditar;
		@Wire private Combobox cbUsuario;
		@Wire private Combobox cbPerfil;
		
		private UsuarioPerfilDao usuarioperfilDao = new UsuarioPerfilDao();
		private PerfilDAO perfilDao = new PerfilDAO();
		private UsuarioDao usuarioDao = new UsuarioDao();
		private Usuarioperfil usuarioperfil;
		
		@AfterCompose
		public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
			Selectors.wireComponents(view, this, false);
			usuarioperfil = (Usuarioperfil) Executions.getCurrent().getArg().get("Usuarioperfil");	
		}
		
		public boolean isValidar() {
			try {
				Boolean retorna = true;
				if(cbUsuario.getValue().isEmpty()) {
					cbUsuario.focus();
					return retorna;
				}
				if(cbPerfil.getValue().isEmpty()) {
					cbPerfil.focus();
					return retorna;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Command
		public void grabar(){					
			try {
				if(isValidar() == false) {
					usuarioperfilDao.getEntityManager().getTransaction().begin();
					if (usuarioperfil.getIdUsuPerfil() == 0) {
						usuarioperfilDao.getEntityManager().persist(usuarioperfil);
					}else{
						usuarioperfil = (Usuarioperfil) usuarioperfilDao.getEntityManager().merge(usuarioperfil);
					}				
					usuarioperfilDao.getEntityManager().getTransaction().commit();				
					Clients.showNotification("Proceso Ejecutado con exito.");			
					BindUtils.postGlobalCommand(null, null, "UsuarioPerfilLista.buscar", null);
					salir();		
				}else {
					Clients.showNotification("No hay datos para guardar.!!");
				}						
			} catch (Exception e) {
				e.printStackTrace();
				usuarioperfilDao.getEntityManager().getTransaction().rollback();
			}			
		}
		
		@Command
		public void salir(){
			winUsuarioPerfilEditar.detach();
		}			
		public List<Usuario> getUsuarios() {
			return usuarioDao.getUsuario();
		}
		public List<Perfil> getPerfiles() {
			return perfilDao.getPerfil();
		}
		public Usuarioperfil getUsuarioperfil() {
			return usuarioperfil;
		}
		public void setUsuarioperfil(Usuarioperfil usuarioperfil) {
			this.usuarioperfil = usuarioperfil;
		}				
}
