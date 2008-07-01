package jettybuilder;

import static jettybuilder.ServerBuilder.servlet;
import static jettybuilder.ServerBuilder.webAppContext;
import jettybuilder.servlets.SleepingServlet;
import org.mortbay.jetty.Server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public TestServer delayed(int delayMillis, String content) {
        serverBuilder.with(webAppContext().with(servlet(new DelayedServlet(delayMillis, content))));

        return this;
    }

    public TestServer redirectingTo(String url) {
        serverBuilder.with(webAppContext().with(servlet(new RedirectingServlet(url))));

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

    private class DelayedServlet extends HttpServlet {
        private final String content;
        private int delayMillis;

        public DelayedServlet(int delayMillis, String content) {
            this.delayMillis = delayMillis;
            this.content = content;
        }

        protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
            waitFor(delayMillis);
            httpServletResponse.getWriter().write(content);
        }

        private void waitFor(int delayMillis) {
            long wakeUpTime = System.currentTimeMillis() + delayMillis;

            while(System.currentTimeMillis() < wakeUpTime){
                sleepForMilliseconds(10);
            }
        }

        private void sleepForMilliseconds(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }

    private class RedirectingServlet extends HttpServlet {
        private String url;

        public RedirectingServlet(String url) {
            this.url = url;
        }

        protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
            httpServletResponse.sendRedirect(url);
        }
    }
}
