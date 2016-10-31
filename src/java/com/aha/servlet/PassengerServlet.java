/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Passenger;
import com.aha.data.PassengerRepository;
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
@WebServlet(name = "PassengerServlet", urlPatterns = {"/PassengerServlet"})
public class PassengerServlet extends HttpServlet {

    PassengerRepository passengerRepository = new PassengerRepository();

    @Override
    public void init() throws ServletException {
        //AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
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
            case "getPassengers":
                List<Passenger> passengers = passengerRepository.getPassengers();
                 writer.write(gson.toJson(passengers));
                break;

            case "getPassengerById":
                String id = req.getParameter("id");
                Passenger passengerId = passengerRepository.getPassengerById(Integer.parseInt(id));

                if (passengerId != null) {
                   writer.write(gson.toJson(passengerId));
                } else {
                    System.out.println("Invalid passenger ID");
                }
                break;

            case "getPassengerByName":
                String name = req.getParameter("name");
                Passenger passengerName = passengerRepository.getPassengerByName(name);

                if (passengerName != null) {
                     writer.write(gson.toJson(passengerName));
                } else {
                    System.out.println("Invalid passenger name");
                }
                break;

            case "getPassengerByEmail":
                String email = req.getParameter("email");
                Passenger passengerEmail = passengerRepository.getPassengerByName(email);

                if (passengerEmail != null) {
                     writer.write(gson.toJson(passengerEmail));
                } else {
                    System.out.println("Invalid passenger email");
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
