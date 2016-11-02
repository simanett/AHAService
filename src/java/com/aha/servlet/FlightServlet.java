/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Flight;
import com.aha.data.FlightRepository;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author simonicsanett
 */
@WebServlet(name = "FlightServlet", urlPatterns = {"/FlightServlet"})
public class FlightServlet extends HttpServlet {

    FlightRepository flightRepository = new FlightRepository();

    @Override
    public void init() throws ServletException {
        //AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
        AHA.connect("jdbc:mysql://localhost:3306/AHA", "AHA", "aha1234");
    }

    private Date getDate(String dateString) throws ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        return df1.parse(dateString);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = GsonUtils.getGson();

        switch (action) {
            case "getFlights":
                List<Flight> flights = flightRepository.getFlights();
                writer.write(gson.toJson(flights));

                break;

            case "getFilteredFlights":
                try {
                    String fromCity = req.getParameter("from");
                    String toCity = req.getParameter("to");

                    Date fromDate = getDate(req.getParameter("start"));
                    Date toDate = getDate(req.getParameter("end"));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(toDate);
                    cal.add(Calendar.DATE, 1);  // number of days to add
                    
                    List<Flight> filteredFlights = flightRepository.getFilteredFlights(fromCity, toCity, fromDate, cal.getTime());
                    writer.write(gson.toJson(filteredFlights));
                } catch (ParseException ex) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writer.write(gson.toJson(ex));
                }
                
                break;

            case "getFlightByFlightNumber":
                String number = req.getParameter("number");
                Flight flight = flightRepository.getFlightByFlightNumber(Integer.parseInt(number));

                if (flight != null) {
                    writer.write(gson.toJson(flight));
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writer.write("{}");
                }
                break;

            case "getFlightById":
                String id = req.getParameter("id");
                Flight flightById = flightRepository.getFlightById(Integer.parseInt(id));

                if (id != null) {
                    writer.write(gson.toJson(flightById));
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writer.write("{}");
                }
                break;
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
