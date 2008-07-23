package jettybuilder;

import static jettybuilder.ServerBuilder.servlet;
import static jettybuilder.ServerBuilder.webAppContext;
import jettybuilder.servlets.*;
import org.mortbay.jetty.Server;

import java.io.*;

public class SimpleServer {
    private ServerBuilder serverBuilder;
    private Server server;

    public SimpleServer(Integer port) {
        serverBuilder = new ServerBuilder().withPort(port);
    }

    public void start() throws Exception {
        server = serverBuilder.build();
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public SimpleServer sleeping() {
        serverBuilder.with(webAppContext().with(servlet(new SleepingServlet())));
        return this;
    }

    public SimpleServer returningHttpStatusCode(int httpResponseCode) {
        serverBuilder.with(webAppContext().with(servlet(new HttpErrorCodeServlet(httpResponseCode))));
        return this;
    }

    public SimpleServer happy(String content) {
        serverBuilder.with(webAppContext().with(servlet(new HappyServlet(content))));
        return this;
    }

    public SimpleServer delayed(int delayMillis, String content) {
        serverBuilder.with(webAppContext().with(servlet(new DelayedServlet(delayMillis, content))));
        return this;
    }

    public SimpleServer redirectingTo(String url) {
        serverBuilder.with(webAppContext().with(servlet(new RedirectingServlet(url))));
        return this;
    }

    public SimpleServer servingPath(String path) {
        serverBuilder.with(webAppContext().withContextRoot("/").withResourceBase(path));
        return this;
    }

    public SimpleServer servingFile(String fullPath) {
        String content = null;
        try {
            content = readFileQuietly(fullPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String fileName = new File(fullPath).getName();
        serverBuilder.with(webAppContext().withContextRoot("/" + fileName).with(servlet(new HappyServlet(content))));
        return this;
    }

    private String readFileQuietly(String fullPath) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(fullPath);
        StringWriter writer = new StringWriter();
        try {
            int n;
            while(-1 != (n = inputStream.read())){
                writer.write(n);
            }
        } catch (IOException e) {
          // ignore
        }
        return writer.toString();
    }

}
