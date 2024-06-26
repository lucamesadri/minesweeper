import java.util.Random;

public class Board {
    private Cell[][] cells;
    private int rows;
    private int cols;
    private int mines;

    public Board(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        cells = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }
        placeMines();
        calculateAdjacentMines();
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!cells[i][j].isMine()) {
                    int count = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (isInBounds(i + x, j + y) && cells[i + x][j + y].isMine()) {
                                count++;
                            }
                        }
                    }
                    cells[i][j].setAdjacentMines(count);
                }
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell getCell(int row, int col) {
        if (isInBounds(row, col)) {
            return cells[row][col];
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
