/**
 * Copyright 2014-2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//[START all]
package com.nova.geracao.portfolio;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.nova.geracao.portfolio.entities.BaseDataClass;
import com.nova.geracao.portfolio.entities.BlogPost;

public class BlogServlet extends HttpServlet {

	private static final long serialVersionUID = -135873361334621489L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Entity newestPost = findNewestPost();
//		if(newestPost == null){
//			BlogPost post = new Gson().fromJson(post1, BlogPost.class);
//			Key<BlogPost> key = ObjectifyService.ofy().save().entity(post).now();
//		}
		
		String month = req.getParameter("month");
		String tag = req.getParameter("tag");
		String order = req.getParameter("order");
		if(order != null){
			String pid = req.getParameter("pid");
			if(pid != null){
				Long id = null;
				if("prev".equalsIgnoreCase(order)){
					id = Long.parseLong(pid) - 1;
				} else if("next".equalsIgnoreCase(order)){
					id = Long.parseLong(pid) + 1;
				}
				BlogPost result = ObjectifyService.ofy().load().type(BlogPost.class).id(id).now();
				resp.getWriter().write(new Gson().toJson(result));
			} else if("last".equals(order)){
				Iterable<Entity> result = findNewestPost();
				if(result.iterator().hasNext()){
					BlogPost post = (BlogPost) getInstanceFromPropertiesMap(result.iterator().next(), BlogPost.class);
					resp.getWriter().write(new Gson().toJson(post));
				}
			} 
		} else if(month != null){
			Iterable<Entity> posts = findPostsByMonth(Integer.parseInt(month));
			resp.getWriter().write(new Gson().toJson(posts));
		} else if(tag != null){
			Iterable<Entity> posts = findPostsByTag(tag);
			resp.getWriter().write(new Gson().toJson(posts));
		}  else {
			RequestDispatcher dispatcher = req.getRequestDispatcher("html/blog/index.html");
			dispatcher.forward(req, resp);
		}
	}
	
	private Object getInstanceFromPropertiesMap(Entity entity, Class<?> klass) {
		Object result = null;
		try {
			Field[] fields = klass.getDeclaredFields();
			result = klass.newInstance();
			for (int i = 0; i < fields.length; i++) {
				if(entity.hasProperty(fields[i].getName())){
					fields[i].setAccessible(true);
					Object value = entity.getProperty(fields[i].getName());
					if(!ClassUtils.isAssignable(fields[i].getType(), BaseDataClass.class)){
						fields[i].set(result, value);
					} else{
						Object embedValue = getInstanceFromPropertiesMap((EmbeddedEntity) value, fields[i].getType());
						fields[i].set(result, embedValue);
					}
				}
			}
			
			Field fieldId = klass.getDeclaredField("id");
			fieldId.setAccessible(true);
			fieldId.set(result, entity.getKey().getId());
		} catch (InstantiationException e) {
			e.printStackTrace(); 
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Object getInstanceFromPropertiesMap(EmbeddedEntity entity, Class<?> klass) {
		Object result = null;
		try {
			Field[] fields = klass.getDeclaredFields();
			result = klass.newInstance();
			for (int i = 0; i < fields.length; i++) {
				if(entity.hasProperty(fields[i].getName())){
					fields[i].setAccessible(true);
					Object value = entity.getProperty(fields[i].getName());
					if(!ClassUtils.isAssignable(fields[i].getType(), BaseDataClass.class)){
						fields[i].set(result, value);
					} else{
						Object embedValue = getInstanceFromPropertiesMap((EmbeddedEntity) value, fields[i].getType());
						fields[i].set(result, embedValue);
					}
				}
			}
			
			Field fieldId = klass.getDeclaredField("id");
			fieldId.setAccessible(true);
			fieldId.set(result, entity.getKey().getId());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Iterable<Entity> findNewestPost() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("BlogPost");//.addSort("id", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asIterable(FetchOptions.Builder.withLimit(1));
	}

	private Iterable<Entity> findPostsByTag(String tag) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FilterPredicate filter = new Query.FilterPredicate("tags", FilterOperator.IN, tag);
		Query q = new Query("BlogPost").setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asList(FetchOptions.Builder.withLimit(10));
	}

	public static void main(String[] args) {
//		BlogPost blogPost = new BlogPost();
//		BlogTag tag = new BlogTag();
//		tag.setId(1l);
//		tag.setName("tag1");
//		BlogTag tag2 = new BlogTag();
//		tag2.setId(2l);
//		tag2.setName("tag2");
//		blogPost.setBody("this is the body of one post");
//		blogPost.setDay(1);
//		blogPost.setMonth(12);
//		blogPost.setYear(1999);
//		blogPost.setId(12l);
//		HashSet<BlogTag> tags = new HashSet<BlogTag>();
//		tags.add(tag);
//		tags.add(tag2);		
//		blogPost.setTags(tags);
//		BlogPostAuthor author = new BlogPostAuthor();
//		author.setBio("this is the author bio");
//		author.setId(1l);
//		author.setName("john");
//		blogPost.setAuthor(author );
//		String json = new Gson().toJson(blogPost);
//		System.out.println(json);
	}

	private Iterable<Entity> findPostsByMonth(Integer month) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FilterPredicate filter = new Query.FilterPredicate("month", FilterOperator.EQUAL, month);
		Query q = new Query("BlogPost").setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asList(FetchOptions.Builder.withLimit(10));
	}
 
}
