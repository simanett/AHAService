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
        AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        switch (action) {
            case "getFlights":
                List<Flight> flights = flightRepository.getFlights();
//                for (Flight flight : flights) {
//                    writer.write(flight.toString());
//                    writer.write("\n");
//                }

                writer.write(gson.toJson(flights));

                break;

            case "getFilteredFlights":
                List<Flight> filteredFlights = flightRepository.getFlights();
                for (Flight filteredFlight : filteredFlights) {
                    writer.write(filteredFlight.toString());
                    writer.write("\n");
                }

            case "getFlightByFlightNumber":
                String number = req.getParameter("number");
                Flight flight = flightRepository.getFlightByFlightNumber(Integer.getInteger(number));

                if (flight != null) {
                    writer.write(gson.toJson(flight));
                } else {
                    
                    System.out.println("Invalid flight number");
                }
                break;

            case "getFlightById":
                String id = req.getParameter("id");
                Flight flightById = flightRepository.getFlightById(Integer.getInteger(id));

                if (id != null) {
                    writer.write(gson.toJson(id));
                } else {
                    System.out.println("Invalid flight ID");
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
