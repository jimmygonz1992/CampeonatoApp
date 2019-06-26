package ec.edu.upse.gcf.reportes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.ClaseDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.util.PrintReport;

public class reporteEquipos {
	ClaseDAO claseDAO = new ClaseDAO();
	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	private Campeonato campeonato;
	private Campeonato campeonatoSeleccionado;
	private Campeonato campSeleccionado;
	@Wire private Combobox cboCampeonato;

	@Command
	public void verReporteEquipos() {		
		// Crea un arreglo de parametros.
		Map<String, Object> parametros = new HashMap<String, Object>();
		// Coloca el parametro a pasar al reporte.
		parametros.put("nombreCampeonato",campeonatoSeleccionado.getNombreC());
		// Ejecuta el reporte.
		PrintReport.ejecutaReporte(campeonatoDao, 
				"/recursos/reportes/rptInscripcionEquipos.jasper", 
				parametros, 
				PrintReport.FORMATO_PDF);
	}	

	@Command
	public void verReporteJugadores() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nombreCampeonato",campSeleccionado.getNombreC());
		PrintReport.ejecutaReporte(campeonatoDao, 
				"/recursos/reportes/rptInscripcionJugadores.jasper", 
				parametros, 
				PrintReport.FORMATO_PDF);
	}	

	@Command
	public void verReporteListadoEquipos() {
		PrintReport.ejecutaReporte(campeonatoDao, "/recursos/reportes/rptListadoEquipos.jasper", null, PrintReport.FORMATO_PDF);
	}	

	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}

	public Campeonato getCampSeleccionado() {
		return campSeleccionado;
	}

	public void setCampSeleccionado(Campeonato campSeleccionado) {
		this.campSeleccionado = campSeleccionado;
	}
}
