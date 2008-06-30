package jettybuilder;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.methods.GetMethod;

import static junit.framework.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;


public class TestServerTest {
    private TestServer testServer;
    private static final int PORT_NUMBER = 8080;
    private static final int ONE_SECOND = 1000;
    private static final int TWO_SECONDS = 2000;

    @Before
    public void setUp(){
        testServer = new TestServer(PORT_NUMBER);
    }

    @After
    public void tearDown() throws Exception {
        testServer.stop();
    }
    
    @Test(timeout = 5000, expected = SocketTimeoutException.class)
    public void sleepingTestServerShouldNotReturn() throws Exception {
        testServer.sleeping().start();
        HttpClient client = httpClientWithTimeout(ONE_SECOND);
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClientParams clientParams = new HttpClientParams();
        get.setParams(clientParams);
        client.executeMethod(get);
        fail("Should have timed out");
    }

    private HttpClient httpClientWithTimeout(int timeout) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        return client;
    }

    @Test
    public void shouldReturnTheHttpStatusCodeSpecified() throws Exception {
        testServer.returningHttpStatusCode(HttpServletResponse.SC_NOT_FOUND).start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();
        client.executeMethod(get);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, get.getStatusCode());
    }

    @Test
    public void shouldProduceAForbidden401Error() throws Exception {
        testServer.returningHttpStatusCode(HttpServletResponse.SC_FORBIDDEN).start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();
        client.executeMethod(get);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, get.getStatusCode());
    }

    @Test
    public void happyServerShouldReturnCorrectContent() throws Exception {
        String content = "content";
        testServer.happy(content).start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();
        client.executeMethod(get);

        assertEquals(HttpServletResponse.SC_OK, get.getStatusCode());
        assertEquals(content, get.getResponseBodyAsString());
    }


    @Ignore
    @Test
    public void slowServerShouldReturnCorrectContentAfterTheDelay() throws Exception {
        testServer.delayed(TWO_SECONDS, "content").start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();

        client.executeMethod(get);

        assertEquals(HttpServletResponse.SC_OK, get.getStatusCode());
        assertEquals("content", get.getResponseBodyAsString());
    }

    @Ignore
    @Test
    public void slowServerShouldNotReturnBeforeTheDelayPeriod() throws Exception {
        testServer.delayed(TWO_SECONDS, "content").start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(ONE_SECOND);
        client.executeMethod(get);
        fail("should have timed out");
    }
    
    // redirect

    // returns with a delay

    // slow proxy


}
