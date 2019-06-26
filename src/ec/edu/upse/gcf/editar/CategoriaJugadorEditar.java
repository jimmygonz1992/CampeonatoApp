package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.dao.CategoriajugadorDAO;
import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.general.ConstanteApp;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Categoriajugador;
import ec.edu.upse.gcf.modelo.Jugador;

public class CategoriaJugadorEditar {

	@Wire private Window winCategoriaJugadorEditar;

	private CategoriajugadorDAO categoriaJugadorDao = new CategoriajugadorDAO();

	private CategoriaDAO categoriaDao = new CategoriaDAO();
	private JugadorDAO jugadorDao = new JugadorDAO();

	private Categoriajugador categoriajugador = new Categoriajugador();
	private List<Jugador> jugadoresAsignados = new ArrayList<>();
	private List<Jugador> jugadoresDisponibles = new ArrayList<>();
	private List<Jugador> jugadores = new ArrayList<>();

	//para mi cuadritos de acceso
	private Categoria categoriaseleccionada;
	private Jugador jugadorseleccionado;
	private Jugador quitarjugador;

	private Jugador jugador;
	private Categoria categoria;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		jugador = (Jugador) Executions.getCurrent().getArg().get("Jugador");
		categoria = (Categoria) Executions.getCurrent().getArg().get("Categoria");

	}

	@Command
	@NotifyChange({"jugadoresAsignados", "jugadoresDisponibles","categoriaseleccionada"})
	public void listarjugadores() {
		if(jugadoresAsignados != null && jugadoresDisponibles != null) {
			jugadoresAsignados.clear();
			jugadoresDisponibles.clear();
		}
		if(categoriaseleccionada != null) {
			List<Categoriajugador> op =  categoriaJugadorDao.getJugadorCategoria(categoriaseleccionada.getIdCategoria());
			if (!op.isEmpty()) {
				for (Categoriajugador categoriajugador : op) {
					jugadoresAsignados.add(categoriajugador.getJugador());					
				}
			}
			//if(categoriaseleccionada.getCategoriajugadores().isEmpty()) {
			List<Categoria> categorias = categoriaDao.getListaCategorias();
			//List<Jugador> jugadores = jugadorDao.getJugador();
			List<Jugador> jugadores =  jugadorDao.getJugadoresDisponibles(categoriaseleccionada);
			Integer contador = 0;
			if(!categorias.isEmpty()) {
				for(Jugador j : jugadores) {
					for(Categoria categoria : categorias) {
						if(categoria.getTipo().equals(ConstanteApp.FEMENINO)) {
							if(!categoria.getCategoriajugadores().isEmpty()) {
								for(Categoriajugador cj : categoria.getCategoriajugadores()) {
									if(cj.getJugador().getIdJugador() == j.getIdJugador() &&
											cj.getCategoria().getTipo().equals(j.getGenero()) &&
											cj.getCategoria().getDescripcion().equals(j.getEdad())) {
										contador++;
									}
								}
							}
						}
					}
					if(contador == 0) {
						if(categoriaseleccionada.getTipo().equals(j.getGenero()) && 
								categoriaseleccionada.getDescripcion().equals(j.getEdad())) {
							jugadoresDisponibles.add(j);
						}
					}else {
						contador = 0;
					}
				}
			}
			//}
		}

	}

	@Command
	@NotifyChange({"jugadorseleccionado", "jugadoresDisponibles", "jugadoresAsignados"})
	public void agregar() {
		if(jugadorseleccionado != null) {		
			jugadoresAsignados.add(jugadorseleccionado);
			jugadoresDisponibles.remove(jugadorseleccionado);	
			jugadorseleccionado = null;					
		}
	}

	@Command
	@NotifyChange({"jugadoresDisponibles", "jugadoresAsignados"})
	public void agregartodo () {
		if (!jugadoresDisponibles.isEmpty()) {
			for (Jugador j : jugadoresDisponibles) {
				jugadoresAsignados.add(j);				
			}
			jugadoresDisponibles = new ArrayList<>();
		}	
	}

	@Command
	@NotifyChange({"quitarjugador", "jugadoresDisponibles", "jugadoresAsignados"})
	public void quitar () {
		if(quitarjugador != null) {		
			jugadoresDisponibles.add(quitarjugador);
			jugadoresAsignados.remove(quitarjugador);
			quitarjugador = null;
		}
	}

	@Command
	@NotifyChange({"jugadoresDisponibles", "jugadoresAsignados"})
	public void quitartodo () {		
		if (!jugadoresAsignados.isEmpty()) {

			for (Jugador j : jugadoresAsignados) {
				jugadoresDisponibles.add(j);	
			}
			jugadoresAsignados = new ArrayList<>();
		}	
	}

	/** Graba la informacion.*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void grabar(){		
		if (categoriaseleccionada == null) {
			Clients.showNotification("Por favor no se pueden guardar datos vacíos, seleccione jugador(es) y asigne categoría.");
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {						
						if (!jugadoresAsignados.isEmpty()) {
							for (Jugador j : jugadoresAsignados) {
								Categoriajugador cj = categoriaJugadorDao.getJugador(categoriaseleccionada.getIdCategoria(), j.getIdJugador());
								if (cj == null) {
									Categoriajugador jugadorGrabar = new Categoriajugador();
									jugadorGrabar.setIdCategoriajugdor(0);
									jugadorGrabar.setJugador(j);
									jugadorGrabar.setCategoria(categoriaseleccionada);
									categoriaJugadorDao.getEntityManager().getTransaction().begin();
									categoriaJugadorDao.getEntityManager().persist(jugadorGrabar);
									categoriaJugadorDao.getEntityManager().getTransaction().commit();
								}else {
									cj.setJugador(j);
									cj.setCategoria(categoriaseleccionada);
									categoriaJugadorDao.getEntityManager().getTransaction().begin();
									categoriaJugadorDao.getEntityManager().merge(cj);
									categoriaJugadorDao.getEntityManager().getTransaction().commit();
								}
							}
						}	
						if (!jugadoresDisponibles.isEmpty()) {
							for (Jugador j : jugadoresDisponibles) {
								Categoriajugador cj = categoriaJugadorDao.getJugador(categoriaseleccionada.getIdCategoria(), j.getIdJugador());
								if (cj != null) {
									cj.setEstado("1");
									categoriaJugadorDao.getEntityManager().getTransaction().begin();
									categoriaJugadorDao.getEntityManager().merge(cj);
									categoriaJugadorDao.getEntityManager().getTransaction().commit();
								}
							}	
						}
						Clients.showNotification("Proceso Ejecutado con exito.");		
						BindUtils.postGlobalCommand(null, null, "Categoriajugador.buscarPorPatronr", null);
						salir();
					} catch (Exception e) {
						e.printStackTrace();
						categoriaJugadorDao.getEntityManager().getTransaction().rollback();
					}		
				}
			}
		});		
	}

	@Command
	public void salir(){
		winCategoriaJugadorEditar.detach();
	}

	//GETTER AND SETTER

	public List<Categoria> getCategorias() {
		return categoriaDao.getListaCategorias();
	}

	public Categoriajugador getCategoriajugador() {
		return categoriajugador;
	}

	public void setCategoriajugador(Categoriajugador categoriajugador) {
		this.categoriajugador = categoriajugador;
	}

	public List<Jugador> getJugadoresAsignados() {
		return jugadoresAsignados;
	}

	public void setJugadoresAsignados(List<Jugador> jugadoresAsignados) {
		this.jugadoresAsignados = jugadoresAsignados;
	}

	public List<Jugador> getJugadoresDisponibles() {
		return jugadoresDisponibles;
	}

	public void setJugadoresDisponibles(List<Jugador> jugadoresDisponibles) {
		this.jugadoresDisponibles = jugadoresDisponibles;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public Categoria getCategoriaseleccionada() {
		return categoriaseleccionada;
	}

	public void setCategoriaseleccionada(Categoria categoriaseleccionada) {
		this.categoriaseleccionada = categoriaseleccionada;
	}

	public Jugador getJugadorseleccionado() {
		return jugadorseleccionado;
	}

	public void setJugadorseleccionado(Jugador jugadorseleccionado) {
		this.jugadorseleccionado = jugadorseleccionado;
	}

	public Jugador getQuitarjugador() {
		return quitarjugador;
	}

	public void setQuitarjugador(Jugador quitarjugador) {
		this.quitarjugador = quitarjugador;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}	
}
