package org.example;

import princeton.MinPQ;
import princeton.In;
import princeton.StdDraw;
import princeton.StdOut;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet; // optimization
import java.util.Set;

// using an extended Dijkstra's algorithm A* algorithm f(n) = g(n) + h(n)

public class Solver {
    private SearchNode goalNode;
    private final boolean solvable;

    private static int DELAY = 700; // milliseconds between moves

    private static final Color TILE_CORRECT = new Color(60, 187, 255);
    private static final Color TILE_INCORRECT = new Color(255, 57, 103);
    private static final Color TILE_BLANK = new Color(2, 15, 21);
    private static final Color BACKGROUND = new Color(2, 15, 21);
    private static final Color BORDER = new Color(255, 255, 255);

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int manhattan;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        public int priority() {
            return moves + manhattan;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority(), that.priority());
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        if (!isSolvable(initial)) {
            this.solvable = false;
            this.goalNode = null;
            return;
        }

        this.solvable = true;

        MinPQ<SearchNode> pq = new MinPQ<>();
        Set<Board> visited = new HashSet<>();

        pq.insert(new SearchNode(initial, 0, null));

        while (!pq.isEmpty()) {
            SearchNode current = pq.delMin();

            if (current.board.isGoal()) {
                goalNode = current;
                break;
            }

            visited.add(current.board); // important for speed!

            for (Board neighbor : current.board.neighbors()) {
                if ((current.previous == null || !neighbor.equals(current.previous.board))
                        && !visited.contains(neighbor)) {
                    pq.insert(new SearchNode(neighbor, current.moves + 1, current));
                }
            }
        }
    }

    private int countInversions(Board board) {
        int n = board.dimension();
        int[][] blocks = board.getBlocks();

        int[] flatblocks = new int[n * n - 1];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    flatblocks[k++] = blocks[i][j];
                }
            }
        }

        int inversions = 0;
        for (int i = 0; i < flatblocks.length; i++) {
            for (int j = i + 1; j < flatblocks.length; j++) {
                if (flatblocks[i] > flatblocks[j]) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    // API
    private boolean isSolvable(Board board) {
        int n = board.dimension();
        int inversions = countInversions(board);

        if (n % 2 != 0) {
            return inversions % 2 == 0;
        } else {
            int blankRowFromBottom = n - board.blankRow();
            return (inversions + blankRowFromBottom) % 2 == 1;
        }
    }

    public int moves() {
        if (!solvable) return -1;
        return goalNode.moves;
    }

    public Iterable<Board> solution() {
        if (!solvable) return null;

        ArrayList<Board> path = new ArrayList<>();
        SearchNode current = goalNode;

        while (current != null) {
            path.add(current.board);
            current = current.previous;
        }

        Collections.reverse(path);
        return path;
    }

    // graphic interface
    private static void drawBoard(Board board, int stepNumber, int totalSteps) {
        StdDraw.clear(BACKGROUND);

        int n = board.dimension();
        int[][] blocks = board.getBlocks();

        int fontSize = n <= 3 ? 50 : (n == 4 ? 40 : 30);
        StdDraw.setFont(new Font("Roboto", Font.BOLD, fontSize));

        double blocksize = 0.7 / n; // 70 % of the canvas
        double startX = 0.15;
        double startY = 0.2;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double x = startX + j * blocksize + blocksize / 2;
                double y = startY + (n - 1 - i) * blocksize + blocksize / 2;

                if (blocks[i][j] == 0) {
                    StdDraw.setPenColor(TILE_BLANK);
                    StdDraw.filledSquare(x, y, blocksize / 2 - 0.005);
                } else {
                    int correctValue = i * n + j + 1;
                    if (blocks[i][j] == correctValue) {
                        StdDraw.setPenColor(TILE_CORRECT);
                    } else {
                        StdDraw.setPenColor(TILE_INCORRECT);
                    }

                    StdDraw.filledSquare(x, y, blocksize / 2 - 0.005);
                    StdDraw.setPenColor(BORDER);
                    StdDraw.setPenRadius(0.003);
                    StdDraw.square(x, y, blocksize / 2 - 0.005);
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text(x, y, String.valueOf(blocks[i][j]));
                }
            }
        }

        double centerX = startX + (n * blocksize) / 2;
        double centerY = startY + (n * blocksize) / 2;
        StdDraw.setPenColor(BORDER);
        StdDraw.setPenRadius(0.006);
        StdDraw.square(centerX, centerY, (n * blocksize) / 2);

        StdDraw.setFont(new Font("Roboto", Font.PLAIN, 18));
        if (stepNumber >= 0) {
            StdDraw.text(0.2, 0.17, String.format("Step %d", stepNumber));
        }

        StdDraw.setFont(new Font("Roboto", Font.PLAIN, 16));
        StdDraw.text(0.25, 0.92, String.format("Hamming: %d", board.hamming()));
        StdDraw.text(0.75, 0.92, String.format("Manhattan: %d", board.manhattan()));

        StdDraw.show();
    }

    private static void visualize(Solver solver) {
        StdDraw.setCanvasSize(800, 830);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();

        if (!solver.isSolvable()) {
            StdDraw.clear(BACKGROUND);
            StdDraw.setPenColor(Color.RED);
            StdDraw.setFont(new Font("Roboto", Font.BOLD, 30));
            StdDraw.text(0.5, 0.5, "OMG! WE HAVE NO SOLUTION :(");
            StdDraw.show();
            return;
        }

        int moves = solver.moves();

        // animation
        int step = 0;
        for (Board board : solver.solution()) {
            drawBoard(board, step, moves);
            StdDraw.pause(DELAY);
            step++;
        }

        StdDraw.setFont(new Font("Roboto", Font.BOLD, 20));
        if (moves > 1) {
            StdDraw.text(0.5, 0.1, String.format("Completed for %d steps :)", moves));
        } else {
            StdDraw.text(0.5, 0.1, String.format("SUCH A WONDERFUL GAME! :)", moves));
        }

        StdDraw.show();
    }

    private boolean isSolvable() {
        return solvable;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }

        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);

        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }

        if (solver.moves() > 10 && solver.moves() < 50) {
            DELAY = 150;
        } else if (solver.moves() >= 50){
            DELAY = 50;
        }

        visualize(solver);
    }
}