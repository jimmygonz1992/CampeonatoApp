package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.dao.EquipojugadorDAO;
import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Categoriajugador;
import ec.edu.upse.gcf.modelo.Configuracionjugador;
import ec.edu.upse.gcf.modelo.Detallecampeonato;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.modelo.Equipojugador;
import ec.edu.upse.gcf.modelo.Jugador;

public class EquipoJugadorEditar {
	@Wire private Window winEquipoJugadorEditar;
	@Wire Combobox cboCampeonato;
	@Wire Combobox cboEquipo;
	@Wire Button btnGrabar;
	@Wire Listbox lstJugadoresD;
	@Wire Listbox lstJugadoresI;

	private EquipojugadorDAO equipoJugadorDao = new EquipojugadorDAO();
	private DetalleCampeonatoDAO detallecampeonatoDao = new DetalleCampeonatoDAO();

	private EquipoDAO equipoDao = new EquipoDAO();
	private JugadorDAO jugadorDao = new JugadorDAO();
	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	//private CategoriajugadorDAO categoriaJugadorDao = new CategoriajugadorDAO();

	private Detallecampeonato detallecampeonato = new Detallecampeonato();
	private Equipojugador equipojugador = new Equipojugador();
	private List<Campeonato> campeonatosDisponibles = new ArrayList<>();
	private List<Detallecampeonato> dc = new ArrayList<>();
	private List<Equipo> listaequipos = new ArrayList<>();
	private List<Jugador> jugadoresInscritos = new ArrayList<>();
	private List<Jugador> jugadoresDisponibles = new ArrayList<>();
	private List<Jugador> jugadores = new ArrayList<>();

	//para mi cuadritos de acceso
	private Campeonato campeonatoseleccionada;
	private Detallecampeonato detallecampeonatoseleccionado;
	private Equipo equiposeleccionada;
	private Jugador jugadorseleccionado;
	private Jugador quitarjugador;
	private Configuracionjugador configuracionjugador;
	private String categoria;

	public Detallecampeonato getDcampeonato() {
		return dcampeonato;
	}

	public void setDcampeonato(Detallecampeonato dcampeonato) {
		this.dcampeonato = dcampeonato;
	}

	private Detallecampeonato dcampeonato;
	private Campeonato campeonato;
	private Equipo equipo;
	private Jugador jugador;	

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro.
		campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");
		equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");
		jugador = (Jugador) Executions.getCurrent().getArg().get("Jugador");
		deshabilitar();
	}	


	@Command
	@NotifyChange({"categoria", "campeonatoseleccionada"})
	public void disableScreen() {
		Date fechaActual = new Date();
		if(campeonatoseleccionada != null) {
			for (Detallecampeonato detalle : campeonatoseleccionada.getDetallecampeonatos()) {
				categoria = detalle.getCategoria().getNombre();
			}
			for (Configuracionjugador objeto : campeonatoseleccionada.getConfiguracionjugadores()) {
				if(fechaActual.compareTo(objeto.getFechaInicio()) == 0) {					
					if(fechaActual.compareTo(objeto.getFechaFin()) == -1) {


						Clients.showNotification("Pantalla Habilitada");
						BindUtils.postGlobalCommand(null, null, "EquipoJugadorEditar.listarequipos", null);
						cboEquipo.setDisabled(false);
						btnGrabar.setDisabled(false);
						lstJugadoresD.setDisabled(false);
						lstJugadoresI.setDisabled(false);
					}

				}else{
					if(fechaActual.compareTo(objeto.getFechaInicio()) == 1) {
						if(fechaActual.compareTo(objeto.getFechaFin()) == 0) {
							Clients.showNotification("Pantalla Habilitada");
							BindUtils.postGlobalCommand(null, null, "EquipoJugadorEditar.listarequipos", null);
							cboEquipo.setDisabled(false);
							btnGrabar.setDisabled(false);
							lstJugadoresD.setDisabled(false);
							lstJugadoresI.setDisabled(false);
						}else {
							if(fechaActual.compareTo(objeto.getFechaFin()) == -1) {
								Clients.showNotification("Pantalla Habilitada");
								BindUtils.postGlobalCommand(null, null, "EquipoJugadorEditar.listarequipos", null);								
								cboEquipo.setDisabled(false);
								btnGrabar.setDisabled(false);
								lstJugadoresD.setDisabled(false);
								lstJugadoresI.setDisabled(false);
							}else {
								Clients.showNotification("Pantalla deshabilitada la fecha del Campeonato ha culminado");								
								cboEquipo.setDisabled(true);
								btnGrabar.setDisabled(true);
								lstJugadoresD.setDisabled(true);
								lstJugadoresI.setDisabled(true);
							}
						}
					}else {
						Clients.showNotification("Pantalla deshabilitada la fecha del Campeonato aún no ha iniciado");					
						cboEquipo.setDisabled(true);
						btnGrabar.setDisabled(true);
						lstJugadoresD.setDisabled(true);
						lstJugadoresI.setDisabled(true);
					}
				}				
			}
		}
	}

	public void deshabilitar () {	
		cboEquipo.setDisabled(true);
		btnGrabar.setDisabled(true);
		lstJugadoresD.setDisabled(true);
		lstJugadoresI.setDisabled(true);
	}

	@Command
	@GlobalCommand("EquipoJugadorEditar.listarequipos")
	@NotifyChange({"listaequipos", "campeonatoseleccionada"})
	public void listarequipos() {
		if(listaequipos != null) {		
			listaequipos.clear();
		}

		if(campeonatoseleccionada != null) {
			List<Detallecampeonato> opcion = detallecampeonatoDao.getCampeonatoEquipo(campeonatoseleccionada.getIdCampeonato());
			if(!opcion.isEmpty()) {
				for(Detallecampeonato detallecampeonato : opcion) {
					listaequipos.add(detallecampeonato.getEquipo());
				}
			}			
		}
	}

	@Command
	@NotifyChange({"jugadoresInscritos", "jugadoresDisponibles", "campeonatoseleccionada", "equiposeleccionada"})
	public void listajugadores() {
		if(jugadoresInscritos != null && jugadoresDisponibles != null) {
			jugadoresInscritos.clear();
			jugadoresDisponibles.clear();
		}

		if(campeonatoseleccionada != null && equiposeleccionada != null ) {
			List<Equipojugador> ej = equipoJugadorDao.getJugadorEquipoCampeonato(campeonatoseleccionada.getIdCampeonato(), equiposeleccionada.getIdEquipo());
			if(!ej.isEmpty()) {
				for(Equipojugador equipojugador : ej) {
					jugadoresInscritos.add(equipojugador.getJugador());
				}
			}			
			List<Categoriajugador> categoriajugadores = new ArrayList<Categoriajugador>();
			Categoria categoriadetalle = null;
			for (Detallecampeonato detalle : campeonatoseleccionada.getDetallecampeonatos()) {
				categoriadetalle = detalle.getCategoria();
			}			
			categoriajugadores = categoriadetalle.getCategoriajugadores();			
			Integer contador = 0;			
			for(Categoriajugador cj : categoriajugadores) {
				for(Equipojugador jugador : campeonatoseleccionada.getEquipojugadors()) {
					if(cj.getJugador().getIdJugador() == jugador.getJugador().getIdJugador()) {
						contador++;								
					}
				}
				if(contador == 0) {
					jugadoresDisponibles.add(cj.getJugador());

				}else {
					contador = 0;
				}
			}
		}	
	}	

	/** BOTONES DE AGREGADO*/
	@Command
	@NotifyChange({"jugadorseleccionado", "jugadoresDisponibles", "jugadoresInscritos"})
	public void agregar() {
		if(jugadorseleccionado != null) {		
			jugadoresInscritos.add(jugadorseleccionado);
			jugadoresDisponibles.remove(jugadorseleccionado);	
			jugadorseleccionado = null;					
		}
	}

	@Command
	@NotifyChange({"jugadoresDisponibles", "jugadoresInscritos"})
	public void agregartodo () {
		if (!jugadoresDisponibles.isEmpty()) {
			for (Jugador j : jugadoresDisponibles) {
				jugadoresInscritos.add(j);				
			}
			jugadoresDisponibles = new ArrayList<>();
		}	
	}

	@Command
	@NotifyChange({"quitarjugador", "jugadoresDisponibles", "jugadoresInscritos"})
	public void quitar () {
		if(quitarjugador != null) {		
			jugadoresDisponibles.add(quitarjugador);
			jugadoresInscritos.remove(quitarjugador);
			quitarjugador = null;
		}
	}

	@Command
	@NotifyChange({"jugadoresDisponibles", "jugadoresInscritos"})
	public void quitartodo () {		
		if (!jugadoresInscritos.isEmpty()) {

			for (Jugador j : jugadoresInscritos) {
				jugadoresDisponibles.add(j);	
			}
			jugadoresInscritos = new ArrayList<>();
		}	
	}

	/** Graba la informacion.*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void grabar(){
		if (campeonatoseleccionada == null && equiposeleccionada == null) {
			Clients.showNotification("Por favor no se pueden guardar datos vacíos, seleccione jugador(es) y asigne categoría.");
			return;
		}	
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {						
						if (!jugadoresInscritos.isEmpty()) {
							for (Jugador j : jugadoresInscritos) {
								Equipojugador cej = equipoJugadorDao.getJugadorGuarda(campeonatoseleccionada.getIdCampeonato(), equiposeleccionada.getIdEquipo(), j.getIdJugador());
								if (cej == null) {
									Equipojugador jugadorGrabar = new Equipojugador();
									jugadorGrabar.setIdEquipojugador(0);
									jugadorGrabar.setJugador(j);
									jugadorGrabar.setEquipo(equiposeleccionada);
									jugadorGrabar.setCampeonato(campeonatoseleccionada);						
									equipoJugadorDao.getEntityManager().getTransaction().begin();
									equipoJugadorDao.getEntityManager().persist(jugadorGrabar);
									equipoJugadorDao.getEntityManager().getTransaction().commit();
								}else {
									cej.setJugador(j);
									cej.setEquipo(equiposeleccionada);
									cej.setCampeonato(campeonatoseleccionada);
									equipoJugadorDao.getEntityManager().getTransaction().begin();
									equipoJugadorDao.getEntityManager().merge(cej);
									equipoJugadorDao.getEntityManager().getTransaction().commit();
								}
							}
						}	
						if (!jugadoresDisponibles.isEmpty()) {
							for (Jugador j : jugadoresDisponibles) {
								Equipojugador cej = equipoJugadorDao.getJugadorGuarda(campeonatoseleccionada.getIdCampeonato(), equiposeleccionada.getIdEquipo(), j.getIdJugador());
								if (cej != null) {
									cej.setEstado("1");
									equipoJugadorDao.getEntityManager().getTransaction().begin();
									equipoJugadorDao.getEntityManager().merge(cej);
									equipoJugadorDao.getEntityManager().getTransaction().commit();
								}
							}	
						}
						Clients.showNotification("Proceso Ejecutado con exito.");
						jugadoresDisponibles.clear();
						jugadoresInscritos.clear();
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Equipojugador.buscarPorPatron", null);
						salir();
					} catch (Exception e) {
						e.printStackTrace();
						// Si hay error, reversa la transaccion.
						equipoJugadorDao.getEntityManager().getTransaction().rollback();
					}		
				}
			}
		});
	}

	@Command
	public void salir(){
		winEquipoJugadorEditar.detach();
	}

	//GETTER AND SETTER
	public DetalleCampeonatoDAO getDetallecampeonatoDao() {
		return detallecampeonatoDao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setDetallecampeonatoDao(DetalleCampeonatoDAO detallecampeonatoDao) {
		this.detallecampeonatoDao = detallecampeonatoDao;
	}

	public List<Equipo> getEquipos() {
		return equipoDao.getListaEquipos();
	}

	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}

	public EquipojugadorDAO getEquipoJugadorDao() {
		return equipoJugadorDao;
	}

	public void setEquipoJugadorDao(EquipojugadorDAO equipoJugadorDao) {
		this.equipoJugadorDao = equipoJugadorDao;
	}

	public EquipoDAO getEquipoDao() {
		return equipoDao;
	}

	public void setEquipoDao(EquipoDAO equipoDao) {
		this.equipoDao = equipoDao;
	}

	public JugadorDAO getJugadorDao() {
		return jugadorDao;
	}

	public void setJugadorDao(JugadorDAO jugadorDao) {
		this.jugadorDao = jugadorDao;
	}

	public Equipojugador getEquipojugador() {
		return equipojugador;
	}

	public void setEquipojugador(Equipojugador equipojugador) {
		this.equipojugador = equipojugador;
	}

	public List<Jugador> getJugadoresInscritos() {
		return jugadoresInscritos;
	}

	public void setJugadoresInscritos(List<Jugador> jugadoresInscritos) {
		this.jugadoresInscritos = jugadoresInscritos;
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

	public Campeonato getCampeonatoseleccionada() {
		return campeonatoseleccionada;
	}

	public void setCampeonatoseleccionada(Campeonato campeonatoseleccionada) {
		this.campeonatoseleccionada = campeonatoseleccionada;
	}

	public Equipo getEquiposeleccionada() {
		return equiposeleccionada;
	}

	public void setEquiposeleccionada(Equipo equiposeleccionada) {
		this.equiposeleccionada = equiposeleccionada;
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

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public List<Campeonato> getCampeonatosDisponibles() {
		return campeonatosDisponibles;
	}

	public void setCampeonatosDisponibles(List<Campeonato> campeonatosDisponibles) {
		this.campeonatosDisponibles = campeonatosDisponibles;
	}

	public List<Equipo> getListaequipos() {
		return listaequipos;
	}

	public void setListaequipos(List<Equipo> listaequipos) {
		this.listaequipos = listaequipos;
	}

	public Window getWinEquipoJugadorEditar() {
		return winEquipoJugadorEditar;
	}

	public void setWinEquipoJugadorEditar(Window winEquipoJugadorEditar) {
		this.winEquipoJugadorEditar = winEquipoJugadorEditar;
	}

	public Detallecampeonato getDetallecampeonato() {
		return detallecampeonato;
	}

	public void setDetallecampeonato(Detallecampeonato detallecampeonato) {
		this.detallecampeonato = detallecampeonato;
	}

	public Detallecampeonato getDetallecampeonatoseleccionado() {
		return detallecampeonatoseleccionado;
	}

	public void setDetallecampeonatoseleccionado(Detallecampeonato detallecampeonatoseleccionado) {
		this.detallecampeonatoseleccionado = detallecampeonatoseleccionado;
	}

	public List<Detallecampeonato> getDc() {
		return dc;
	}

	public void setDc(List<Detallecampeonato> dc) {
		this.dc = dc;
	}

	public Configuracionjugador getConfiguracionjugador() {
		return configuracionjugador;
	}

	public void setConfiguracionjugador(Configuracionjugador configuracionjugador) {
		this.configuracionjugador = configuracionjugador;
	}	

}
