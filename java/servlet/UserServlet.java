/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import beans.User;
import beans.UserFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Herlangga
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

    @EJB
    UserFacade uF;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            List listuser = uF.findAll();

            request.setAttribute("listuser", listuser);
            String action = request.getParameter("action");
            String page = "listuser.jsp";
            //Barang barang = brg.find(Integer.parseInt(request.getParameter("cari")));
            if ("userlogin".equals(action)) {
                //User user = uF.find(Integer.parseInt(request.getParameter("iduser")));
                User user = uF.find(request.getParameter("iduser"));

                if (user != null) {
                    if (request.getParameter("password").equals(user.getPassword())) {
                        if ("Admin".equals(user.getRole())) {
                            page = "adminpage.jsp";
                            response.sendRedirect(page);
                        } else {
                            page = "userpage.jsp";
                            response.sendRedirect(page);
                        }
                    } else {
                        out.println("password salah");
                    }

                } else {
                    out.println("field masih kosong");
                }
            } else if ("insertuser".equals(action)) {
                
                int tamp = listuser.size();
                String newid = null;
                User user = (User) listuser.get(tamp - 1);
                String idUserSebelumnya = user.getIdUser().substring(2);
                String an = "0" + (Integer.parseInt(idUserSebelumnya) + 1);
                newid = "US" + an;



                String userName = request.getParameter("name");
                String gender = request.getParameter("gender");
                String date = request.getParameter("dateofbirth");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                String city = request.getParameter("city");

                User userIn = new User();

                userIn.setIdUser(newid);
                userIn.setName(userName);
                userIn.setGender(gender);
                userIn.setDateOfBirth(date);
                userIn.setPassword(password);
                userIn.setRole(role);
                userIn.setCity(city);
                
                uF.create(userIn);
                
                page = "UserServlet";
                response.sendRedirect(page);

            } else if ("deleteuser".equals(action)) {
                
                String idUser = request.getParameter("iduser");
                User user = uF.find(idUser);
                uF.remove(user);
                page = "UserServlet";
                response.sendRedirect(page);
                
            } else if ("edituser".equals(action)) {
                
                String idUser = request.getParameter("iduser");
                User user = uF.find(idUser);
                page = "edituser.jsp";
                request.setAttribute("datauser", user);
                request.getRequestDispatcher(page).forward(request, response);
                
            } else if ("updateuser".equals(action)) {
                String idUser = request.getParameter("iduser");
                String userName = request.getParameter("name");
                String gender = request.getParameter("gender");
                String date = request.getParameter("dateofbirth");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                String city = request.getParameter("city");
                
                
                User user = new User();
                user.setIdUser(idUser);
                user.setName(userName);
                user.setGender(gender);
                user.setDateOfBirth(date);
                user.setPassword(password);
                user.setRole(role);
                user.setCity(city);
                //uF.edit(user);
                page = "UserServlet";
                response.sendRedirect(page);
            }else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
