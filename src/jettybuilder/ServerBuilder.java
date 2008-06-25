package jettybuilder;

import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerCollection;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;

import javax.servlet.Servlet;

public class ServerBuilder {
		private int port;
        List<WebAppContext> contexts = new ArrayList<WebAppContext>();
        private boolean shouldStart;

        public ServerBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public static WebAppContext webAppContext(String name, String contextPath, String resourceBase) {
            WebAppContext context = new WebAppContext(name, contextPath);
            context.setResourceBase(new File(resourceBase).getAbsolutePath());
            return context;
        }

        public static WebAppContextBuilder  webAppContext() {
            return new WebAppContextBuilder().withContextRoot("/");
        }

        public ServerBuilder with(WebAppContext webAppContext) {
            contexts.add(webAppContext);
            return this;
        }

        public static class WebAppContextBuilder {
            private String name = "/";
            private String contextRoot = "/";
            private String resourceBase;
            // TODO Should allow a list of servlets
			private ServletHolder servletHolder;

            public WebAppContextBuilder withName(String name) {
                this.name = name;
                return this;
            }

            public WebAppContextBuilder withContextRoot(String contextRoot) {
                this.contextRoot = contextRoot;
                return this;
            }
            public WebAppContextBuilder withResourceBase(String resourceBase) {
                this.resourceBase = resourceBase;
                return this;
            }

            public WebAppContext build(){
                WebAppContext context = new WebAppContext(name, contextRoot);
                if(resourceBase != null)
                    context.setResourceBase(resourceBase);
                if(servletHolder != null){
                	context.addServlet(servletHolder, "/*");
                }
                return context;
            }

			public WebAppContextBuilder with(ServletHolder servletHolder) {
				this.servletHolder = servletHolder;
				return this;
			}
			
			public WebAppContextBuilder with(ServletHolderBuilder servletHolderBuilder) {
				this.servletHolder = servletHolderBuilder.build();
				return this;
			}

        }

        public Server build() {
            Server server = new Server(port);
            for (WebAppContext context : contexts) {
                server.addHandler(context);
            }
            
            if(shouldStart){
                start(server);
            }
            
            return server;
        }

        private void start(Server server) {
            try {
                server.start();
            } catch (Exception e) {
                throw new RuntimeException("Server failed to start:", e);
            }
        }

        public ServerBuilder started() {
            this.shouldStart = true;
            return this;
        }

		public ServerBuilder with(WebAppContextBuilder servlet) {
			// TODO should probably do this at build time
			return with(servlet.build());
		}

		public static ServletHolderBuilder servlet(Servlet servlet) {
			return new ServletHolderBuilder().withServlet(servlet);
		}
		
        public static class ServletHolderBuilder {
			private Servlet servlet;
			private Map<String, String> initParams = new HashMap<String, String>();

			public ServletHolderBuilder withServlet(Servlet servlet) {
				this.servlet = servlet;
				return this;
			}

			public ServletHolderBuilder withInitParameter(String param, String value) {
				this.initParams.put(param, value);
				return this;
			}
			
			public ServletHolder build(){
				ServletHolder servletHolder = new ServletHolder();
				servletHolder.setServlet(servlet);
				servletHolder.setInitParameters(initParams);
				servletHolder.setInitOrder(1);
				return servletHolder;
			}

	}



    }
