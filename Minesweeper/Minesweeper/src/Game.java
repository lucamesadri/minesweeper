import java.util.Scanner;

public class Game {
    private Board board;
    private boolean gameOver;
    private boolean win;
    private int rows;
    private int cols;
    private int mines;

    public static final String RESET = "\033[0m";
    public static final String GREEN = "\033[0;32m";
    public static final String BLUE = "\033[0;34m";
    public static final String RED = "\033[0;31m";
    public static final String YELLOW = "\033[0;33m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";

    public Game(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        board = new Board(rows, cols, mines);
        gameOver = false;
        win = false;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (!gameOver && !win) {
            printBoard();
            System.out.println("Enter your move (e.g., '3 B' to reveal, 'F 2 D' to flag):");
            String input = scanner.nextLine().trim();
            try {
                if (input.toLowerCase().startsWith("f")) {
                    String[] parts = input.split(" ");
                    int row = Integer.parseInt(parts[1]) - 1;
                    int col = parts[2].charAt(0) - 'A';
                    flagCell(row, col);
                } else {
                    String[] parts = input.split(" ");
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = parts[1].charAt(0) - 'A';
                    revealCell(row, col);
                }
                checkWin();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid move.");
            }
        }

        printBoard();
        if (gameOver) {
            System.out.println("Game Over! You hit a mine.");
        } else if (win) {
            System.out.println("Congratulations! You've cleared the board.");
        }
    }

    private void revealCell(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell == null || cell.isRevealed() || cell.isFlagged()) {
            return;
        }
        cell.setRevealed(true);
        if (cell.isMine()) {
            gameOver = true;
            return;
        }
        if (cell.getAdjacentMines() == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    revealCell(row + x, col + y);
                }
            }
        }
    }

    private void flagCell(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell != null && !cell.isRevealed()) {
            cell.setFlagged(!cell.isFlagged());
        }
    }

    private void checkWin() {
        boolean allCellsRevealedOrFlagged = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = board.getCell(i, j);
                if (!cell.isRevealed() && !cell.isMine()) {
                    allCellsRevealedOrFlagged = false;
                    break;
                }
            }
        }
        if (allCellsRevealedOrFlagged) {
            win = true;
        }
    }

    private void printBoard() {
        int maxRowLabelLength = String.valueOf(rows).length();
        System.out.print(" ".repeat(maxRowLabelLength + 1));

        for (int col = 0; col < cols; col++) {
            System.out.print((char) ('A' + col) + " ");
        }
        System.out.println();

        for (int row = 0; row < rows; row++) {
            System.out.printf("%" + maxRowLabelLength + "d ", row + 1);
            for (int col = 0; col < cols; col++) {
                Cell cell = board.getCell(row, col);
                if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print(RED + "* " + RESET);
                    } else {
                        switch (cell.getAdjacentMines()) {
                            case 0:
                                System.out.print("  ");
                                break;
                            case 1:
                                System.out.print(GREEN + "1 " + RESET);
                                break;
                            case 2:
                                System.out.print(BLUE + "2 " + RESET);
                                break;
                            case 3:
                                System.out.print(RED + "3 " + RESET);
                                break;
                            case 4:
                                System.out.print(PURPLE + "4 " + RESET);
                                break;
                            case 5:
                                System.out.print(YELLOW + "5 " + RESET);
                                break;
                            case 6:
                                System.out.print(CYAN + "6 " + RESET);
                                break;
                            case 7:
                                System.out.print(RED + "7 " + RESET);
                                break;
                            case 8:
                                System.out.print(GREEN + "8 " + RESET);
                                break;
                        }
                    }
                } else if (cell.isFlagged()) {
                    System.out.print(PURPLE + "F " + RESET);
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Game game = new Game(10, 10, 10);
        game.play();
    }
}
