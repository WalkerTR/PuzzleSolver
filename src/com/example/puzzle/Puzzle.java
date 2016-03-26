package com.example.puzzle;

import com.example.puzzle.exception.PuzzleItemSetException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Puzzle extends Remote {

    public int getRowsCount() throws RemoteException;

    public int getColumnsCount() throws RemoteException;

    public Item getItem(int i, int j) throws RemoteException;

    public List<Item> getRow(int index) throws RemoteException;

    public List<Item> getColumn(int index) throws RemoteException;

    public List<List<Item>> getItems() throws RemoteException;

    public String stringify() throws RemoteException;

    public static interface Solver extends Remote {

        void setItems(Iterable<Item> items) throws RemoteException;

        Puzzle solve() throws RemoteException, PuzzleItemSetException;

    }

    public static interface ConcurrentSolver extends Solver {

        void setThreadsNumber(int thread) throws RemoteException;

        int getThreadsNumber() throws RemoteException;

    }


    public static final class Item implements Serializable {

        private static final long serialVersionUID = -5758613535606900024L;

        private final String id;
        private final char character;
        private final String north;
        private final String est;
        private final String south;
        private final String west;

        public Item(String id, char character, String north, String est, String south, String west) {
            this.id = id;
            this.character = character;
            this.north = north;
            this.est = est;
            this.south = south;
            this.west = west;
        }

        public String getId() {
            return id;
        }

        public char getCharacter() {
            return character;
        }

        public String getNorth() {
            return north;
        }

        public String getEst() {
            return est;
        }

        public String getSouth() {
            return south;
        }

        public String getWest() {
            return west;
        }

        @Override
        public String toString() {
            return String.valueOf(getCharacter());
        }

    }


}
