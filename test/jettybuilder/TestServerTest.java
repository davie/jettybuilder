package jettybuilder;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;

import static junit.framework.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;


public class TestServerTest {
    private TestServer testServer;

    @Before
    public void setUp(){
        testServer = new TestServer(8080);
    }

    @After
    public void tearDown() throws Exception {
        testServer.stop();
    }
    
    @Ignore(value = "this test doesn't seem to time out")
    @Test(timeout = 2000)
    public void sleepingTestServerShouldNotReturn() throws Exception {
        testServer.sleeping().start();
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(1000);
        GetMethod get = new GetMethod("http://127.0.0.1:8080");
        client.executeMethod(get);
        fail("Should have timed out");
    }

    @Test
    public void shouldReturnTheHttpStatusCodeSpecified() throws Exception {
        testServer.returningHttpStatusCode(HttpServletResponse.SC_NOT_FOUND).start();
        GetMethod get = new GetMethod("http://127.0.0.1:8080");
        HttpClient client = new HttpClient();
        client.executeMethod(get);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, get.getStatusCode());
    }

    @Test
    public void shouldProduceAForbidden401Error() throws Exception {
        testServer.returningHttpStatusCode(HttpServletResponse.SC_FORBIDDEN).start();
        GetMethod get = new GetMethod("http://127.0.0.1:8080");
        HttpClient client = new HttpClient();
        client.executeMethod(get);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, get.getStatusCode());
    }

    @Test
    public void happyServerShouldReturnCorrectContent() throws Exception {
        String content = "content";
        testServer.happy(content).start();
        GetMethod get = new GetMethod("http://127.0.0.1:8080");
        HttpClient client = new HttpClient();
        client.executeMethod(get);

        assertEquals(HttpServletResponse.SC_OK, get.getStatusCode());
        assertEquals(content, get.getResponseBodyAsString());
    }


    @Ignore
    @Test
    public void slowServerShouldReturnCorrectContentAfterTheDelay() throws Exception {
        testServer.delayed(2000, "content").start();
        GetMethod get = new GetMethod("http://127.0.0.1:8080");
        HttpClient client = new HttpClient();
        client.executeMethod(get);

        assertEquals(HttpServletResponse.SC_OK, get.getStatusCode());
        assertEquals("content", get.getResponseBodyAsString());
    }

    @Ignore
    @Test
    public void slowServerShouldNotReturnBeforeTheDelayPeriod() throws Exception {
        testServer.delayed(2000, "content").start();
        GetMethod get = new GetMethod("http://127.0.0.1:8080");

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(1000);
        client.executeMethod(get);
        fail("should have timed out");
    }
    // returns with a delay

    // redirect

    // slow proxy


}
