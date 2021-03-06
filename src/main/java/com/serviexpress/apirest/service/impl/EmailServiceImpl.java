package com.serviexpress.apirest.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;

import com.serviexpress.apirest.service.EmailService;



@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
    TemplateEngine templateEngine; // From Thymeleaf
    
	@Autowired
    JavaMailSender mailSender;
    
	@Override
	public void emailSend(String email, String name, String usuario, String pass) {
		String processedHTMLTemplate = this.constructHTMLTemplate(name, usuario, pass);

		
			MimeMessagePreparator preparator = message -> {
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
						"UTF-8");
				helper.setFrom("serviexpress2080@gmail.com");
				helper.setTo(email);
					helper.setSubject("Hola "+ name+ " Bienvenido");
				helper.setText(processedHTMLTemplate, true);
			};

			try {
				System.out.println("Enviando email a " + email);
				mailSender.send(preparator);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

	

	}

	// Fills up the HTML file
	private String constructHTMLTemplate(String name,String usuario, String pass) {

		Context context = new Context();
		context.setVariable("name", name);
		context.setVariable("usuario", usuario);
		context.setVariable("pass", pass);
		return templateEngine.process("MyHTML", context);
	}

	@Override
	public void emailSendReserva(String email, String name, String mensaje, String estado) {
		String processedHTMLTemplate = this.constructHTMLreserva(name, mensaje, estado);

		
		MimeMessagePreparator preparator = message -> {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
					"UTF-8");
			helper.setFrom("serviexpress2080@gmail.com");
			helper.setTo(email);
				helper.setSubject("Hola "+ name+ " "+mensaje+" "+estado);
			helper.setText(processedHTMLTemplate, true);
		};

		try {
			System.out.println("Enviando email a " + email);
			mailSender.send(preparator);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private String constructHTMLreserva(String name,String mensaje, String estado) {

		Context context = new Context();
		context.setVariable("name", name);
		context.setVariable("mensaje", mensaje);
		context.setVariable("estado", estado);
		return templateEngine.process("MyHTML2", context);
	}

	@Override
	public void emailSendEncuesta(String email, String name, String mensaje, String estado) {
		String processedHTMLTemplate = this.constructHTMLEncuesta(name, mensaje, estado);

		
		MimeMessagePreparator preparator = message -> {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
					"UTF-8");
			helper.setFrom("serviexpress2080@gmail.com");
			helper.setTo(email);
				helper.setSubject("Hola "+ name);
			helper.setText(processedHTMLTemplate, true);
		};

		try {
			System.out.println("Enviando email a " + email);
			mailSender.send(preparator);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private String constructHTMLEncuesta(String name,String mensaje, String estado) {

		Context context = new Context();
		context.setVariable("name", name);
		context.setVariable("mensaje", mensaje);
		context.setVariable("estado", estado);
		return templateEngine.process("MyHTML3", context);
	}
    
}