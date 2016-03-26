package com.example.puzzle;

import com.example.puzzle.Puzzle.ConcurrentSolver;
import com.example.puzzle.Puzzle.Solver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConcreteFactory implements PuzzleSolverFactory {

    private static final long serialVersionUID = 5631457409442988540L;

    public ConcreteFactory() throws RemoteException {}

    @Override
    public Solver getPuzzleAsListSolver() throws RemoteException {
        return (Solver) UnicastRemoteObject.exportObject(PuzzleAsList.getSolver(), 0);
    }

    @Override
    public ConcurrentSolver getPuzzleAsMapConcurrentSolver() throws RemoteException {
        return (ConcurrentSolver) UnicastRemoteObject.exportObject(PuzzleAsMap.getConcurrentSolver(), 0);
    }

}
