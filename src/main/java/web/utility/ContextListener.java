package web.utility;
import roupla.utility.*;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

@WebListener
public class ContextListener extends HttpServlet implements ServletContextListener{
    private ServletContext context = null;
    public void init(ServletConfig config) throws javax.servlet.ServletException{
        //initializing
        super.init(config);
        context = getServletContext();
    }
    //ServletContextListener methods
    @Override
    public void contextDestroyed(ServletContextEvent arg0){

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0){
        //context = getServletContext();
        // String path = context.getRealPath("mapdata/bw.fmi");
        Hub hub = new Hub("bw.fmi");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){

    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
