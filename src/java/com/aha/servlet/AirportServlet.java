/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Airplane;
import com.aha.businesslogic.model.Airport;
import com.aha.data.AirportRepository;
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
@WebServlet(name = "AirportServlet", urlPatterns = {"/AirportServlet"})
public class AirportServlet extends HttpServlet {

    AirportRepository airportRepository = new AirportRepository();
    
    @Override
    public void init() throws ServletException {
        //AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");}
        AHA.connect("jdbc:mysql://localhost:3306/AHA", "AHA", "aha1234");
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
            case "getAirports":
                List<Airport> airports = airportRepository.getAirports();
                writer.write(gson.toJson(airports));
                

                break;

            case "getAirportByCode":
                String code = req.getParameter("code");
                Airport airport = airportRepository.getAirportByCode(code);

                if (airport != null) {
                    writer.write(gson.toJson(airport));
                } else {
                    System.out.println("Invalid airport code");
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
    


