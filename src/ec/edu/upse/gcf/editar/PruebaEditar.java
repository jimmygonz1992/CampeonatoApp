package ec.edu.upse.gcf.editar;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.EquipojugadorDAO;
import ec.edu.upse.gcf.dao.PartidoDAO;
import ec.edu.upse.gcf.modelo.Cambio;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Detallecalendario;
import ec.edu.upse.gcf.modelo.Detallepartido;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.modelo.Equipojugador;
import ec.edu.upse.gcf.modelo.Jugador;
import ec.edu.upse.gcf.modelo.Partido;

public class PruebaEditar extends SelectorComposer {
	@Wire private Window winInscripcionequipoEditar;
	@Wire Listbox lstJugadoresEuno;
	@Wire Label lblCampeonato; 
	@Wire Label lblEquipouno;
	@Wire Label lblEquipodos;
	@Wire Label fecha;
	@Wire Label hora;
	@Wire List<Detallepartido> lblJuno;
	@Wire List<Detallepartido> lblJdos;
	@Wire Textbox txtgolEunopt;
	@Wire Textbox txtgolEunost;
	@Wire Textbox txtgolEdospt;
	@Wire Textbox txtgolEdosSt;
	
	private Detallecalendario detallecalendario;
	private List<Jugador> jugadoresEuno = new ArrayList<>();
	private List<Jugador> jugadoresEdos = new ArrayList<>();	
	private EquipojugadorDAO equipoJugadorDao = new EquipojugadorDAO();
	private List<Cambio> cambioJugadorElocal = new ArrayList<>();
	private List<Cambio> cambioJugadorEVisitante = new ArrayList<>();
	private List<Jugador> jugadorSEuno = new ArrayList<>();
	private PartidoDAO partidoDao = new PartidoDAO();

	private Equipo equipo;
	private Campeonato campeonato;
	private Jugador jugador;	
	private Equipojugador equipojugador = new Equipojugador();

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera el objeto pasado como parametro. 
				detallecalendario = (Detallecalendario) Executions.getCurrent().getArg().get("Detallecalendario");	
				campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");
				equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");
				jugador = (Jugador) Executions.getCurrent().getArg().get("Jugador");
	}
	

	//@GlobalCommand("Equipojugador.BuscarJugadorEquipouno")
	//@Command
	//@NotifyChange({"lblCampeonato","lblEquipouno", "jugadoresEuno"})
	@Listen("onClick=#buscarEquipouno")
	public void buscarEquipouno() {	
		if(jugadoresEuno != null ) {
			jugadoresEuno.clear();
		}
		if(lblCampeonato.getValue() != null && lblEquipouno.getValue() != null ) {
			List<Equipojugador> ej = equipoJugadorDao.getJugadorEquipoCampeonato(lblCampeonato.getValue(), lblEquipouno.getValue());    //getJugadorEquipoUno(equiposeleccionado.getIdEquipo());
			if(!ej.isEmpty()) {
				for(Equipojugador equipojugador : ej) {
					jugadoresEuno.add(equipojugador.getJugador());					
				}
			}
		}
	}

	//@GlobalCommand("Equipojugador.BuscarJugadorEquipodos")
	//@Command
	//@NotifyChange({"lblCampeonato","lblEquipodos", "jugadoresEdos"})
	@Listen("onClick=#buscarEquipodos")
	public void buscarEquipodos() {		
		if(jugadoresEdos != null ) {
			jugadoresEdos.clear();
		}
		if(lblCampeonato.getValue() != null && lblEquipodos.getValue() != null ) {
			List<Equipojugador> ej = equipoJugadorDao.getJugadorEquipoDos(lblCampeonato.getValue(), lblEquipodos.getValue());    //getJugadorEquipoUno(equiposeleccionado.getIdEquipo());
			if(!ej.isEmpty()) {
				for(Equipojugador equipojugador : ej) {
					jugadoresEdos.add(equipojugador.getJugador());					
				}
			}else {
				Clients.showNotification("No hay Jugadores inscritos en la lista.");
			}
		}
	}
	
			
	/** BOTONES PARA CAMBIOS DE JUGADOR*/
	/*@Command
	@NotifyChange({"cambioJugadorElocal"})*/
	@Listen("onClick=#agregarEuno")
	public void agregarEuno() {
		Cambio cambioJugador = new Cambio();
		cambioJugadorElocal.add(cambioJugador);				
	}	

	/*@Command
	@NotifyChange({"cambioJugadorElocal"})*/
	@Listen("onClick=#quitarEuno")
	public void quitarEuno(@BindingParam("cambioEuno") Cambio cambioJugador ) {	
		cambioJugadorElocal.remove(cambioJugador);				
	}	

	/*@Command
	@NotifyChange({"cambioJugadorEVisitante"})*/
	@Listen("onClick=#agregarEdos")
	public void agregarEdos() {
		Cambio cambioJugador = new Cambio();
		cambioJugadorEVisitante.add(cambioJugador);			
	}

	/*@Command
	@NotifyChange({"cambioJugadorEVisitante"})*/
	@Listen("onClick=#quitarEdos")
	public void quitarEdos(@BindingParam("cambioEdos") Cambio cambioJugador ) {		
		cambioJugadorEVisitante.remove(cambioJugador);				
	}	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	//@Command
	@Listen("onClick=#grabar")
	public void grabar() {
		/*if (campeonatoSeleccionado == null && categoriaSeleccionado != null) {
			Clients.showNotification("Por favor no se pueden guardar datos vacíos, seleccione campeonato, categoría y asigne equipo.");
			return;
		}*/
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						Partido partido = new Partido();
						partido.setIdPartido(0);
						partido.setDetallecalendario(detallecalendario);
						System.out.println("calendario: " + detallecalendario.getIdDetallecalendario());
						//partido.setFecha(Date.valueOf(fecha.getValue()));
						//System.out.println("fecha: " + fecha);
						//partido.setHora(sqlTime);
						//partido.setArbitro(arbitro);
						//partido.setVocal();
						//partido.setNovedadarbitro(novedadarbitro);
						//partido.setNovedadvocal(novedadvocal);
						List<Detallepartido> listaDetPartido = new ArrayList<Detallepartido>();
						
						for(Detallepartido det : lblJuno) {
							det.setGolpt(Integer.parseInt(txtgolEunopt.getText()));
							det.setGolst(Integer.parseInt(txtgolEunost.getText()));
							det.setPartido(partido);
							listaDetPartido.add(det);
						}
						
						for(Detallepartido det : lblJdos) {
							det.setGolpt(Integer.parseInt(txtgolEdospt.getText()));
							det.setGolst(Integer.parseInt(txtgolEdosSt.getText()));
							det.setPartido(partido);
							listaDetPartido.add(det);
						}
						partido.setDetallepartidos(listaDetPartido);
						partidoDao.getEntityManager().getTransaction().begin();
						partidoDao.getEntityManager().persist(partido);
						partidoDao.getEntityManager().getTransaction().commit();

						Clients.showNotification("Proceso Ejecutado con exito.");
						// Actualiza la lista
						//BindUtils.postGlobalCommand(null, null, "Detallecampeonato.buscarPorPatron", null);
						//salir();
					} catch (Exception e) {
						e.printStackTrace();						
						partidoDao.getEntityManager().getTransaction().rollback();
					}		
				}
			}
		});	
	}

	//@Command
	@Listen("onClick=#salir")
	public void salir(){
		winInscripcionequipoEditar.detach();
	}

	public Detallecalendario getDetallecalendario() {
		return detallecalendario;
	}

	public void setDetallecalendario(Detallecalendario detallecalendario) {
		this.detallecalendario = detallecalendario;
	}

	public List<Jugador> getJugadoresEuno() {
		return jugadoresEuno;
	}

	public void setJugadoresEuno(List<Jugador> jugadoresEuno) {
		this.jugadoresEuno = jugadoresEuno;
	}

	public List<Jugador> getJugadoresEdos() {
		return jugadoresEdos;
	}

	public void setJugadoresEdos(List<Jugador> jugadoresEdos) {
		this.jugadoresEdos = jugadoresEdos;
	}

	public EquipojugadorDAO getEquipoJugadorDao() {
		return equipoJugadorDao;
	}

	public void setEquipoJugadorDao(EquipojugadorDAO equipoJugadorDao) {
		this.equipoJugadorDao = equipoJugadorDao;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Equipojugador getEquipojugador() {
		return equipojugador;
	}

	public void setEquipojugador(Equipojugador equipojugador) {
		this.equipojugador = equipojugador;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}	

	public List<Cambio> getCambioJugadorElocal() {
		return cambioJugadorElocal;
	}

	public void setCambioJugadorElocal(List<Cambio> cambioJugadorElocal) {
		this.cambioJugadorElocal = cambioJugadorElocal;
	}

	public List<Cambio> getCambioJugadorEVisitante() {
		return cambioJugadorEVisitante;
	}

	public void setCambioJugadorEVisitante(List<Cambio> cambioJugadorEVisitante) {
		this.cambioJugadorEVisitante = cambioJugadorEVisitante;
	}

	public List<Jugador> getJugadorSEuno() {
		return jugadorSEuno;
	}

	public void setJugadorSEuno(List<Jugador> jugadorSEuno) {
		this.jugadorSEuno = jugadorSEuno;
	}
}