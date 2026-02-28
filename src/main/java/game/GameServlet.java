package game;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import game.GameBean;
import game.GameBean.GamePlayer;

public class GameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        GameBean game = (GameBean) request.getSession(true).getAttribute("gameBean");

        int line = Integer.parseInt(request.getParameter("Line"));
        int col = Integer.parseInt(request.getParameter("Col"));

        game.playPlayerTurn(line, col);

        GamePlayer winner = game.getWinner();

        if (winner == GamePlayer.NOBODY) {
            if (game.hasEmptyCell()) {

                game.playComputerTurn();

                switch (game.getWinner()) {
                    case COMPUTER:
                        request.setAttribute("winner", "¡El computador ganó!");
                        break;
                    case USER:
                        request.setAttribute("winner", "¡Tú ganaste!");
                        break;
                    default:
                        break;
                }
            }
        } else {

            if (winner == GamePlayer.COMPUTER) {
                request.setAttribute("winner", "¡El computador ganó!");
            } else if (winner == GamePlayer.USER) {
                request.setAttribute("winner", "¡Tú ganaste!");
            }
        }

        if (game.getWinner() == GamePlayer.NOBODY && !game.hasEmptyCell()) {
            request.setAttribute("winner", "¡Empate! Nadie ganó.");
        }

        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}