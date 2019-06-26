package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.control.Conexion;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Equipo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class EquipoLista {

	public String textoBuscar;
	private EquipoDAO equipoDao = new EquipoDAO();
	private List<Equipo> equipos ;
	private Equipo equipoSeleccionado;
	
	@Wire private Button btnEditar;
	@Wire private Button btnEliminar;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		deshabilitarBoton();
	}

	private void deshabilitarBoton() {
		btnEditar.setDisabled(true);
		btnEliminar.setDisabled(true);
	}
	
	@GlobalCommand("Equipo.buscarPorPatron")
	@Command
	@NotifyChange({"equipos", "equipoSeleccionado."})
	public void buscar(){
		if (equipos != null) {
			equipos = null; 
		}		
		equipos = equipoDao.getEquipos(textoBuscar);
		if(equipos.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}else {
			equipoSeleccionado = null; 
			btnEditar.setDisabled(false);
			btnEliminar.setDisabled(false);
		}
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipo", new Equipo());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/equipos/equipoEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(){
		if (equipoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una equipo.");
			return; 
		}
		equipoDao.getEntityManager().refresh(equipoSeleccionado);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipo", equipoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/equipos/equipoEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void eliminar(){
		if (equipoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una equipo.");
			return; 
		}
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						equipoDao.getEntityManager().getTransaction().begin();
						equipoSeleccionado.setEstado("1");
						equipoSeleccionado = equipoDao.getEntityManager().merge(equipoSeleccionado);
						equipoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						BindUtils.postGlobalCommand(null, null, "Equipo.buscarPorPatron", null);
					} catch (Exception e) {
						e.printStackTrace();
						equipoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	@Command
	public void verReporte() throws JRException {		
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				"C:\\Users\\Dayana Tigrero\\eclipse-workspace\\CampeonatoApp\\WebContent\\recursos\\reportes\\rptDetalle.jasper", null,
				Conexion.conectar());
		JRPdfExporter exp = new JRPdfExporter();
		exp.setExporterInput(new SimpleExporterInput(jasperPrint));
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput("Reporte.pdf"));
		SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
		exp.setConfiguration(conf);
		exp.exportReport();

		// se muestra en una ventana aparte para su descarga
		JasperPrint jasperPrintWindow = JasperFillManager.fillReport(
				"C:\\Users\\Dayana Tigrero\\eclipse-workspace\\CampeonatoApp\\WebContent\\recursos\\reportes\\rptDetalle.jasper", null,
				Conexion.conectar());
		JasperViewer jasperViewer = new JasperViewer(jasperPrintWindow);
		jasperViewer.setVisible(true);
		
		
	}

	public List<Equipo> getEquipos() {
		return equipos;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public void setEquipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}

	public Equipo getEquipoSeleccionado() {
		return equipoSeleccionado;
	}

	public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
		this.equipoSeleccionado = equipoSeleccionado;
	}
}
