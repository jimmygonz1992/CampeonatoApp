package ec.edu.upse.gcf.login;

import java.util.Random;

import org.zkforge.bwcaptcha.Captcha;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.editar.UsuarioEditar;
import ec.edu.upse.gcf.modelo.Usuario;


public class Recuperar{

	@Wire Button enviar;
	@Wire Textbox txtCaptcha;
	@Wire Textbox correoC;
	@Wire Captcha cap;
	@WireVariable
	private UsuarioDao correousuario;

	private String captcha;
	private String correo;
	private String textoCaptcha;
	private boolean textoCaptchaControl;
	private boolean buttonIngresar;

	private UsuarioEditar usuarioo = new UsuarioEditar();
	private UsuarioDao usuarioDao = new UsuarioDao();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		setCaptcha(getRandomString());
		setButtonIngresar(true);
		setTextoCaptchaControl(false);		
	}


	public boolean isValidarD() {
		try {
			Boolean resultado = true;	
			if(cap.getValue() != txtCaptcha.getText()) {
				Clients.showNotification("EL texto del captcha no coincide.!!");
				return resultado;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public String getRandomString() {
		int lenght = 5;
		String alfabeto = "abcdefghijklmnñopqrstuvwxyz";
		Random rn = new Random();
		StringBuilder sb = new StringBuilder(lenght);

		for(int i=0; i<lenght; i++) {
			sb.append(alfabeto.charAt(rn.nextInt(alfabeto.length())));
		}		
		return sb.toString();		
	}

	@Command
	@NotifyChange("*")
	public void actualizarCaptcha(@BindingParam("valor") Captcha valor){
		setCaptcha(getRandomString());
		textoCaptcha = "";
		setButtonIngresar(true);
		setTextoCaptchaControl(false);
	}

	@Command
	public void enviarMail() throws Exception{
		if(txtCaptcha.getText().isEmpty() && correoC.getText().isEmpty()) {
			Clients.showNotification("No hay datos para guardar");			
		}
		UsuarioDao usu = new UsuarioDao();
		EmailSenderService ess = new EmailSenderService();
		String correoUsuario = correo.trim();
		Usuario usu2 = usu.getUsuarioCorreo(correoUsuario);
		if (usu2 == null) {
			Clients.showNotification("El correo electrónico ingresado no está registrado en nuestro sistema. Por favor ingrese otro");
			return;
		}	
		if(ess.enviarCorreo(usu2.getCorreo().trim(),usu2.getNombres() + usu2.getApellidos())){
			String resultado = usuarioo.encriptar(VariableGeneral.getNumeroAleatorio().toString());
			usuarioDao.getEntityManager().getTransaction().begin();

			usu2.setClave(resultado);
			usuarioDao.getEntityManager().merge(usu2);
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Mail enviado con Exito");
			Executions.getCurrent().sendRedirect("/login.zul");

		}else{
			Clients.showNotification("Ha ocurrido un error al momento de restablecer la contraseña . Por favor intente más tarde");
		}

	}

	//GETTER AND SETTER
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTextoCaptcha() {
		return textoCaptcha;
	}

	public void setTextoCaptcha(String textoCaptcha) {
		this.textoCaptcha = textoCaptcha;
	}

	public boolean isTextoCaptchaControl() {
		return textoCaptchaControl;
	}

	public void setTextoCaptchaControl(boolean textoCaptchaControl) {
		this.textoCaptchaControl = textoCaptchaControl;
	}

	public boolean isButtonIngresar() {
		return buttonIngresar;
	}

	public void setButtonIngresar(boolean buttonIngresar) {
		this.buttonIngresar = buttonIngresar;
	}
}
