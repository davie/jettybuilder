package jettybuilder.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class SleepingServlet implements Servlet {
    	public void destroy() {
	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public String getServletInfo() {
		return null;
	}

	public void init(ServletConfig unused) throws ServletException {

	}

	public void service(ServletRequest ignore, ServletResponse ignore2)
			throws ServletException, IOException {
        while(true){
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //don't give up
        }

    }
}