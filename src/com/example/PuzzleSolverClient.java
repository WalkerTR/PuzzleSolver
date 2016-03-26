package com.example;

import com.example.puzzle.Puzzle;
import com.example.puzzle.Puzzle.Item;
import com.example.puzzle.PuzzleSolverFactory;
import com.example.puzzle.exception.PuzzleFormatException;
import com.example.puzzle.exception.PuzzleItemSetException;
import com.example.puzzle.io.PuzzleFileReader;
import com.example.puzzle.io.PuzzleFileWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;


public class PuzzleSolverClient {

    public static void main(String args[]) {

        if(args.length < 2 || args.length > 4) {
            abort("Invalid number of arguments");
        }

        String inputfile = args[0];
        String outputfile = args[1];
        String hostname = "localhost";
        int threads = 4;

        if (args.length == 3) {
            if (args[2].matches("^\\d+$")) {
                threads = Integer.parseInt(args[2]);
            } else {
                hostname = args[2];
            }
        } else if (args.length == 4) {
            hostname = args[2];
            if (args[3].matches("^\\d+$")) {
                threads = Integer.parseInt(args[2]);
            } else {
                abort("Invalid threads number");
            }
        }

        PuzzleFileReader reader = new PuzzleFileReader(inputfile);
        Set<Item> set = null;
        try {
            set = reader.read();
        } catch (IOException e) {
            abort("Unable to read input file");
        } catch (PuzzleFormatException e) {
            abort("Invalid input format");
        }


        PuzzleSolverFactory factory = null;
        try {
            factory = (PuzzleSolverFactory) Naming.lookup("rmi://" + hostname + "/PuzzleSolverFactory");
        } catch(RemoteException e) {
            abort("Connection failure");
        } catch (MalformedURLException e) {
            abort("Invalid hostname");
        } catch(NotBoundException e) {
            abort("Remote object not found");
        }

        Puzzle.ConcurrentSolver solver = null;
        try {
            solver = factory.getPuzzleAsMapConcurrentSolver();
            solver.setThreadsNumber(threads);
        } catch (RemoteException e) {
            abort("Connection failure");
        }

        try {
            solver.setItems(set);
        } catch (RemoteException e) {
            abort("Connection failure");
        }


        Puzzle puzzle = null;
        try {
            puzzle = solver.solve();
        } catch (RemoteException e) {
            abort("Connection failure");
        } catch (PuzzleItemSetException e) {
            abort("Invalid item set");
        }

        PuzzleFileWriter writer = new PuzzleFileWriter(outputfile);
        try {
            writer.write(puzzle);
        } catch(RemoteException e) {
            abort("Connection failure");
        } catch(IOException e) {
            abort("Unable to write output file");
        }

    }

    private static void abort(String reason) {
        System.err.println("Error: " + reason);
        System.err.println("Abort");
        System.exit(1);
    }

}
