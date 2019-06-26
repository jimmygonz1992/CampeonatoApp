
package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.OpcionDao;
import ec.edu.upse.gcf.dao.OpcionPerfilDAO;
import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.modelo.Opcion;
import ec.edu.upse.gcf.modelo.Opcionperfil;
import ec.edu.upse.gcf.modelo.Perfil;

@SuppressWarnings("unused")
public class OpcionPerfilEditar {
	@Wire
	private Window winOpcionPerfilEditar;

	@Wire Listbox listBox;
	
	// Instancia el objeto para acceso a datos.
	private OpcionPerfilDAO opcionPerfildao = new OpcionPerfilDAO();
	private PerfilDAO perfilDao = new PerfilDAO();
	private OpcionDao opcionDao = new OpcionDao();

	private Opcionperfil opcionperfil = new Opcionperfil();
	private List<Opcion> opcionesAsignadas = new ArrayList<>();
	private List<Opcion> opcionesDisponible = new ArrayList<>();
	private List<Opcion> opciones = new ArrayList<>();
	private Set<Opcion> opcionSeleccionado;
	private List<Opcion>  menus = new ArrayList<>();
	
	private ArrayList<Opcion> listaOpciones = new ArrayList<Opcion>();
	private Opcion opcSeleccionado;
	private Opcionperfil opcperfil;


	@Wire
	Tree menu;
	
	@ Wire Checkbox chkAcceso;
 
	//para mi cuadritos de acceso
	private Perfil perfilseleccionado;
	private Opcion opcionseleccionado;
	private Opcion quitaropcion;

	private Opcion opcion;
	private Perfil perfil;	

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		menuLista();
		//System.out.println("hoooolllaaa");
		// Recupera el objeto pasado como parametro. 
		opcion = (Opcion) Executions.getCurrent().getArg().get("Opcion");
		perfil = (Perfil) Executions.getCurrent().getArg().get("Perfil");	

	}

	@Command
	@NotifyChange({"opciones", "menu"})
	public void menuLista() throws IOException{
		if(opciones != null) {
			opciones = null;
		}

		if(menu.getChildren() != null) {
			menu.getChildren().clear();
		}	

		opciones = opcionDao.getOpcionPadre();
		menu.appendChild(getTreechildren(opciones));
		opcionSeleccionado = null;		
	}

	@Command
	@NotifyChange({"menu", "opciones", "perfilseleccionado"})
	public void listaropciones() {				

		if(perfilseleccionado != null) {
			List<Opcionperfil> accesos =  opcionPerfildao.getAccesoPerfil(perfilseleccionado.getIdPerfil());
			if (!accesos.isEmpty()) {
				for (Opcionperfil opcionperfil : accesos) {
				/*	menus.add(opcionperfil. .getOpcion());
					if(!menus.isEmpty()) {
						menu.setCheckmark(true);
					}*/
					
					//menus.add(opcionperfil.getOpcion());
				}
			}		
		}
	}

	
	private Treechildren getTreechildren(List<Opcion> opciones) {
		Treechildren retorno = new Treechildren();

		for(Opcion opcion:opciones) {
			System.out.println(opcion);
			Treeitem ti = getTreeitem(opcion);	
			retorno.appendChild(ti);
			System.out.println(opcion.getIdOpcion());
			List<Opcion> listaPadreHijo = opcionDao.getOpcionPadreHijo(opcion.getIdOpcion());  //getOpcionPadrePorHijo(opcion.getIdOpcion());

			if (!listaPadreHijo.isEmpty()) {
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}

	@SuppressWarnings({ "deprecation" })
	private Treeitem getTreeitem(Opcion opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getTitulo());
		//System.out.println("Titulo :" + tc);

		if (opcion.getImagen() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getImagen());
		}		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}


	/** Graba la informacion.*/
	@Command
	public void grabar(){
		try {		
			System.out.println("seleccionado: " + menu.getSelectedItems().size());
			collectSelectedItems();
			opcionPerfildao.getEntityManager().getTransaction();
			for(Opcion op : listaOpciones) {
				Opcionperfil opClon = (Opcionperfil) opcperfil.clone();
				if (opClon.getIdOpcionPerfil() == 0) {
					opClon.setOpcion(op);
					opcionPerfildao.getEntityManager().persist(opClon);
				}else {
					opcperfil = (Opcionperfil) opcionPerfildao.getEntityManager().merge(opClon);
				}
			}
			// Cierra transaccion
			opcionPerfildao.getEntityManager().getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			opcionPerfildao.getEntityManager().getTransaction().rollback();
			Clients.showNotification("Proceso Ejecutado con exito.");			
			// Actualiza la lista
			//BindUtils.postGlobalCommand(null, null, "Detallecampeonato.buscarPorPatron", null);
			//salir();
		}		
	}


	
	public void collectSelectedItems() {
		listaOpciones.clear();
		Iterator<Listitem> iterator = listBox.getSelectedItems().iterator();
		while (iterator.hasNext()) {
			Listitem listitem = (Listitem) iterator.next();
			listaOpciones.add(listitem.getValue());
		}
	}
	
	
	/**Getter anD Setter */

	public List<Perfil> getPerfiles() {
		return perfilDao.getPerfil();
	}


	public List<Opcion> getOpcionesAsignadas() {
		return opcionesAsignadas;
	}
	
	public List<Opcion> getOpciones() {
		//System.out.println(detallecampeonato.getCampeonato());
		return opcionDao.getOpciones(null);
	}
	
	public void setOpcionesAsignadas(List<Opcion> opcionesAsignadas) {
		this.opcionesAsignadas = opcionesAsignadas;
	}
	public Opcionperfil getOpcionperfil() {
		return opcionperfil;
	}
	public void setOpcionperfil(Opcionperfil opcionperfil) {
		this.opcionperfil = opcionperfil;
	}
	public Perfil getPerfilseleccionado() {
		return perfilseleccionado;
	}
	public void setPerfilseleccionado(Perfil perfilseleccionado) {
		this.perfilseleccionado = perfilseleccionado;
	}
	public List<Opcion> getOpcionesDisponible() {
		return opcionesDisponible;
	}
	public void setOpcionesDisponible(List<Opcion> opcionesDisponible) {
		this.opcionesDisponible = opcionesDisponible;
	}
	public Opcion getOpcionseleccionado() {
		return opcionseleccionado;
	}
	public void setOpcionseleccionado(Opcion opcionseleccionado) {
		this.opcionseleccionado = opcionseleccionado;
	}

	public Opcion getQuitaropcion() {
		return quitaropcion;
	}

	public void setQuitaropcion(Opcion quitaropcion) {
		this.quitaropcion = quitaropcion;
	}

	public Opcion getOpcion() {
		return opcion;
	}

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Set<Opcion> getOpcionSeleccionado() {
		return opcionSeleccionado;
	}

	public void setOpcionSeleccionado(Set<Opcion> opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}

	public List<Opcion> getMenus() {
		return menus;
	}

	public void setMenus(List<Opcion> menus) {
		this.menus = menus;
	}
}
