package com.want.utils;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.want.amqp.ConnectionManager;
 
public class WantContextListener  implements ServletContextListener{
	ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) {
		System.out.println(" ---- WANT Context Created");
		context = contextEvent.getServletContext();
		// set variable to servlet context
		//context.setAttribute("TEST", "TEST_VALUE");
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println(" ---- WANT Context Destroyed, stopping all threads");
		ConnectionManager.getInstance().closeThreads();
		try {
			ConnectionManager.getInstance().getConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}