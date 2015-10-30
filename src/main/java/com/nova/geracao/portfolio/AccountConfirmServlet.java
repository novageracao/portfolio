package com.nova.geracao.portfolio;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.nova.geracao.portfolio.core.MessageOutcome;
import com.nova.geracao.portfolio.core.MessageType;

public class AccountConfirmServlet extends HttpServlet {

	private static final long serialVersionUID = 4256039087080611035L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		Gson gson = new Gson();
		if(code == null){
			req.getRequestDispatcher("");
			resp.getWriter().write(
					gson.toJson(new MessageOutcome(MessageType.ERROR, "Esse link está correntemente invalidado", null)));
			resp.getWriter().flush();//send redirect instead
		}
		String[] codes = code.split("-");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FilterPredicate filter = new FilterPredicate("id", FilterOperator.EQUAL, codes[0]);
		Query query = new Query("User").setFilter(filter);
		PreparedQuery pq = datastore.prepare(query);
		Entity user = pq.asSingleEntity();
		String confirmCode = (String) user.getProperty("confirmCode");
		if(codes[1] != null && codes[1].equals(confirmCode)){
			user.setProperty("accountConfirmed", true);
			datastore.put(user);
			resp.getWriter().write(
					gson.toJson(new MessageOutcome(MessageType.SUCCESS, "Registro e Email confirmados", null)));
			resp.getWriter().flush();//send redirect instead
		} else {
			resp.getWriter().write(
					gson.toJson(new MessageOutcome(MessageType.ERROR, "Esse link está correntemente invalidado", null)));
			resp.getWriter().flush();//send redirect instead
		}
	}

}
