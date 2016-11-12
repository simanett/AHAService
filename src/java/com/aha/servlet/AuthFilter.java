package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Passenger;
import com.aha.businesslogic.model.User;
import com.aha.data.PassengerRepository;
import com.aha.data.UserRepository;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author simonicsanett
 */
@WebFilter(filterName = "AuthFilter",
        urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private PassengerRepository passengerRepository = new PassengerRepository();

    public void init(FilterConfig config)
            throws ServletException {
        AHA.connect("jdbc:mysql://localhost:3306/AHA", "AHA", "aha1234");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws java.io.IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String jwt = httpRequest.getHeader("x-auth");

        if (jwt == null || jwt.length() == 0) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Token ellenorzese Google altal
        URL googleURL = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + jwt);
        URLConnection googleConnection = googleURL.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(googleConnection.getInputStream()));

        String inputLine;
        String googleResponse = "";

        while ((inputLine = reader.readLine()) != null) {
            googleResponse += inputLine;
        }
        reader.close();

        // Token JSON konvertalasa TokenInfo osztalyba
        TokenInfo tokenInfo = new Gson().fromJson(googleResponse, TokenInfo.class);
        if (tokenInfo.error_description != null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Passenger passenger = passengerRepository.getPassengerByEmail(tokenInfo.email);
        if (passenger == null) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("passenger", passenger);

        // A lanc tovabbi elemeinek meghivasa
        chain.doFilter(request, response);
    }

    public void destroy() {
        /* Called before the Filter instance is removed 
      from service by the web container*/
    }
}
