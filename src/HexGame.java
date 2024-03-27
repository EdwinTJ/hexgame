import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//TODO:
// 1. AMke board start at 1
// 2. Make the board shift to the right
// 3. Make the getNegibesr non-edge cells

public class HexGame {
    private static final int BOARD_SIZE = 11;
    private static final int TOTAL_CELLS = (BOARD_SIZE * BOARD_SIZE) + 1;
    private static final int RIGHT = TOTAL_CELLS;
    private static final int LEFT = TOTAL_CELLS + 1;
    private static final int TOP = TOTAL_CELLS + 2;
    private static final int BOTTOM = TOTAL_CELLS + 3;

    private final UnionFind uf = new UnionFind(TOTAL_CELLS + 4);
    private final CellColor[] board = new CellColor[TOTAL_CELLS];
    private int moveCount = 0;

    public HexGame() {
        for (int i = 0; i < TOTAL_CELLS; i++) {
            board[i] = CellColor.NONE;
        }
    }

    public boolean makeMove(int cell, CellColor color) {
        if (cell < 0 || cell >= TOTAL_CELLS || board[cell] != CellColor.NONE) {
            System.out.println("Invalid move or cell already occupied.");
            return false;
        }

        board[cell] = color;
        System.out.println(color + " moves to cell " + cell + ".");
        moveCount++;

        // Union the cell with its neighbors of the same color
        for (int neighbor : getNeighbors(cell)) {
            if (board[neighbor] == color) {
                uf.union(cell, neighbor);
            }
        }

        // Handle edge and corner cells
        if (color == CellColor.RED) {
            if (cell < BOARD_SIZE) {
                uf.union(cell, TOP);
            }
            if (cell > TOTAL_CELLS - BOARD_SIZE) {
                uf.union(cell, BOTTOM);
            }

        } else if (color == CellColor.BLUE) {
            if (cell % BOARD_SIZE == 0) {
                uf.union(cell, LEFT);
            }
            if ((cell + 1) % BOARD_SIZE == 0) {
                uf.union(cell, RIGHT);
            }
        }

        return checkWin(color);
    }

    public boolean checkWin(CellColor currentPlayer) {
        System.out.println(currentPlayer + " checking win...");

        // Check if left and right are in the same set by union find if they are they win
        System.out.println(uf.toString());
        if (currentPlayer == CellColor.BLUE && uf.find(LEFT) == uf.find(RIGHT)) {
            return true;
        } else if (currentPlayer == CellColor.RED && uf.find(TOP) == uf.find(BOTTOM)) {
            return true;

        }


        return false; // No winner yet
    }

    private int[] getNeighbors(int item) {
        int[] neighbors;
        boolean isLeftEdge = item % BOARD_SIZE == 0;
        boolean isRightEdge = (item + 1) % BOARD_SIZE == 0;
        boolean isTopRow = item >= 1 && item <= BOARD_SIZE;
        boolean isBottomRow = item >= TOTAL_CELLS - BOARD_SIZE + 1 && item <= TOTAL_CELLS;

        if (isTopRow) {
            neighbors = isLeftEdge ? new int[]{item + 1, item + BOARD_SIZE} :
                    isRightEdge ? new int[]{item - 1, item + BOARD_SIZE - 1} :
                            new int[]{item - 1, item + 1, item + BOARD_SIZE - 1, item + BOARD_SIZE, item + BOARD_SIZE + 1};
        } else if (isBottomRow) {
            neighbors = isLeftEdge ? new int[]{item + 1, item - BOARD_SIZE + 1} :
                    isRightEdge ? new int[]{item - 1, item - BOARD_SIZE} :
                            new int[]{item - 1, item + 1, item - BOARD_SIZE, item - BOARD_SIZE + 1, item - BOARD_SIZE + 1};
        } else {
            neighbors = isLeftEdge ? new int[]{item - BOARD_SIZE, item + 1, item + BOARD_SIZE} :
                    isRightEdge ? new int[]{item - BOARD_SIZE, item - 1, item + BOARD_SIZE} :
                            new int[]{item - BOARD_SIZE, item - BOARD_SIZE + 1, item - 1, item + 1, item + BOARD_SIZE - 1, item + BOARD_SIZE};
        }

        // Print the neighbors
        System.out.print("Neighbors of " + item + " [");
        for (int neighbor : neighbors) {
            System.out.print(neighbor + ",");
        }
        System.out.println("]");

        return neighbors;
    }

    public void printBoard() {
        System.out.println("Current Board State:");

        int rowLength = 11;
        for (int row = 0; row < rowLength; row++) {
            // Print leading spaces for alignment
            for (int space = 0; space < rowLength - row - 1; space++) {
                System.out.print(" ");
            }

            for (int col = 1; col <= rowLength; col++) {
                int index = row * rowLength + col;
                // Adjust the index to shift the board to the left
                int adjustedIndex = index - row;
                printCell(board[adjustedIndex]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printCell(CellColor color) {
        String colorCode;
        switch (color) {
            case RED:
                colorCode = ANSI_RED;
                break;
            case BLUE:
                colorCode = ANSI_BLUE;
                break;
            default:
                colorCode = ANSI_WHITE; // ANSI_WHITE or another color for empty cells
        }
        System.out.print(colorCode + "● " + ANSI_RESET); // Print colored cell
    }

    public boolean isValidMove(int cell) {
        if (cell < 0 || cell >= TOTAL_CELLS) {
            return false; // Cell is out of board range
        }
        return board[cell] == CellColor.NONE; // Move is valid if the cell is unoccupied
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public enum CellColor {
        NONE, RED, BLUE
    }

    public static void main(String[] args) {
//        String[] files = {"moves.txt", "moves2.txt"};
        String[] files = {"moves.txt"};
        for (String fileName : files) {
            System.out.println("Processing file: " + fileName);
            HexGame game = new HexGame();
            CellColor currentPlayer = CellColor.BLUE;

            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    int cell = Integer.parseInt(scanner.nextLine().trim());
                    if (!game.isValidMove(cell)) {
                        System.out.println("Invalid move at cell " + cell + ", skipping.");
                        continue;
                    }

                    game.makeMove(cell, currentPlayer);
//                    Thread.sleep(500); // Add delay for better visualization
                    game.printBoard();

                    if (game.checkWin(currentPlayer)) {
                        System.out.println(currentPlayer + " wins!");
                        break;
                    }

                    currentPlayer = (currentPlayer == CellColor.BLUE) ? CellColor.RED : CellColor.BLUE;
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + fileName);
            }
//            catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println("Finished processing file: " + fileName);
        }
    }
}