
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;

public class ServletConnection extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // No content here as it's handled in doPost
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain"); // Change to plain text for easier handling
        try (PrintWriter out = response.getWriter()) {
            String nm = request.getParameter("name");
            String roll = request.getParameter("roll");

            // Validate input
            if (nm == null || nm.isEmpty() || roll == null || roll.isEmpty()) {
                out.println("Name and Roll No cannot be empty");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dharmik", "root", "")) {
                    PreparedStatement ptsmt = con.prepareStatement("INSERT INTO test (name, roll_no) VALUES (?, ?)");
                    ptsmt.setString(1, nm);
                    ptsmt.setString(2, roll);

                    int i = ptsmt.executeUpdate();

                    if (i > 0) {
                        out.println("Successfully inserted");
                    } else {
                        out.println("Something went wrong");
                    }
                }
            } catch (SQLException e) {
                out.println("SQL Error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                out.println("Driver Error: " + e.getMessage());
            }
        }
    }
}
