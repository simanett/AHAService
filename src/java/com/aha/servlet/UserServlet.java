/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.User;
import com.aha.data.UserRepository;
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
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

    UserRepository userRepository = new UserRepository();

    @Override
    public void init() throws ServletException {
       // AHA.connect("jdbc:oracle:thin:@192.168.56.101:1521:XE", "AHA", "aha1234");
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
            case "getUsers":
                List<User> users = userRepository.getUsers();
                 writer.write(gson.toJson(users));

                break;

            case "getUserById":
                String id = req.getParameter("id");
                User user = userRepository.getUserById(Integer.parseInt(id));

                if (user != null) {
                    writer.write(gson.toJson(user));
                } else {
                    System.out.println("Invalid user id");
                }
                break;

            case "getUserByName":
                String name = req.getParameter("name");
                User userName = userRepository.getUserByName(name);

                if (userName != null) {
                    writer.write(gson.toJson(userName));
                } else {
                    System.out.println("Invalid user name");
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
