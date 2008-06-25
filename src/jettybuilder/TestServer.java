package jettybuilder;

import static jettybuilder.ServerBuilder.*;
import org.mortbay.jetty.Server;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TestServer {
    private ServerBuilder serverBuilder;
    private Server server;

    public TestServer(Integer port) {
        serverBuilder = new ServerBuilder().withPort(port);
    }

    public TestServer sleeping() {
        serverBuilder.with(webAppContext().with(servlet(new SleepingServlet())));
        return this;
    }

    public void start() throws Exception {
        server = serverBuilder.build();
        server.start();
    }

    public TestServer pageNotFound() {
        serverBuilder.with(webAppContext().with(servlet(new HttpErrorCodeServlet(HttpServletResponse.SC_NOT_FOUND))));
        return this;
    }

    public TestServer returningHttpStatusCode(int httpResponseCode) {
        serverBuilder.with(webAppContext().with(servlet(new HttpErrorCodeServlet(httpResponseCode))));
        return this;
    }

    public void stop() throws Exception {
        server.stop();
    }

    public TestServer happy(String content) {
        serverBuilder.with(webAppContext().with(servlet(new HappyServlet(content))));
        return this;
    }

    private class HttpErrorCodeServlet extends HttpServlet {
        private int errorCode;

        protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
            httpServletResponse.setStatus(errorCode);
        }

        public HttpErrorCodeServlet(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    private static class HappyServlet extends HttpServlet {
        private String content;

        public HappyServlet(String content) {
            this.content = content;
        }

        protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
            httpServletResponse.getWriter().write(content);
        }
    }
}
