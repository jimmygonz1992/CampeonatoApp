package ec.edu.upse.gcf.editar;
import java.util.ArrayList;
import java.util.List;

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
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.OpcionDao;
import ec.edu.upse.gcf.dao.OpcionPerfilDAO;
import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.modelo.Opcion;
import ec.edu.upse.gcf.modelo.Opcionperfil;
import ec.edu.upse.gcf.modelo.Perfil;
public class Permiso {
	@Wire
	private Window winOpcionPerfilEditar;

	// Instancia el objeto para acceso a datos.
	private OpcionPerfilDAO opcionPerfildao = new OpcionPerfilDAO();
	private PerfilDAO perfilDao = new PerfilDAO();
	private OpcionDao opcionDao = new OpcionDao();

	private Opcionperfil opcionperfil = new Opcionperfil();
	private List<Opcion> opcionesAsignadas = new ArrayList<>();
	private List<Opcion> opcionesDisponible = new ArrayList<>();
	private List<Opcion> listasubmenu = new ArrayList<>();

	//para mi cuadritos de acceso
	private Perfil perfilseleccionado;
	private Opcion opcionseleccionado;
	private Opcion opcionmenuseleccionado;
	private Opcion quitaropcion;

	private Opcion opcion;
	private Perfil perfil;


	/**
	 * Metodo que emula al metodo doAfterComposer de MVC
	 */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		opcion = (Opcion) Executions.getCurrent().getArg().get("Opcion");
		perfil = (Perfil) Executions.getCurrent().getArg().get("Perfil");	
	}	
	
	@Command
	@NotifyChange({"opcionesAsignadas", "opcionesDisponible"})
	public void listaropciones() {
		if(opcionesAsignadas != null) {
			opcionesAsignadas.clear();
		}

		if(opcionesDisponible != null) {
			opcionesDisponible.clear();
		}

		if(perfilseleccionado != null) {
			List<Opcionperfil> accesos =  opcionPerfildao.getAccesoPerfil(perfilseleccionado.getIdPerfil());
			if (!accesos.isEmpty()) {
				for (Opcionperfil opcionperfil : accesos) {
					opcionesAsignadas.add(opcionperfil.getOpcion());

				}
			}

			List<Opcion> opciones = opcionDao.getOpcionesDisponibles(perfilseleccionado);
			if (!opciones.isEmpty()) {
				for (Opcion objeto : opciones) {
					opcionesDisponible.add(objeto);
				}
			}
		}
	}	
	
	@Command
	@NotifyChange({"listasubmenu", "opcionmenuseleccionado"})
	public void listarsubmenu () {
		if(listasubmenu != null) {
			listasubmenu.clear();
		}
		if(opcionmenuseleccionado != null) {
			//List<Opcion> opmenu = opcionDao.getOpcion();
			List<Opcion> o = opcionDao.getSubmenu(opcionmenuseleccionado.getIdOpcion());
			if(!o.isEmpty()) {
				for(Opcion opcion : o) {				
						listasubmenu.add(opcion);														
				}
			}
		}
	}


	/** Graba la informacion.*/
	@Command
	public void grabar(){
		try {			
			if (perfilseleccionado == null) {
				Clients.showNotification("Seleccion el perfil");
				return;
			}		
			if (!opcionesAsignadas.isEmpty()) {
				for (Opcion objeto : opcionesAsignadas) {
					Opcionperfil acceso = opcionPerfildao.getAcceso(perfilseleccionado.getIdPerfil(), objeto.getIdOpcion());
					if (acceso == null) {
						Opcionperfil accesoGrabar = new Opcionperfil();
						accesoGrabar.setIdOpcionPerfil(0);
						accesoGrabar.setOpcion(objeto);
						accesoGrabar.setPerfil(perfilseleccionado);
						opcionPerfildao.getEntityManager().getTransaction().begin();
						opcionPerfildao.getEntityManager().persist(accesoGrabar);
						opcionPerfildao.getEntityManager().getTransaction().commit();
					}else {		

						acceso.setOpcion(objeto);
						acceso.setPerfil(perfilseleccionado);
						opcionPerfildao.getEntityManager().getTransaction().begin();
						opcionPerfildao.getEntityManager().merge(acceso);
						opcionPerfildao.getEntityManager().getTransaction().commit();
					}
				}
			}	

			if (!opcionesDisponible.isEmpty()) {
				for (Opcion objeto : opcionesDisponible) {
					Opcionperfil op = opcionPerfildao.getAcceso(perfilseleccionado.getIdPerfil(), objeto.getIdOpcion());
					if (op != null) {
						op.setEstado("1");
						opcionPerfildao.getEntityManager().getTransaction().begin();
						opcionPerfildao.getEntityManager().merge(op);
						opcionPerfildao.getEntityManager().getTransaction().commit();
					}
				}	
			}

			Clients.showNotification("Proceso Ejecutado con exito.");
			// Actualiza la lista
			//BindUtils.postGlobalCommand(null, null, "UsuarioPerfilLista.buscar", null);

		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			opcionPerfildao.getEntityManager().getTransaction().rollback();
		}		
	}

	@Command
	@NotifyChange({"opcionseleccionado", "opcionesDisponible", "opcionesAsignadas"})
	public void agregar() {
		if(opcionseleccionado != null) {
			System.out.println(opcionseleccionado.getTitulo());
			opcionesAsignadas.add(opcionseleccionado);
			opcionesDisponible.remove(opcionseleccionado);	
			opcionseleccionado = null;
		}
	}

	@Command
	@NotifyChange({"opcionesDisponible", "opcionesAsignadas"})
	public void agregartodo () {
		if (!opcionesDisponible.isEmpty()) {
			for (Opcion op : opcionesDisponible) {
				opcionesAsignadas.add(op);				
			}
			opcionesDisponible = new ArrayList<>();
		}	
	}

	@Command
	@NotifyChange({"quitaropcion", "opcionesDisponible", "opcionesAsignadas"})
	public void quitar () {
		if(quitaropcion != null) {
			System.out.println(quitaropcion.getTitulo());
			opcionesDisponible.add(quitaropcion);
			opcionesAsignadas.remove(quitaropcion);
			quitaropcion = null;
		}
	}

	@Command
	@NotifyChange({"opcionesDisponible", "opcionesAsignadas"})
	public void quitartodo () {		
		if (!opcionesAsignadas.isEmpty()) {

			for (Opcion op : opcionesAsignadas) {
				opcionesDisponible.add(op);	
			}
			opcionesAsignadas = new ArrayList<>();
		}	
	}	

	/**Getter anD Setter */

	public List<Perfil> getPerfiles() {
		return perfilDao.getPerfil();
	}
	
	public List<Opcion> getOpcionesMenu() {
		return opcionDao.getOpcionMenu();
	}


	public List<Opcion> getOpcionesAsignadas() {
		return opcionesAsignadas;
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

	public List<Opcion> getListasubmenu() {
		return listasubmenu;
	}

	public void setListasubmenu(List<Opcion> listasubmenu) {
		this.listasubmenu = listasubmenu;
	}

	public Opcion getOpcionmenuseleccionado() {
		return opcionmenuseleccionado;
	}

	public void setOpcionmenuseleccionado(Opcion opcionmenuseleccionado) {
		this.opcionmenuseleccionado = opcionmenuseleccionado;
	}		
}
