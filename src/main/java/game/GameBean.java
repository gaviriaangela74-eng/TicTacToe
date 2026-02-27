package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBean {
    private static final int GRID_SIZE = 3;

    public enum GameState {
        NULL, O, X;
    }

    public enum GamePlayer {
        USER(GameState.X),
        COMPUTER(GameState.O),
        NOBODY(GameState.NULL);
        
        private GameState state;

        private GamePlayer(GameState state) {
            this.state = state;
        }
    }

    private boolean userFirst = true;
    private GameState[][] gameStatus;
    private static final Random rand = new Random();

    public GameBean() {
        this.gameStatus = new GameState[GRID_SIZE][GRID_SIZE];
        this.startGame(); // Inicializa el tablero al crear el objeto
    }

    public void setStartByUser(boolean userFirst) {
        this.userFirst = userFirst;
    }

    public void startGame() {
        for (int line = 0; line < GRID_SIZE; line++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                this.gameStatus[line][col] = GameState.NULL;
            }
        }
        if (!this.userFirst) {
            // Si inicia el computador, juega en el centro por defecto
            this.play(GamePlayer.COMPUTER, 1, 1);
        }
    }

    public void playPlayerTurn(int line, int col) {
        this.play(GamePlayer.USER, line, col);
    }

    public void playComputerTurn() {
        if (!this.hasEmptyCell()) return;
        
        int line = this.getRandomLineIndexWithEmptyCell();
        int col = this.getRandomEmptyCell(line);
        this.play(GamePlayer.COMPUTER, line, col);
    }

    private void play(GamePlayer player, int line, int col) {
        if (line >= 0 && line < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            if (this.gameStatus[line][col] == GameState.NULL) {
                this.gameStatus[line][col] = player.state;
            }
        }
    }

    public GamePlayer getWinner() {
        // Revisar Filas
        for (int i = 0; i < GRID_SIZE; i++) {
            if (gameStatus[i][0] != GameState.NULL && 
                gameStatus[i][0] == gameStatus[i][1] && 
                gameStatus[i][1] == gameStatus[i][2]) {
                return getPlayer(gameStatus[i][0]);
            }
        }
        // Revisar Columnas
        for (int i = 0; i < GRID_SIZE; i++) {
            if (gameStatus[0][i] != GameState.NULL && 
                gameStatus[0][i] == gameStatus[1][i] && 
                gameStatus[1][i] == gameStatus[2][i]) {
                return getPlayer(gameStatus[0][i]);
            }
        }
        // Diagonales
        if (gameStatus[1][1] != GameState.NULL) {
            if (gameStatus[0][0] == gameStatus[1][1] && gameStatus[1][1] == gameStatus[2][2])
                return getPlayer(gameStatus[1][1]);
            if (gameStatus[0][2] == gameStatus[1][1] && gameStatus[1][1] == gameStatus[2][0])
                return getPlayer(gameStatus[1][1]);
        }
        return GamePlayer.NOBODY;
    }

    private GamePlayer getPlayer(GameState state) {
        for (GamePlayer player : GamePlayer.values()) {
            if (player.state == state) return player;
        }
        return GamePlayer.NOBODY;
    }

    public boolean hasEmptyCell() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameStatus[i][j] == GameState.NULL) return true;
            }
        }
        return false;
    }

    private int getRandomLineIndexWithEmptyCell() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameStatus[i][j] == GameState.NULL) {
                    indexes.add(i);
                    break;
                }
            }
        }
        return indexes.get(rand.nextInt(indexes.size()));
    }

    private int getRandomEmptyCell(int line) {
        List<Integer> indexes = new ArrayList<>();
        for (int j = 0; j < GRID_SIZE; j++) {
            if (gameStatus[line][j] == GameState.NULL) {
                indexes.add(j);
            }
        }
        return indexes.get(rand.nextInt(indexes.size()));
    }

    // Métodos para que el JSP pueda leer el estado
    public List<Line> getGridLines() {
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            lines.add(new Line(gameStatus[i], i));
        }
        return lines;
    }

    public List<Cell> getGridStatus(Line line) {
        List<Cell> cells = new ArrayList<>();
        GameState[] datas = line.getDatas();
        for (int j = 0; j < datas.length; j++) {
            cells.add(new Cell(datas[j], line.getIndex(), j));
        }
        return cells;
    }
}