# PSMC_HW1
Sequential and parallel sudoku solver. Statistics and comparison between the two implementations.

> [Programmazione di Sistemi Multicore, Homework1 - Irene Finocchi, 2016, Sapienza Università di Roma]

## SequentialSolver
SequentialSolver implements a sequential recursive, depth-first, backtracking algorithm.
First off, it uses supportive arrays containing only 0's and 1's for valid values' quick individuation, given a cell (row, column), which are created at the moment of input file's reading.
For each empty cell found, it searches for values which keep the Sudoku instance legal; then selects the least-values-allowed cell in the matrix, keeping the values' set.
After that, for each value in the valid values' set, it starts a recursive call on the solving method, moving on a DFS path on the choices tree (ideally).
Backtracking is handled by the recursion call stack.
P.S. Further releases will not optimize this implementation.

## ParallelSolver
ParallelSolver implements a the same algorithmic idea of the SequentialSolver, but the recursion is now swapped to parallel computations, instead, using the Fork/Join library.
For each value in valid values' set for a given cell, it forks a solving task.
P.S. Further releases will not optimize this implementation.

## SeqParSolver
SeqParSolver wrappes up both implementations and compares their execution, showing algorithms' performances statistics other than the given input Sudoku statistics.
Both SequentialSolver and ParallelSolver implementations wrapped up here will be the latest updated versions, containing incremental optimizations.

> Emanuele Giona, 2016
