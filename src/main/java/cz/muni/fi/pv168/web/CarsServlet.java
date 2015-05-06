package cz.muni.fi.pv168.web;

import ch.qos.logback.classic.Logger;
import cz.muni.fi.pv168.Car;
import cz.muni.fi.pv168.CarManager;
import cz.muni.fi.pv168.DatabaseException;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Servlet for managing cars.
 */
@WebServlet(CarsServlet.URL_MAPPING + "/*")
public class CarsServlet extends HttpServlet {

    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/cars";

    private final static ch.qos.logback.classic.Logger log = (Logger) LoggerFactory.getLogger(CarsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showCarsList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //aby fungovala čestina z formuláře
        request.setCharacterEncoding("utf-8");
        //akce podle přípony v URL
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                //načtení POST parametrů z formuláře
                String licencePlate = request.getParameter("licencePlate");
                String model = request.getParameter("model");
                BigDecimal rentalPayment = new BigDecimal(request.getParameter("rentalPayment"));
                boolean status = true; //request.getParameter("status")

                //kontrola vyplnění hodnot
                if (licencePlate == null || licencePlate.length() == 0 || model == null || model.length() == 0 ||
                        rentalPayment == null) {
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty !");
                    showCarsList(request, response);
                    return;
                }
                //zpracování dat - vytvoření záznamu v databázi
                try {
                    Car car = new Car(licencePlate, model, rentalPayment, status);
                    getCarManager().createCar(car);
                    log.debug("created {}", car);
                    //redirect-after-POST je ochrana před vícenásobným odesláním formuláře
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                } catch (DatabaseException e) {
                    log.error("Cannot add car", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long id = Long.valueOf(request.getParameter("id"));
                    getCarManager().deleteCar(id);
                    log.debug("deleted car {}", id);
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                } catch (DatabaseException e) {
                    log.error("Cannot delete car", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets CarManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return BookManager instance
     */
    private CarManager getCarManager() {
        return (CarManager) getServletContext().getAttribute("carManager");
    }

    /**
     * Stores the list of cars to request attribute "cars" and forwards to the JSP to display it.
     */
    private void showCarsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("cars", getCarManager().getAllCars());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (DatabaseException e) {
            log.error("Cannot show cars", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
