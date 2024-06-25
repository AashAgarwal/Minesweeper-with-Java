package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static char[][] board() {
        char[][] board = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = '.';
            }
        }
        return board;
    }

    public static char[][] mineGenerator(int remainingNumOfMines, int initialMines, char[][] board) {
        Random rand = new Random();
        for (int i = 0; i < remainingNumOfMines; i++) {
            board[rand.nextInt(9)][rand.nextInt(9)] = 'X';
        }
        // Checks if the correct number of mines are generated
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 'X') {
                    count++;
                }
            }
        }
        if (count == initialMines) {
            return board;
        } else {
            mineGenerator(initialMines - count, initialMines, board);
        }
        return board;
    }

    public static char[][] setHints(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 'X') {
                    int mineCounter = countMinesAround(board, i, j);
                    if (mineCounter > 0) {
                        board[i][j] = (char) (mineCounter + '0');
                    }
                }
            }
        }
        return board;
    }

    public static int countMinesAround(char[][] board, int row, int col) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(8, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(8, col + 1); j++) {
                if (board[i][j] == 'X') {
                    count++;
                }
            }
        }
        return count;
    }

    public static char[][] maskedBoard(char[][] board) {
        char[][] maskedBoard = new char[9][9];
        for (int i = 0; i < maskedBoard.length; i++) {
            for (int j = 0; j < maskedBoard.length; j++) {
                if (board[i][j] == 'X') {
                    maskedBoard[i][j] = '.';
                } else {
                    maskedBoard[i][j] = board[i][j];
                }
            }
        }
        return maskedBoard;
    }

    public static void updateMaskedBoard(char[][] board, char[][] maskedBoard, int x, int y, Scanner scanner) {
        if (maskedBoard[x][y] == '.') {
            maskedBoard[x][y] = '*';
            if (maskedBoard[x][y] == '*' && board[x][y] == 'X') {
                board[x][y] = '*';
            }
        } else if (Character.isDigit(maskedBoard[x][y])) {
            System.out.println("There is a number here! ");
            y = scanner.nextInt() - 1;
            x = scanner.nextInt() - 1;
            updateMaskedBoard(board, maskedBoard, x, y, scanner);
        } else if (maskedBoard[x][y] == '*') {
            maskedBoard[x][y] = '.';
        }
    }

    public static void printBoard(char[][] board) {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < 9; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static void main(String[] args) {
        // Main method
        System.out.println("How many mines do you want on the field?");
        Scanner scanner = new Scanner(System.in);
        int numberOfMines = scanner.nextInt();
        char[][] mineBoard = mineGenerator(numberOfMines, numberOfMines, board());
        char[][] hintBoard = setHints(mineBoard);
        char[][] playersBoard = maskedBoard(hintBoard);
        while (!Arrays.deepEquals(hintBoard, playersBoard)) {
            printBoard(playersBoard);
            System.out.println("Set/delete mine marks (x and y coordinates): ");
            int yCoordinates = scanner.nextInt() - 1;
            int xCoordinates = scanner.nextInt() - 1;
            updateMaskedBoard(hintBoard, playersBoard, xCoordinates, yCoordinates, scanner);
        }
        printBoard(playersBoard);
        System.out.println("Congratulations! You found all the mines!");
    }
}
