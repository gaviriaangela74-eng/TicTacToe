package game;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EntryServlet extends HttpServlet {

    /**
     * Procesa la solicitud inicial del formulario.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtenemos o creamos la sesión
        HttpSession session = request.getSession(true);
        
        // 2. Intentamos recuperar el juego de la sesión
        GameBean game = (GameBean) session.getAttribute("gameBean");
        
        // 3. ¡IMPORTANTE! Si el juego no existe en la sesión, debemos crearlo
        if (game == null) {
            game = new GameBean();
            session.setAttribute("gameBean", game);
        }
        
        // 4. Leemos quién empieza (basado en el botón que presionó el usuario)
        String user = request.getParameter("User");
        boolean userFirst = (user != null);
        
        // 5. Configuramos e iniciamos el tablero
        game.setStartByUser(userFirst);
        game.startGame();
        
        // 6. Enviamos al usuario a la página del tablero
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    /**
     * Maneja el método HTTP GET.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja el método HTTP POST.
     * Esto evita el error "Método HTTP POST no soportado".
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Maneja el inicio del juego de Tres en Raya.";
    }
}