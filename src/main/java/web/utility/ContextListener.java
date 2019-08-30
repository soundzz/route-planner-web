package web.utility;

import roupla.utility.*;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class ContextListener extends HttpServlet implements ServletContextListener {
    private ServletContext context = null;
    private Hub hub = new Hub("bw.fmi");
    public void init(ServletConfig config) throws javax.servlet.ServletException {
        //initializing
        super.init(config);
        context = getServletContext();
    }
    //ServletContextListener methods

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        //context = getServletContext();
        // String path = context.getRealPath("mapdata/bw.fmi");
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //hub = new Hub("bw.fmi");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
              try {
            String input = request.getReader().readLine();
            System.out.println(input);
            String[] coordinates = input.split("[(, )]");
            System.out.println(coordinates[1] + " " + coordinates[3] + " " + coordinates[5] + " " + coordinates[7]);
            List<Integer> path = hub.userQuery(
                    Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[3]), Double.parseDouble(coordinates[5]), Double.parseDouble(coordinates[7]));
            System.out.println("done" + path.toString() + " done");
            //Knotenindizes -> koordinatenpaare (lat/long) in string
            List<Double> LatLong = new List(2* path.length);
            for (int i =0; i <= LatLong.length(); i +=2){
                LatLong[i] = graph.getNodes()[0][path[i]];  // lat is 0
                LatLong[i+1] = graph.getNodes()[1][path[i+1]]; // long is 1
            };
            
            //FALLS LatLong nicht mit Json funktioniert hier der Stringbuilder:
            //object of stringbuilder class
           /* Stringbuilder builder = new Stringbuilder();
            
            for(Double d: str){
                builder.append(d);
                builder.append(" "); // adds space to split it later
            }
            
            String string = builder.toString();
            */
            
            // LatLong muss dann mit string geaendert werden.
            String json = new Gson().toJson(LatLong);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (IOException e) {

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
  
    }
}
