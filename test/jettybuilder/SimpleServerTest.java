package jettybuilder;

import static junit.framework.Assert.assertEquals;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.net.SocketTimeoutException;


public class SimpleServerTest {
    private SimpleServer testServer;
    private static final int PORT_NUMBER = 8080;
    private static final int ONE_SECOND = 1000;
    private static final int TWO_SECONDS = 2000;

    @Before
    public void setUp(){
        testServer = new SimpleServer(PORT_NUMBER);
    }

    @After
    public void tearDown() throws Exception {
        testServer.stop();
    }

    @Test(timeout = TWO_SECONDS, expected = SocketTimeoutException.class)
    public void sleepingTestServerShouldNotReturn() throws Exception {
        testServer.sleeping().start();
        HttpClient client = httpClientWithTimeout(ONE_SECOND);
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
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


    @Test
    public void slowServerShouldReturnCorrectContentAfterTheDelay() throws Exception {
        testServer.delayed(TWO_SECONDS, "content").start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = new HttpClient();

        client.executeMethod(get);

        assertEquals(HttpServletResponse.SC_OK, get.getStatusCode());
        assertEquals("content", get.getResponseBodyAsString());
    }

    @Test(expected = SocketTimeoutException.class)
    public void slowServerShouldNotReturnBeforeTheDelayPeriod() throws Exception {
        testServer.delayed(TWO_SECONDS, "content").start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        HttpClient client = httpClientWithTimeout(1000);
        client.executeMethod(get);
        fail("should have timed out");
    }


    @Test
    public void shouldRedirectsToGivenUrl() throws Exception {
        String url = "http://some.other/url";
        testServer.redirectingTo(url).start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s", PORT_NUMBER));
        get.setFollowRedirects(false);

        new HttpClient().executeMethod(get);
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, get.getStatusCode());
        assertEquals(url, get.getResponseHeader("Location").getValue());
    }

    @Test
    public void shouldServeGivenPath() throws Exception {
        File file = File.createTempFile("test", ".txt");
        FileWriter writer = new FileWriter( file);
        String contents = "contents";
        writer.write(contents);
        writer.flush();

        testServer.servingPath(file.getParentFile().getAbsolutePath()).start();
        GetMethod get = new GetMethod(String.format("http://127.0.0.1:%s/%s", PORT_NUMBER, file.getName()));
        get.setFollowRedirects(false);

        new HttpClient().executeMethod(get);
        assertEquals(contents, get.getResponseBodyAsString());
    }

    @Test
    public void shouldServeGivenFile() throws Exception {
        File file = File.createTempFile("test", ".txt");
        FileWriter writer = new FileWriter( file);
        String contents = "contents";
        writer.write(contents);
        writer.flush();

        testServer.servingFile(file.getAbsolutePath()).start();
        // Note the trailing slash, probably need to change this.
        String url = String.format("http://127.0.0.1:%s/%s/", PORT_NUMBER, file.getName());
        GetMethod get = new GetMethod(url);
        get.setFollowRedirects(false);

        new HttpClient().executeMethod(get);
        assertEquals(contents, get.getResponseBodyAsString());
    }


}
