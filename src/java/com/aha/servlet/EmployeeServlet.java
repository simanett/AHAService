/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.aha.AHA;
import com.aha.businesslogic.model.Employee;
import com.aha.data.EmployeeRepository;
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
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {

    EmployeeRepository employeeRepository = new EmployeeRepository();

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

            case "getEmployeeById":
                String id = req.getParameter("id");
                Employee employee = employeeRepository.getEmployeeById(Integer.getInteger(id));

                if (employee != null) {
                    writer.write(gson.toJson(employee));
                } else {
                    System.out.println("Invalid employee id");
                }
                break;

            case "getEmployeeByName":
                String name = req.getParameter("name");
                Employee employeeName = employeeRepository.getEmployeeByName(name);

                if (employeeName != null) {
                    writer.write(gson.toJson(employeeName));
                } else {
                    System.out.println("Invalid employee name");
                }
                break;

            case "getEmployees":
                List<Employee> employees = employeeRepository.getEmployees();
                 writer.write(gson.toJson(employees));

                break;
        }
    }
     @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
