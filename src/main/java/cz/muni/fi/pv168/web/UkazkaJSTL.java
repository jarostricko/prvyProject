package cz.muni.fi.pv168.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@WebServlet(urlPatterns = {"/UkazkaJSTL"})
public class UkazkaJSTL extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Map<String, Object>> m = new TreeMap<>();

        for (Locale l : Locale.getAvailableLocales()) {
            Map<String, Object> v = new HashMap<>();
            m.put(l.toString(), v);

            v.put("name", l.getDisplayName());
            v.put("origname", l.getDisplayName(l));
            v.put("loc", l);
        }
        request.setAttribute("jazyky", m);

        request.getRequestDispatcher("/stranka.jsp").forward(request, response);
    }

}
