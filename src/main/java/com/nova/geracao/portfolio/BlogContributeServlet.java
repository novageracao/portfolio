package com.nova.geracao.portfolio;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.nova.geracao.portfolio.core.MessageOutcome;
import com.nova.geracao.portfolio.core.MessageType;
import com.nova.geracao.portfolio.entities.BlogPost;

public class BlogContributeServlet extends HttpServlet {

	private static final long serialVersionUID = -7357195537807526147L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher("html/blog/new-post.html");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Key<BlogPost> key = null;
		Gson gson = new Gson();
		try{
			BlogPost blogPost = gson.fromJson(req.getParameter("blog_post"), BlogPost.class);
			key = ObjectifyService.ofy().save().entity(blogPost).now();
		} catch (Exception e){
			resp.getWriter().write(gson.toJson(new MessageOutcome(MessageType.ERROR, "O post não pôde ser gravado", e.getMessage())));
		}
		resp.getWriter().write(gson.toJson(new MessageOutcome(MessageType.SUCCESS, "Post adicionado com succeso", key.getId())));
	}

}
