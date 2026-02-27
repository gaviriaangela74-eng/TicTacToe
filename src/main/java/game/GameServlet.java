package game;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import game.GameBean;
import game.GameBean.GamePlayer;

public class GameServlet extends HttpServlet {

    /**
     * Procesa las peticiones para ambos métodos HTTP (GET y POST).
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtenemos el objeto del juego de la sesión
        GameBean game = (GameBean) request.getSession(true).getAttribute("gameBean");
        
        // Leemos la jugada del usuario
        int line = Integer.parseInt(request.getParameter("Line"));
        int col = Integer.parseInt(request.getParameter("Col"));
        
        // El usuario juega su turno
        game.playPlayerTurn(line, col);
        
        GamePlayer winner = game.getWinner();
        
        // Lógica para el turno del ordenador si no hay ganador aún
        if (winner == GamePlayer.NOBODY) {
            if (game.hasEmptyCell()) {
                game.playComputerTurn();
                // Verificamos si el ordenador ganó después de su turno
                switch (game.getWinner()) {
                    case COMPUTER:
                        request.setAttribute("winner", "The computer");
                        break;
                    case USER:
                        request.setAttribute("winner", "You");
                        break;
                    default:
                        break;
                }
            }
        } else {
            // Si ya hay un ganador tras el turno del usuario
            if (winner == GamePlayer.COMPUTER) {
                request.setAttribute("winner", "The computer");
            } else if (winner == GamePlayer.USER) {
                request.setAttribute("winner", "You");
            }
        }

        // Comprobamos si es un empate (nadie gana y no hay más celdas)
        if (game.getWinner() == GamePlayer.NOBODY && !game.hasEmptyCell()) {
            request.setAttribute("winner", "Nobody");
        }

        // Enviamos de vuelta al tablero de juego para mostrar el estado actual
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    /**
     * Maneja el método HTTP GET llamando a processRequest.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja el método HTTP POST llamando a processRequest.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Game Servlet for TicTacToe";
    }
}