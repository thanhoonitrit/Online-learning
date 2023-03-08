/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.uploadfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import dao.DBContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author NHATNAM-PC
 */
@WebServlet(name = "UploadFile", urlPatterns = {"/admin/upload-file"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class UploadFile extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  
  public UploadFile() {
    super();
  }

  // This Method takes in All the information
  // required and is used to store in the
  // MySql Database.
  public int uploadFile(int adminID, InputStream file) {
    System.out.println("da chay");
    String sql
            = "UPDATE [dbo].[Admin]\n"
            + "   SET [image] = ?\n"
            + " WHERE [adminID] = ?";
    int row = 0;
    
    Connection connection = new DBContext().getConnection();
    
    PreparedStatement ps;
    try {
      ps = connection.prepareStatement(sql);
      
      ps.setInt(1, adminID);
      
      if (file != null) {

        // Fetches the input stream
        // of the upload file for
        // the blob column
        ps.setBlob(2, file);
      }

      // Sends the statement to
      // the database server
      row = ps.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    
    return row;
  }

  // As Submit button is hit from
  // the Web Page, request is made
  // to this Servlet and
  // doPost method is invoked.
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
//    String action = request.getParameter("action");
//    if (action.equals("link")) {
//      request.getRequestDispatcher("../view/admin/addfile.jsp")
//              .forward(request, response);
//    }

    // Getting the parametes from web page
    int adminID = Integer.parseInt(request.getParameter("adminID"));

    // Input stream of the upload file
    InputStream inputStream = null;
    
    String message = null;

    // Obtains the upload file
    // part in this multipart request
    Part filePart = request.getPart("photo");
    
    if (filePart != null) {

      // Prints out some information
      // for debugging
      System.out.println(filePart.getName());
      System.out.println(filePart.getSize());
      System.out.println(filePart.getContentType());

      // Obtains input stream of the upload file
      inputStream = filePart.getInputStream();
    }

    // Sends the statement to the
    // database server
    int row = uploadFile(adminID,
            inputStream);
    if (row > 0) {
      message = "File uploaded and saved into database";
    }
    System.out.println(message);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    super.doGet(request, response); //To change body of generated methods, choose Tools | Templates.
    String action = request.getParameter("action");
    if (action.equals("link")) {
//      response.sendRedirect("../view/admin/addfile.jsp");
      request.getRequestDispatcher("../view/admin/addfile.jsp")
              .forward(request, response);
    }
  }
  
}
