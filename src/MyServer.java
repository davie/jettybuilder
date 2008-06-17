
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import javax.servlet.*;
import java.io.IOException;

public class MyServer {
    private Server server;

    public MyServer() {

        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setServlet(new DummyServlet());
        servletHolder.setInitOrder(1);

        server = new Server(8081);
        WebAppContext context = new WebAppContext("/", "/");
        context.addServlet(servletHolder, "/*");
        server.addHandler(context);


    }

    private void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args){
        new MyServer().start();
    }


    public final class DummyServlet implements Servlet {
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

	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
        while(true){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //don't give up
        }

    }
}

}
