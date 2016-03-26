package com.example.puzzle;

import com.example.puzzle.Puzzle.ConcurrentSolver;
import com.example.puzzle.Puzzle.Solver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PuzzleSolverFactory extends Remote {

    Solver getPuzzleAsListSolver() throws RemoteException;

    ConcurrentSolver getPuzzleAsMapConcurrentSolver() throws RemoteException;

}
