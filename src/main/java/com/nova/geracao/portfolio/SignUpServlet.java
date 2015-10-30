package com.nova.geracao.portfolio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.nova.geracao.portfolio.core.MessageOutcome;
import com.nova.geracao.portfolio.core.MessageType;
import com.nova.geracao.portfolio.entities.User;
import com.nova.geracao.portfolio.security.SecurityManager;

public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = -6974926352540971388L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("user-name");
		String email = req.getParameter("user-email");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
		Query query = new Query("User").setFilter(emailFilter);
		PreparedQuery pq = datastore.prepare(query);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
//		for (Entity entity : result) {
//			ObjectifyService.ofy().delete().entity(entity).now();
//		}
//		result.clear();
		Gson gson = new Gson();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("json");
		if(result.isEmpty()){
			//Save user 
			String pass = RandomStringUtils.random(8, true, true);
			String confirmCode = SecurityManager.getInstance().getRandomCode();
			User user = new User(username, email, pass,confirmCode, false); 
			Key<User> key = ObjectifyService.ofy().save().entity(user).now();
			System.out.println("Id: "+key.getId());
			
			//Send email
			String confirmLink = req.getServerName() + "/confirmar-conta?code=" + key.getId() + "-" + confirmCode;
			sendConfirmationEmail(username, email, pass, confirmLink);
			resp.getWriter().write(gson.toJson(new MessageOutcome(MessageType.SUCCESS, "Yay! Você fez seu registro no Nova Geração com sucesso! Enviamos o link de ativação da"
							+ " conta para seu email! :)", null)));
		} else {
			resp.getWriter().write(gson.toJson(new MessageOutcome(MessageType.ERROR, "Já existe um usuário registrado com esse email.", null)));
		}
	}

	/**
	 * Send confirmation email with generated password and confirmation link.
	 */
	private void sendConfirmationEmail(String username, String email, String pass, String confirmLink) {
		try {
			Session session = Session.getDefaultInstance(new Properties(), null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ismael.sarmento.jr@gmail.com", "Nova Geração Tecnologias"));
			System.out.println("TO: " +email);
			message.addRecipient(RecipientType.TO, new InternetAddress(email, username));
			message.setSubject("Confirmação de conta - Nova Geração");
			message.setText("<html><head></head><body>msgBody</body></html>", "UTF-8", "html");
			System.out.println("Msg: "+message);
			Transport.send(message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	

}
