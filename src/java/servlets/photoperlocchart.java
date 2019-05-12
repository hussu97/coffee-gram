/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Beans.Singleton;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author hp
 */
public class photoperlocchart extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                CachedRowSet crs=RowSetProvider.newFactory().createCachedRowSet();
                System.out.println(Singleton.getInstance().getDB());
                crs.setUrl(Singleton.getInstance().getDB());
                crs.setUsername(Singleton.getInstance().getUser());
                crs.setPassword(Singleton.getInstance().getPassword());
                crs.setCommand("select count(*),locationname from photos,locations where photos.LOCATIONID=locations.LOCATIONID group by locationname");
                crs.execute();
                StringBuilder r = new StringBuilder("[ [\"Location\", \"Posts\"],");
                while(crs.next()){
                    r.append("[\""+crs.getString("locationname")+"\","+crs.getDouble(1)+"]");
                    if(!crs.isLast()){
                        r.append(",");
                    }
                }
                r.append("]");
                System.out.println(r.toString());
                crs.close();
                out.print(r.toString());
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
