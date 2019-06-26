package ec.edu.upse.gcf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import ec.edu.upse.gcf.dao.ClaseDAO;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class PrintReport {
	
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";

	/**
	 * Ejecuta un reporte.
	 * @param pathReporte
	 * @param parametros
	 * @param formato
	 */
	public static void ejecutaReporte(ClaseDAO claseDAO, 
			                          String pathReporte, Map<String, Object> parametros, 
			                          String formato) {
		// Almacena el nombre de archivo resultante a descargar.
		String nombreArchivo = null;

		// Obtiene la conexion con la base de datos para pasarsela a Jasper
		Connection cn = claseDAO.abreConexion();
		
		try {
			
			// Obtiene el path de la aplicacion.
			String pathAbsoluto = Executions.getCurrent()
					              .getDesktop().getWebApp()
					              .getRealPath("/");
			
			// Arma el path del reporte basado en el path de la aplicacion.
			String archivoReporte = pathAbsoluto + pathReporte;
			
			// Obtiene un nombre aleatorio para el reporte
			nombreArchivo = pathAbsoluto + "/temp/" + UUID.randomUUID().toString();
			
			if (parametros == null) {
				parametros = new HashMap<String, Object>();
			}
			
			// Coloca los parametros por defecto
			parametros.put("__NOMBRE_INSTITUCION__", "CampeonatoApp");
			
			// Selecciona el formato.
			if (formato.equals(FORMATO_PDF)) {
				nombreArchivo += ".pdf";
				byte[] b = null;
				b = JasperRunManager.runReportToPdf(archivoReporte, parametros, cn);
				FileOutputStream fos = new FileOutputStream(nombreArchivo);
				fos.write(b);
				fos.close();
			}else{
				// Si se genera en hoja electrónica.
				nombreArchivo += ".pdf";
		        JasperPrint jasperPrint = JasperFillManager.fillReport(archivoReporte, parametros, cn);
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
			}
			
			// Descarga el archivo.
			Filedownload.save(new File(nombreArchivo), formato); 
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
