# 8-Puzzle Solver with A* Algorithm

A Java application that solves the classic sliding puzzle game using the A* search algorithm with Manhattan distance heuristic and provides animated visualization.

## Features

- **A* Algorithm**: Optimal pathfinding using f(n) = g(n) + h(n)
- **Manhattan Heuristic**: Efficient distance estimation
- **Solvability Check**: Detects unsolvable puzzles using inversion counting
- **Animated Visualization**: Watch the solution step-by-step
- **Color-Coded Tiles**: Visual feedback for correct/incorrect positions
- **Performance Metrics**: Displays Hamming and Manhattan distances

## The Puzzle

The 8-puzzle (or N-puzzle) consists of a square grid with numbered tiles and one blank space. The goal is to rearrange tiles into order by sliding them into the blank space.

**Example:**
```
Initial:              Goal:
1  3  0               1  2  3
4  2  5               4  5  6
7  8  6               7  8  0
```

## Algorithm

### A* Search
The solver uses A* with priority function:
```
f(n) = g(n) + h(n)
```
- **g(n)**: Number of moves made so far
- **h(n)**: Manhattan distance (heuristic)
- **f(n)**: Total estimated cost

### Heuristics

**Hamming Distance**: Count of misplaced tiles
```
1  3  0       Goal: 1  2  3
4  2  5             4  5  6
7  8  6             7  8  0
Hamming = 4 (tiles 2, 3, 5, 6 out of place)
```

**Manhattan Distance**: Sum of distances tiles must move
```
Tile 3 at (0,1) should be at (0,2) → distance = 1
Tile 2 at (1,1) should be at (0,1) → distance = 1
Manhattan = sum of all tile distances
```

### Solvability Check

Not all puzzle configurations are solvable. The solver checks:

- **Odd-sized boards** (3×3, 5×5): Solvable if inversions are even
- **Even-sized boards** (4×4, 6×6): Solvable if (inversions + blank row from bottom) is odd

**Inversion**: A pair of tiles where a larger number appears before a smaller number in row-major order.

## Usage

### Input File Format

```
3
1 3 0
4 2 5
7 8 6
```
- First line: board dimension (n)
- Next n lines: n×n grid (0 = blank space)

### Running the Solver

```bash
javac org/example/*.java
java org.example.Solver puzzle.txt
```

### Output

**Console:**
```
Minimum number of moves = 4
3
1 3 0
4 2 5
7 8 6

3
1 2 0
4 3 5
7 8 6
...
```

**Visualization:**
- Animated step-by-step solution
- Blue tiles = correctly positioned
- Pink/red tiles = incorrectly positioned
- Black space = blank tile

## Project Structure

```
org/example/
├── Solver.java    # A* algorithm implementation
└── Board.java     # Puzzle board representation
```

## Class Overview

### Board.java

**Represents puzzle state with:**
- `dimension()`: Board size (n×n)
- `hamming()`: Count of misplaced tiles
- `manhattan()`: Sum of Manhattan distances
- `isGoal()`: Check if solved
- `neighbors()`: Generate possible moves
- `equals()`: Compare board states

### Solver.java

**Implements A* search with:**
- `SearchNode`: Priority queue node with board state and cost
- `isSolvable()`: Check if puzzle can be solved
- `solution()`: Return sequence of boards to solution
- `moves()`: Number of moves in solution
- `visualize()`: Animated display

## Complexity Analysis

**Time Complexity**: O(b^d) in worst case
- b = branching factor (up to 4 moves per state)
- d = solution depth
- A* significantly reduces this with good heuristic

**Space Complexity**: O(b^d)
- Stores explored states in HashSet
- Priority queue contains unexplored nodes

**Optimizations:**
- HashSet prevents revisiting states
- Manhattan heuristic provides tight lower bound
- Previous node check avoids immediate backtracking

## Visualization Features

### Color Scheme
- **Blue (#3CBBFF)**: Correctly positioned tiles
- **Pink (#FF3967)**: Incorrectly positioned tiles
- **Black (#020F15)**: Blank space and background
- **White (#FFFFFF)**: Border lines

### Animation Speed
Automatically adjusts based on solution length:
- ≤10 moves: 700ms per step
- 11-49 moves: 150ms per step
- ≥50 moves: 50ms per step

### Display Information
- Current step number
- Hamming distance
- Manhattan distance
- Completion message

## Example Puzzles

### Easy (4 moves)
```
3
1 3 0
4 2 5
7 8 6
```

### Medium (14 moves)
```
3
4 1 3
0 2 6
7 5 8
```

### Hard (31 moves)
```
3
8 1 3
4 0 2
7 6 5
```

### Unsolvable
```
3
1 2 3
4 5 6
8 7 0
```

## Dependencies

- **Princeton StdDraw**: Graphics library
- **Princeton MinPQ**: Priority queue implementation
- **Princeton In**: File input utility

## Key Insights

1. **A* Optimality**: Always finds shortest solution
2. **Heuristic Choice**: Manhattan > Hamming (more informed)
3. **State Deduplication**: HashSet prevents exponential blowup
4. **Admissible Heuristic**: Manhattan never overestimates cost

## Performance Tips

For faster solving:
- Use Manhattan distance (already implemented)
- Consider linear conflict heuristic for even better performance
- Implement IDA* for memory-constrained environments

## Educational Value

Perfect for learning:
- A* search algorithm
- Heuristic design
- Graph traversal
- Priority queues
- Optimization techniques
- Game state representation
---

**License**: Educational project - free to use and modify.
