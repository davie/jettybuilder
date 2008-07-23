package jettybuilder;

import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertEquals;
import org.mortbay.jetty.Server;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNull;import static junit.framework.Assert.assertNotNull;
import static jettybuilder.ServerBuilder.*;
import jettybuilder.servlets.DummyServlet;

import javax.servlet.Servlet;

public class ServerBuilderTest {
    private Server server;

    @After
    public void tearDown() throws Exception {
        if(server != null){
            server.stop();
        }
    }

    @Test
    public void testThatServerIsNotStartedWhenBuilt(){
        server = new ServerBuilder().build();
        assertFalse("server should not be started", server.isStarted());
    }

    @Test
    public void testThatServerIsStartedIfSpecifiedWhenBuilt(){
        server = new ServerBuilder().started().build();
        assertTrue("server should be started", server.isStarted());
    }

    @Test
    public void testThatServerBuiltWithThePortSpecified(){
        int port = 9999;
        server = new ServerBuilder().withPort(port).build();
        assertEquals(port, server.getConnectors()[0].getPort());
    }


    @Test
    public void shouldBeAbleToStartServerContainingServlet() throws Exception {
        Servlet servlet = new DummyServlet();
        server = new ServerBuilder().withPort(8080).with(webAppContext().with(servlet(servlet))).build();
        server.start();
        assertTrue(server.isStarted());
    }
}
