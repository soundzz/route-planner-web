package web.utility;

import com.google.gson.Gson;
import roupla.utility.Graph;
import roupla.utility.Hub;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class ContextListener extends HttpServlet implements ServletContextListener {
    private ServletContext context = null;
    private Hub hub = new Hub("MV.fmi");
    private Graph graph = new Graph("MV.fmi");
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

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String input = request.getReader().readLine();
            System.out.println(input);
            String[] coordinates = input.split("[(, )]");
            System.out.println(coordinates[1] + " " + coordinates[3] + " " + coordinates[5] + " " + coordinates[7]);
            List<Integer> path = hub.userQuery(
                    Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[3]), Double.parseDouble(coordinates[5]), Double.parseDouble(coordinates[7]));
            System.out.println("done" + path.toString() + " done");
            //Knotenindizes -> koordinatenpaare (lat/long) in string
            List<Double> LatLong = new ArrayList<Double>();
            for (int i =0; i <= LatLong.size(); i +=2){
                LatLong.set(i, graph.getNodes()[0][path.get(i)]);  // lat is 0
                LatLong.set(i + 1, graph.getNodes()[1][path.get(i + 1)]); // long is 1
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
}
