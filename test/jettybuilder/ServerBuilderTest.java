package jettybuilder;

import org.junit.Test;
import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertEquals;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.Handler;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNull;import static junit.framework.Assert.assertNotNull;
import static jettybuilder.ServerBuilder.*;

import javax.servlet.Servlet;

public class ServerBuilderTest {

    @Test
    public void testThatServerIsNotStartedWhenBuilt(){
        Server server = new ServerBuilder().build();
        assertFalse("server should not be started", server.isStarted());
    }

    @Test
    public void testThatServerIsStartedIfSpecifiedWhenBuilt(){
        Server server = new ServerBuilder().started().build();
        assertTrue("server should be started", server.isStarted());
    }

    @Test
    public void testThatServerBuiltWithThePortSpecified(){
        int port = 9999;
        Server server = new ServerBuilder().withPort(port).build();
        assertEquals(port, server.getConnectors()[0].getPort());
    }

    @Test
    public void shouldBeAbleToStartServerContainingServlet() throws Exception {
        Servlet servlet = new DummyServlet();
        Server server = new ServerBuilder().withPort(8080).with(webAppContext().with(servlet(servlet))).build();
        server.start();
        assertTrue(server.isStarted());
    }


}
