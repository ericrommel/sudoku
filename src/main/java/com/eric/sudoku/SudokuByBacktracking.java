package com.eric.sudoku;

import java.util.stream.IntStream;

/**
 * The SudokuByBacktracking Puzzle
 *
 * SudokuByBacktracking is a logic-based, combinatorial number-placement puzzle. The objective is to fill a 9×9 grid with digits so
 * that each column, each row, and each of the nine 3×3 subgrids that compose the grid (also called "boxes", "blocks",
 * or "regions") contain all of the digits from 1 to 9. The puzzle setter provides a partially completed grid, which for
 * a well-posed puzzle has a single solution.
 * Completed games are always a type of Latin square with an additional constraint on the contents of individual
 * regions. For example, the same single integer may not appear twice in the same row, column, or any of the nine 3×3
 * subregions of the 9x9 playing board.
 * French newspapers featured variations of the puzzles in the 19th century, and the puzzle has appeared since 1979 in
 * puzzle books under the name Number Place. However, the modern SudokuByBacktracking only started to become mainstream in 1986 by the
 * Japanese puzzle company Nikoli, under the name SudokuByBacktracking, meaning "single number". It first appeared in a US newspaper
 * and then The Times (London) in 2004, from the efforts of Wayne Gould, who devised a computer program to rapidly
 * produce distinct puzzles.
 *
 * Soure: https://en.wikipedia.org/wiki/Sudoku
 */
public class SudokuByBacktracking {
    /*
     * Also known like Brutal Force solution, Backtracking algorithm tries to solve the puzzle by testing each cell for
     * a valid solution. If there's no violation of constraints, the algorithm moves to the next cell, fills in all
     * potential solutions and repeats all checks. If there's a violation, then it increments the cell value. Once, the
     * value of the cell reaches 9, and there is still violation then the algorithm moves back to the previous cell and
     * increases the value of that cell. It tries all possible solutions, that's why Brutal Force.
     */

    // Define the board as a two-dimensional array of integers. Use 0 as an empty cell.
    // http://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_SIZE = 9;
    private static final int SUBSECTION_SIZE = 3;
    private static final int NO_VALUE = 0;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    private static int[][] board = {
            { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
            { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
            { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
            { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
            { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
            { 0, 9, 0, 0, 0, 0, 4, 0, 0 }
    };

    int[][] solution = {
            {8, 1, 2, 7, 5, 3, 6, 4, 9 },
            {6, 7, 5, 4, 9, 1, 2, 8, 3 },
            {1, 5, 4, 2, 3, 7, 8, 9, 6 },
            {3, 6, 9, 8, 4, 5, 7, 2, 1 },
            {2, 8, 7, 1, 6, 9, 5, 3, 4 },
            {5, 2, 1, 9, 7, 4, 3, 6, 8 },
            {4, 3, 8, 5, 2, 6, 9, 1, 7 },
            {7, 9, 6, 3, 1, 8, 4, 5, 2 }
    };

    public static void main(String[] args) {
        SudokuByBacktracking sb = new SudokuByBacktracking();
        sb.solve(board);
        sb.printBoard();
    }

    /* Print out the result */
    private void printBoard() {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }

    /* It takes the board as the input parameter and iterates through rows, columns and values testing each
     * cell for a valid solution:
     */
    private boolean solve(int[][] board) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == NO_VALUE) {
                    for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
                        board[row][column] = i;
                        if (isValid(board, row, column) && solve(board)) {
                            return true;
                        }
                        board[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /* It is going to check Sudoku constraints, i.e., check if the row, column, and 3 x 3 grid are valid: */
    private boolean isValid(int[][] board, int row, int column) {
        return rowConstraint(board, row) &&
                columnConstraint(board, column) &&
                subsectionConstraint(board, row, column);
    }

    /* Row checks */
    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    /* Column checks */
    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    /* Subsection checks (3x3) */
    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    /* Constraints check */
    private boolean checkConstraint(int[][] board, int row, boolean[] constraint, int column) {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }
}
