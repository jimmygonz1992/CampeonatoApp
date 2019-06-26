package ec.edu.upse.gcf.control;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import ec.edu.upse.gcf.dao.OpcionDao;
import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Opcion;
import ec.edu.upse.gcf.security.SecurityUtil;

@SuppressWarnings("unchecked")
public class MenuPrincipalControl {
	private Opcion opcionSeleccionado;
	private List<Opcion> listaOpcion;
	private OpcionDao opcionDao = new OpcionDao();
	private UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();
			
	@Wire
	Tree menu;
	
	@Wire
	Include areaContenido;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		loadTree();
	}
	
	public void loadTree() throws IOException{
		
		if(listaOpcion != null) {
			listaOpcion = null;
		}
		
		if(menu.getChildren() != null) {
			menu.getChildren().clear();
		}
		
		Integer id = usuarioPerfilDao.getUsuarioPorRol(SecurityUtil.getUser().getUsername().trim()); 
		
		//if(listaOpcion != null) {
			if (id != null){
				System.out.println("**** rol :" +id);
				listaOpcion = opcionDao.getOpcionPadrePorPerfil(id);
				if (listaOpcion.size()>0) {
					menu.appendChild(getTreechildren(listaOpcion,id));   
				}			
			}			
		//}		
		listaOpcion = null;	
	}
	
	private Treechildren getTreechildren(List<Opcion> opciones, Integer idPerfil) {
		Treechildren retorno = new Treechildren();
		for(Opcion opcion:opciones) {
			Treeitem ti = getTreeitem(opcion);
			retorno.appendChild(ti);
			List<Opcion> listaPadreHijo = opcionDao.getOpcionHijoPorPerfil(opcion.getIdOpcion(), idPerfil);
			if (!listaPadreHijo.isEmpty()) {
				ti.appendChild(getTreechildren(listaPadreHijo,idPerfil));
			}
		}
		return retorno;
	}

	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private Treeitem getTreeitem(Opcion opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getTitulo());
		//System.out.println("titulomenu: " + tc);
		if (opcion.getImagen() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getImagen());
		}
	
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}
	
	@NotifyChange({"areaContenido"})
	public void loadContenido(Opcion opcion) {
		
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
		
	}
		  	
	/**
	 * Retorna el nombre del usuario logoneado.
	 * @return
	 */
	public String getNombreUsuario() {
		return SecurityUtil.getUser().getUsername();
	}
	

	public List<Opcion> getListaOpcion() {
		return listaOpcion;
	}

	public void setListaOpcion(List<Opcion> listaOpcion) {
		this.listaOpcion = listaOpcion;
	}

	public Tree getMenu() {
		return menu;
	}

	public void setMenu(Tree menu) {
		this.menu = menu;
	}

	public Include getAreaContenido() {
		return areaContenido;
	}

	public void setAreaContenido(Include areaContenido) {
		this.areaContenido = areaContenido;
	}

	public Opcion getOpcionSeleccionado() {
		return opcionSeleccionado;
	}

	public void setOpcionSeleccionado(Opcion opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}
}
