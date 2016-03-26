package com.example.puzzle;

import com.example.puzzle.exception.PuzzleItemSetException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class AbstractPuzzle implements Puzzle {

    @Override
    public int getRowsCount() {
        int i = 0;

        while(getItem(i,0) != null) {
            i++;
        }

        return i;
    }

    @Override
    public int getColumnsCount() {
        int i = 0;

        while(getItem(0,i) != null) {
            i++;
        }

        return i;
    }

    @Override
    public abstract Item getItem(int row, int column);

    @Override
    public List<Item> getRow(int index) {
        ArrayList<Item> list = new ArrayList<Item>();

        int columns = getColumnsCount();

        for(int i = 0; i < columns; ++i) {
            list.add(getItem(index,i));
        }

        return list;
    }

    @Override
    public List<Item> getColumn(int index) {
        ArrayList<Item> list = new ArrayList<Item>();

        int rows = getRowsCount();

        for(int i = 0; i < rows; ++i) {
            list.add(getItem(i,index));
        }

        return list;
    }

    @Override
    public List<List<Item>> getItems() {
        List<List<Item>> list = new ArrayList<List<Item>>();

        int rows = getRowsCount();
        int columns = getColumnsCount();

        for(int i = 0; i < rows; ++i) {
            list.add(new ArrayList<Item>());
            for(int j = 0; j < columns; ++j) {
                list.get(i).add(getItem(i,j));
            }
        }

        return list;
    }

    @Override
    public String stringify() {
        StringBuilder builder = new StringBuilder();

        int rows = getRowsCount();
        int columns = getColumnsCount();

        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                builder.append(getItem(i,j));
            }
        }

        return builder.toString();
    }

    public static abstract class AbstractSolver implements Solver {
        protected HashMap<String,Item> map = new HashMap<String,Item>();
        protected final static String empty = "_";

        @Override
        public void setItems(Iterable<Item> items) {

            map.clear();

            for(Item item : items) {
                map.put(item.getId(), item);
            }

        }

        @Override
        public abstract Puzzle solve() throws RemoteException, PuzzleItemSetException;

        protected boolean check(Puzzle puzzle) throws RemoteException {
            int rows = puzzle.getRowsCount();
            int columns = puzzle.getColumnsCount();
            Item item;

            for(int i = 0; i < rows; ++i)
                for(int j = 0; j < columns; ++j) {

                    item = puzzle.getItem(i, j);

                    if(item == null) {
                        return false;
                    }

                    if(i == 0) {
                        if(!item.getNorth().equals(empty))
                            return false;
                    } else {
                        if(!item.getNorth().equals(puzzle.getItem(i-1, j).getId()))
                            return false;
                    }

                    if(j == columns-1) {
                        if(!item.getEst().equals(empty))
                            return false;
                    } else {
                        if(!item.getEst().equals(puzzle.getItem(i, j+1).getId()))
                            return false;
                    }

                    if(i == rows-1) {
                        if(!item.getSouth().equals(empty))
                            return false;
                    } else {
                        if(!item.getSouth().equals(puzzle.getItem(i+1, j).getId()))
                            return false;
                    }

                    if(j == 0) {
                        if(!item.getWest().equals(empty))
                            return false;
                    } else {
                        if(!item.getWest().equals(puzzle.getItem(i, j-1).getId()))
                            return false;
                    }
                }

                return true;

        }
    }

    public static abstract class AbstractConcurrentSolver extends AbstractSolver implements ConcurrentSolver {
        private int thread = 1;

        @Override
        public void setThreadsNumber(int thread) {
            if(thread > 1) {
                this.thread = thread;
            } else {
                this.thread = 1;
            }
        }

        @Override
        public int getThreadsNumber() {
            return thread;
        }
    }

}
