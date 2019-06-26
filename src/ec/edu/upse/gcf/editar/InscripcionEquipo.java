package ec.edu.upse.gcf.editar;

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

import ec.edu.upse.gcf.control.Conexion;
import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Detallecampeonato;
import ec.edu.upse.gcf.modelo.Equipo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class InscripcionEquipo {

	@Wire private Window winDetalleCampeonatoEquipoEditar;

	private DetalleCampeonatoDAO detallecampeonatoDao = new DetalleCampeonatoDAO();
	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();	
	private CategoriaDAO categoriaDao = new CategoriaDAO();	
	private EquipoDAO equipoDao = new EquipoDAO();

	private Detallecampeonato detallecampeonato = new Detallecampeonato();
	//lista de equipos
	private List<Equipo> equiposInscritos = new ArrayList<>();
	private List<Equipo> equiposDisponibles = new ArrayList<>();
	private List<Categoria> categorias = new ArrayList<>();


	//para los cuadritos
	private Campeonato campeonatoSeleccionado;
	private Categoria categoriaSeleccionado;

	private Equipo equipoSeleccionado;
	private Equipo quitarequipo;

	private Campeonato campeonato;
	private Categoria categoria;
	private Equipo equipo;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");	
		equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");
	}	

	@Command
	@NotifyChange({"campeonatoSeleccionado","categorias"})
	public void listarcategorias() {
		if(categorias != null) {
			categorias.clear();
		}		
		
		if(campeonatoSeleccionado != null) {
			List<Categoria> listaCategoria = categoriaDao.getListaCategorias();			 
			if(!listaCategoria.isEmpty()) {
				for(Categoria cat : listaCategoria) {
					if(cat.getTipo().equals(campeonatoSeleccionado.getTipoCampeonato())) {
						categorias.add(cat);
					}
				}	
			}
		}
	}

	@Command
	@NotifyChange({"equiposDisponibles", "equiposInscritos","campeonatoSeleccionado"})
	public void listarequipos() {
		if(equiposInscritos != null) {
			equiposInscritos.clear();
		}

		if(equiposDisponibles != null) {
			equiposDisponibles.clear();
		}
		if(campeonatoSeleccionado != null && categoriaSeleccionado != null) {
			List<Detallecampeonato> opcion = detallecampeonatoDao.getCampeonatoEquipo(campeonatoSeleccionado.getIdCampeonato(), categoriaSeleccionado.getIdCategoria());
			if(!opcion.isEmpty()) {
				for(Detallecampeonato detallecampeonato : opcion) {
					equiposInscritos.add(detallecampeonato.getEquipo());
				}
			}
			List<Equipo> equipos = equipoDao.getEquiposDisponibles(campeonatoSeleccionado, categoriaSeleccionado);
			if(!equipos.isEmpty()) {
				for(Equipo objeto : equipos) {
					equiposDisponibles.add(objeto);
				}
			}
		}
	}

	@Command
	@NotifyChange({"equipoSeleccionado", "equiposDisponibles", "equiposInscritos"})
	public void agregar() {
		if(equipoSeleccionado != null) {
			//if(jugadorseleccionado.getEdad().equals(categoriaseleccionada.getDescripcion())) {
			equiposInscritos.add(equipoSeleccionado);
			equiposDisponibles.remove(equipoSeleccionado);	
			equipoSeleccionado = null;
			//}else {
			//	Clients.showNotification("El jugador(a) no puede asignarse en la categoría seleccionada.");
			//}			
		}
	}

	@Command
	@NotifyChange({"equiposDisponibles", "equiposInscritos"})
	public void agregartodo () {
		if (!equiposDisponibles.isEmpty()) {
			for (Equipo e : equiposDisponibles) {
				equiposInscritos.add(e);				
			}
			equiposDisponibles = new ArrayList<>();
		}	
	}

	@Command
	@NotifyChange({"quitarequipo", "equiposDisponibles", "equiposInscritos"})
	public void quitar () {
		if(quitarequipo != null) {		
			equiposDisponibles.add(quitarequipo);
			equiposInscritos.remove(quitarequipo);
			quitarequipo = null;

		}
	}

	@Command
	@NotifyChange({"equiposDisponibles", "equiposInscritos"})
	public void quitartodo () {		
		if (!equiposInscritos.isEmpty()) {

			for (Equipo e : equiposInscritos) {
				equiposDisponibles.add(e);	
			}
			equiposInscritos = new ArrayList<>();

		}	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void grabar(){
		if (campeonatoSeleccionado == null && categoriaSeleccionado != null) {
			Clients.showNotification("Por favor no se pueden guardar datos vacíos, seleccione campeonato, categoría y asigne equipo.");
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {			

						if (!equiposInscritos.isEmpty()) {
							for (Equipo e : equiposInscritos) {
								Detallecampeonato dc = detallecampeonatoDao.getEquipos(campeonatoSeleccionado.getIdCampeonato(),categoriaSeleccionado.getIdCategoria(),e.getIdEquipo());
								if (dc == null) {
									Detallecampeonato graba = new Detallecampeonato();
									graba.setId_detalleC(0);
									graba.setEquipo(e);			
									graba.setCampeonato(campeonatoSeleccionado);
									graba.setCategoria(categoriaSeleccionado);
									detallecampeonatoDao.getEntityManager().getTransaction().begin();
									detallecampeonatoDao.getEntityManager().persist(graba);
									detallecampeonatoDao.getEntityManager().getTransaction().commit();												
								}else {
									dc.setEquipo(e);
									dc.setCampeonato(campeonatoSeleccionado);
									dc.setCategoria(categoriaSeleccionado);
									detallecampeonatoDao.getEntityManager().getTransaction().begin();
									detallecampeonatoDao.getEntityManager().merge(dc);
									detallecampeonatoDao.getEntityManager().getTransaction().commit();
								}
							}
							if (!equiposDisponibles.isEmpty()) {
								for (Equipo e : equiposDisponibles) {
									Detallecampeonato dc = detallecampeonatoDao.getEquipos(campeonatoSeleccionado.getIdCampeonato(),categoriaSeleccionado.getIdCategoria(),e.getIdEquipo());
									if (dc != null) {
										dc.setEstado("1");
										detallecampeonatoDao.getEntityManager().getTransaction().begin();
										detallecampeonatoDao.getEntityManager().merge(dc);
										detallecampeonatoDao.getEntityManager().getTransaction().commit();
									}
								}	
							}
						}
						Clients.showNotification("Proceso Ejecutado con exito.");
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Detallecampeonato.buscarPorPatron", null);
						salir();
					} catch (Exception e) {
						e.printStackTrace();						
						detallecampeonatoDao.getEntityManager().getTransaction().rollback();
					}		
				}
			}
		});	
	}

	@Command
	public void verReporte() throws JRException{
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				"C:\\Users\\Dayana Tigrero\\eclipse-workspace\\CampeonatoApp\\WebContent\\recursos\\reportes\\rptInscripcionEquipos.jasper", null,
				Conexion.conectar());
		JRPdfExporter exp = new JRPdfExporter();
		exp.setExporterInput(new SimpleExporterInput(jasperPrint));
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput("Reporte.pdf"));
		SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
		exp.setConfiguration(conf);
		exp.exportReport();

		// se muestra en una ventana aparte para su descarga
		JasperPrint jasperPrintWindow = JasperFillManager.fillReport(
				"C:\\Users\\Dayana Tigrero\\eclipse-workspace\\CampeonatoApp\\WebContent\\recursos\\reportes\\rptInscripcionEquipos.jasper", null,
				Conexion.conectar());
		JasperViewer jasperViewer = new JasperViewer(jasperPrintWindow);
		jasperViewer.setVisible(true);

	}	

	@Command
	public void salir(){
		winDetalleCampeonatoEquipoEditar.detach();
	}

	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}

	public EquipoDAO getEquipoDao() {
		return equipoDao;
	}

	public void setEquipoDao(EquipoDAO equipoDao) {
		this.equipoDao = equipoDao;
	}

	public List<Equipo> getEquiposInscritos() {
		return equiposInscritos;
	}

	public void setEquiposInscritos(List<Equipo> equiposInscritos) {
		this.equiposInscritos = equiposInscritos;
	}

	public List<Equipo> getEquiposDisponibles() {
		return equiposDisponibles;
	}

	public void setEquiposDisponibles(List<Equipo> equiposDisponibles) {
		this.equiposDisponibles = equiposDisponibles;
	}

	public DetalleCampeonatoDAO getDetallecampeonatoDao() {
		return detallecampeonatoDao;
	}

	public void setDetallecampeonatoDao(DetalleCampeonatoDAO detallecampeonatoDao) {
		this.detallecampeonatoDao = detallecampeonatoDao;
	}

	public CampeonatoDAO getCampeonatoDao() {
		return campeonatoDao;
	}

	public void setCampeonatoDao(CampeonatoDAO campeonatoDao) {
		this.campeonatoDao = campeonatoDao;
	}

	public Detallecampeonato getDetallecampeonato() {
		return detallecampeonato;
	}

	public void setDetallecampeonato(Detallecampeonato detallecampeonato) {
		this.detallecampeonato = detallecampeonato;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}

	public Equipo getEquipoSeleccionado() {
		return equipoSeleccionado;
	}

	public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
		this.equipoSeleccionado = equipoSeleccionado;
	}

	public Equipo getQuitarequipo() {
		return quitarequipo;
	}

	public void setQuitarequipo(Equipo quitarequipo) {
		this.quitarequipo = quitarequipo;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Categoria getCategoriaSeleccionado() {
		return categoriaSeleccionado;
	}

	public void setCategoriaSeleccionado(Categoria categoriaSeleccionado) {
		this.categoriaSeleccionado = categoriaSeleccionado;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

}
