package com.epf.rentmanager.ui.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet(name = "VehiculeUpdateServlet", value = "/cars/update")
public class VehiculeUpdateServlet extends HttpServlet {
   
    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("vehicule", vehicleService.findById(Integer.parseInt(req.getParameter("id"))).get());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
                Vehicle vehicle = new Vehicle(Integer.parseInt(req.getParameter("id")),
                                                req.getParameter("manufacturer"),
                                                req.getParameter("modele"),
                                                (byte)Integer.parseInt(req.getParameter("seats")));
                try {
                    vehicleService.update(vehicle);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
        resp.sendRedirect("/rentmanager/cars");  
    }
}
