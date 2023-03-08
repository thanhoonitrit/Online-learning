/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.lecture;

import dao.LectureDAO;
import dao.LessonDAO;
import dao.QuestionDAO;
import dao.QuizDAO;
import entity.Account;
import entity.Lecture;
import entity.Lesson;
import entity.Question;
import entity.Quiz;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Duc Anh
 */
public class LectureQuizAddController extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LectureQuizAddController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LectureQuizAddController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        Account account = new Account();
        account = (Account) request.getSession().getAttribute("accountAdmin");

        Lecture lecture = new Lecture();
        lecture = new LectureDAO().getLectureByAccount(account);
        
        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
        Lesson lesson = new Lesson();
        lesson.setLessonID(lessonID);
        lesson = new LessonDAO().getOne(lesson);
        
        request.setAttribute("lecture", lecture);
        request.setAttribute("lessonID", lessonID);
        request.setAttribute("lesson", lesson);
        request.getRequestDispatcher("../view/lecture/quiz_add.jsp").forward(request, response);
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
         request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        ArrayList<Question> list_question = new ArrayList<>();
        
        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
        String [] index = request.getParameterValues("index");
        Lesson lesson = new Lesson();
        lesson.setLessonID(lessonID);
        lesson = new LessonDAO().getOne(lesson);
        
        Quiz quiz = new Quiz(lesson, true);       
        int quizId = new QuizDAO().insert(quiz); 
        quiz.setQuizID(quizId);
        
        for (String in : index) {
            String question_index = request.getParameter("question"+Integer.parseInt(in));
            Question question = new Question(question_index, 0, quiz, true);
            list_question.add(question);
        }
        
        new QuestionDAO().insert(list_question);
        response.sendRedirect("../lecture/quiz_list?lessonID="+lessonID);
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
