package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] blocks;
    private final int n;
    private int blankRow;
    private int blankCol;

    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * n + j + 1) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = blocks[i][j];
                if (val != 0) {
                    int targetRow = (val - 1) / n;
                    int targetCol = (val - 1) % n;
                    sum += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }
        return sum;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    if (blocks[i][j] != 0) return false;
                } else {
                    if (blocks[i][j] != i * n + j + 1) return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (this.n != that.n) return false;

        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // move blank up
        if (blankRow > 0) {
            neighbors.add(createNeighbor(blankRow - 1, blankCol));
        }

        // down
        if (blankRow < n - 1) {
            neighbors.add(createNeighbor(blankRow + 1, blankCol));
        }

        // left
        if (blankCol > 0) {
            neighbors.add(createNeighbor(blankRow, blankCol - 1));
        }

        // right
        if (blankCol < n - 1) {
            neighbors.add(createNeighbor(blankRow, blankCol + 1));
        }

        return neighbors;
    }

    private Board createNeighbor(int newRow, int newCol) {
        int[][] copy = copyBlocks();
        copy[blankRow][blankCol] = copy[newRow][newCol];
        copy[newRow][newCol] = 0;
        return new Board(copy);
    }

    private int[][] copyBlocks() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        return copy;
    }

    public int[][] getBlocks() {
        return copyBlocks();
    }

    public int blankRow() {
        return blankRow;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}