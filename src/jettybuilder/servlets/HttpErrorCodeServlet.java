package jettybuilder.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class HttpErrorCodeServlet extends HttpServlet {
    private int errorCode;

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setStatus(errorCode);
    }

    public HttpErrorCodeServlet(int errorCode) {
        this.errorCode = errorCode;
    }
}
