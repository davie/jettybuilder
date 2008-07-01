package jettybuilder.servlets;

import jettybuilder.exceptions.Defect;

import javax.servlet.*;
import java.io.IOException;

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
                throw new Defect("got interrupted while sleeping", e);
            }
        }

    }
}