package jettybuilder.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class DelayedServlet extends HttpServlet {
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
