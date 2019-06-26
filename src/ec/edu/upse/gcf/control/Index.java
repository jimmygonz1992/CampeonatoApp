package ec.edu.upse.gcf.control;

import java.io.IOException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;

import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.security.SecurityUtil;




public class Index {
	private UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();


	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		principal();
	}

	private void principal() {
		Usuario objeto  = usuarioPerfilDao.getUsuario(SecurityUtil.getUser().getUsername().trim()); 		
		if(objeto.isCambioClave()== true) {
			Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
		}else {
			Executions.sendRedirect("/Mantenimiento/usuarios/cambioClave.zul");
		}

	}
}
