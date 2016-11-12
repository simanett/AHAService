/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Airplane;
import com.aha.businesslogic.model.Booking;
import com.aha.businesslogic.model.Flight;
import com.aha.businesslogic.model.Passenger;
import com.aha.businesslogic.model.Seat;
import com.aha.data.BookingRepository;
import com.aha.data.FlightRepository;
import com.aha.data.PassengerRepository;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
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
    FlightRepository flightRepository = new FlightRepository();

    PassengerRepository passengerRepository = new PassengerRepository();

    @Override
    public void init() throws ServletException {
        //AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
        AHA.connect("jdbc:mysql://localhost:3306/AHA", "AHA", "aha1234");
    }

    private Seat findSeat(Flight flight, int seatId) {
        for (Seat seat : flight.getAirplane().getSeats()) {
            if (seat.getId() == seatId) {
                return seat;
            }
        }
        return null;
    }

    private String generateBookingReference(Flight flight) {
        SecureRandom random = new SecureRandom();

        String randomNumber = new BigInteger(130, random).toString(32);
        String reference = flight.getFlightNumber() + randomNumber;

        return reference.substring(0, 20);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = GsonUtils.getGson();

        int baggage = 0;
        int ticketType = 0;

        switch (action) {
            case "createBooking":
                int flightId = Integer.parseInt(req.getParameter("flight"));
                int seatId = Integer.parseInt(req.getParameter("seat"));

                Passenger passenger = (Passenger) req.getAttribute("passenger");
                if (passenger == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Message message = new Message("No passenger provided");
                    writer.write(gson.toJson(message));
                }

                Flight flight = flightRepository.getFlightById(flightId);
                if (flight == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Message message = new Message("Can't find flight");
                    writer.write(gson.toJson(message));
                }

                Seat seat = findSeat(flight, seatId);
                if (seat == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Message message = new Message("Can't find seat");
                    writer.write(gson.toJson(message));
                }

                Booking booking = new Booking();
                booking.setBookingReference(generateBookingReference(flight));
                booking.setFlight(flight);
                booking.setPassenger(passenger);
                booking.setSeat(seat);
                booking.setBaggage(baggage);
                booking.setTicketType(ticketType);

                boolean result = bookingRepository.addBooking(booking);
                if (result == true) {
                    resp.setStatus(HttpServletResponse.SC_OK);

                    writer.write(gson.toJson(booking));
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    Message message = new Message("Can't create booking");
                    writer.write(gson.toJson(message));
                }

                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = GsonUtils.getGson();

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
                Passenger activePassenger = passengerRepository.getPassengerById(Integer.parseInt(activePassengerId));
                List<Booking> bookingsByActivePassangers = bookingRepository.getActiveBookingsByPassenger(activePassenger);

                writer.write(gson.toJson(bookingsByActivePassangers));

                break;

            case "getApprovedBookingsByPassenger":
                String approvedPassengerId = req.getParameter("activePassengerId");
                Passenger approvedPassenger = passengerRepository.getPassengerById(Integer.parseInt(approvedPassengerId));

                List<Booking> bookingsByApprovedPassenger = bookingRepository.getApprovedBookingsByPassenger(approvedPassenger);
                writer.write(gson.toJson(bookingsByApprovedPassenger));

                break;

            case "getPendingBookingsByPassenger":
                String pendingPassengerId = req.getParameter("pendingPassengerId");
                Passenger pendingPassenger = passengerRepository.getPassengerById(Integer.parseInt(pendingPassengerId));

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
