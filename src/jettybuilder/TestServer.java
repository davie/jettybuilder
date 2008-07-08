package jettybuilder;

import static jettybuilder.ServerBuilder.servlet;
import static jettybuilder.ServerBuilder.webAppContext;
import jettybuilder.servlets.*;
import org.mortbay.jetty.Server;
import org.mortbay.component.LifeCycle;

public class TestServer {
    private ServerBuilder serverBuilder;
    private Server server;

    public TestServer(Integer port) {
        serverBuilder = new ServerBuilder().withPort(port);
    }

    public void start() throws Exception {
        server = serverBuilder.build();
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public TestServer sleeping() {
        serverBuilder.with(webAppContext().with(servlet(new SleepingServlet())));
        return this;
    }

    public TestServer returningHttpStatusCode(int httpResponseCode) {
        serverBuilder.with(webAppContext().with(servlet(new HttpErrorCodeServlet(httpResponseCode))));
        return this;
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

    public TestServer servingPath(String path) {
        serverBuilder.with(webAppContext().withContextRoot("/").withResourceBase(path));
        return this;
    }
}
