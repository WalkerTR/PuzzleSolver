package com.example;

import com.example.puzzle.ConcreteFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class PuzzleSolverServer {

    public static void main(String[] args) {

        if(args.length > 1) {
            abort("Invalid number of arguments");
        }

        String hostname = "localhost";

        if(args.length == 1) {
            hostname = args[0];
        }

        try {
            ConcreteFactory factory = new ConcreteFactory();
            Naming.rebind("rmi://" + hostname + "/PuzzleSolverFactory", UnicastRemoteObject.exportObject(factory, 0));
        } catch (RemoteException e) {
            abort("Connection failure");
        } catch (MalformedURLException e) {
            abort("Invalid hostname");
        }

        System.out.println("Server ready");

    }

    private static void abort(String reason) {
        System.err.println("Error: " + reason);
        System.err.println("Abort");
        System.exit(1);
    }

}
