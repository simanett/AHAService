/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Airplane;
import com.aha.businesslogic.model.Booking;
import com.aha.businesslogic.model.Passenger;
import com.aha.data.BookingRepository;
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
@WebServlet(name = "BookingServlet", urlPatterns = {"/BookingServlet"})
public class BookingServlet extends HttpServlet {

    BookingRepository bookingRepository = new BookingRepository();
    PassengerRepository passengerRepository = new PassengerRepository();

    @Override
    public void init() throws ServletException {
        //AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
        AHA.connect("jdbc:mysql://localhost:3306/AHA", "root", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        switch (action) {
            case "getBookings":
                List<Booking> bookings = bookingRepository.getBookings();
                writer.write(gson.toJson(bookings));

                break;

            case "getBookingByBookingReference":
                String reference = req.getParameter("reference");
                Booking booking = bookingRepository.getBookingByBookingReference(reference);

                if (booking != null) {
                    writer.write(gson.toJson(booking));
                } else {
                    System.out.println("Invalid booking reference");
                }
                break;

            case "getActiveBookingsByPassenger":
                String activePassengerId = req.getParameter("activePassengerId");
                Passenger activePassenger = passengerRepository.getPassengerById(Integer.getInteger(activePassengerId));
                List<Booking> bookingsByActivePassangers = bookingRepository.getActiveBookingsByPassenger(activePassenger);

                writer.write(gson.toJson(bookingsByActivePassangers));

                break;

            case "getApprovedBookingsByPassenger":
                String approvedPassengerId = req.getParameter("activePassengerId");
                Passenger approvedPassenger = passengerRepository.getPassengerById(Integer.getInteger(approvedPassengerId));

                List<Booking> bookingsByApprovedPassenger = bookingRepository.getApprovedBookingsByPassenger(approvedPassenger);
                writer.write(gson.toJson(bookingsByApprovedPassenger));

                break;

            case "getPendingBookingsByPassenger":
                String pendingPassengerId = req.getParameter("pendingPassengerId");
                Passenger pendingPassenger = passengerRepository.getPassengerById(Integer.getInteger(pendingPassengerId));

                List<Booking> bookingsByPendingPassenger = bookingRepository.getApprovedBookingsByPassenger(pendingPassenger);
                writer.write(gson.toJson(bookingsByPendingPassenger));

                break;

        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
