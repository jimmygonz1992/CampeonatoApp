
package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.modelo.Jugador;
import ec.edu.upse.gcf.util.FileUtil;

public class JugadorEditar {

	@Wire private Window winJugadorEditar;
	@Wire private Textbox cedula;	
	@Wire private Textbox nombres;
	@Wire private Textbox edad;
	@Wire private Textbox numcamisa;
	@Wire private Datebox fechaNac;
	@Wire private Combobox posicionJuego;
	@Wire private Checkbox validar;

	private JugadorDAO jugadorDao = new JugadorDAO(); 
	private Jugador jugador;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		jugador = (Jugador) Executions.getCurrent().getArg().get("Jugador");		
	}

	public List<String> getGenero() {		
		List<String> genero = new ArrayList<String>();	
		genero.add("F");				
		genero.add("M");   
		return genero;					
	}

	public List<String> getPosicionJuego() {		
		List<String> posicion = new ArrayList<String>();	
		posicion.add("ARQUERO");	
		posicion.add("DEFENSA");
		posicion.add("DELANTERO");
		posicion.add("MEDIO CAMPISTA");		
		return posicion;					
	}

	public boolean isValidar() {
		try {
			Boolean retorna = true;
			if(cedula.getText().isEmpty()) {
				cedula.focus();
				return retorna;
			}
			if(nombres.getText().isEmpty()) {
				nombres.focus();
				return retorna;
			}			
			if(edad.getText().isEmpty()) {
				edad.focus();
				return retorna;
			}
			if(numcamisa.getText().isEmpty()) {
				numcamisa.focus();
				return retorna;
			}
			if(fechaNac.getText().isEmpty()) {
				fechaNac.focus();
				return retorna;
			}
			if(posicionJuego.getValue().isEmpty()) {
				posicionJuego.focus();
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** Validar si el usuario existe a trav�s de la cedula o pasaporte */
	boolean validarUsuarioExistente() {
		try {
			Jugador jugador  = jugadorDao.getValidarJugadorExistente(cedula.getText());
			if(jugador != null)
				return true;
			else
				return false;
		}catch(Exception ex) {
			return false;
		}
	}

	/*@SuppressWarnings("unused")
	private int calcularEdad(Calendar fechaNac) {
		Calendar today = Calendar.getInstance();
		int diffYear = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
		int diffMonth = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
		int diffDay = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
		// Si está en ese año pero todavía no los ha cumplido
		if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
			diffYear = diffYear - 1;
			
		}
		return diffYear;		
	}*/

	@Command
	public void calcularEdad(Calendar fechaNac) {
		Calendar fecha = Calendar.getInstance();
		if(jugador.getFechaNac() != null) {
			int anio = fecha.get(Calendar.YEAR) -  fechaNac.get(Calendar.YEAR);
			System.out.println("EDAD : " + anio);
		}			
	}

	/** M�todo para validar c�dula*/
	public boolean validarCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validaci�n c�dula
					// El decimo digito se lo considera d�gito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}
					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validacion");
			cedulaCorrecta = false;
		}
		if (!cedulaCorrecta) {
			System.out.println("La C�dula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}

	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			if(isValidar() == false) {
				jugadorDao.getEntityManager().getTransaction().begin();
				if (jugador.getIdJugador() == 0) {
					jugadorDao.getEntityManager().persist(jugador);
				}else{
					jugador = (Jugador) jugadorDao.getEntityManager().merge(jugador);
				}
				jugadorDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");			
				BindUtils.postGlobalCommand(null, null, "Jugadores.buscarPorPatron", null);
				salir();	
			}else {
				Clients.showNotification("No hay datos para guardar.!!");
			}					
		} catch (Exception e) {
			e.printStackTrace();
			jugadorDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Command
	@NotifyChange("imagen")
	public void subir(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento){
		String imagen = FileUtil.loadFile(evento.getMedia());
		if (imagen != null) {
			jugador.setFoto(imagen);
		}
	}

	public AImage getImagen() {
		AImage retorno = null;
		if (jugador.getFoto() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(jugador.getFoto()), 163, 163);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}

	@Command
	public void salir(){
		winJugadorEditar.detach();
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}	

}
