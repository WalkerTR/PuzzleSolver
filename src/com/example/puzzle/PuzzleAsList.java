package com.example.puzzle;

import com.example.puzzle.exception.PuzzleItemSetException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class PuzzleAsList extends AbstractPuzzle {

    private List<List<Item>> items = new ArrayList<List<Item>>();;

    public static Solver getSolver() {
        return new PuzzleAsListSolver();
    }

    private PuzzleAsList() {}

    @Override
    public int getRowsCount() {
        return items.size();
    }

    @Override
    public int getColumnsCount() {
        return items.get(0).size();
    }

    @Override
    public Item getItem(int row, int column) {
        try {
            return items.get(row).get(column);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private static class PuzzleAsListSolver extends AbstractSolver {

        @Override
        public Puzzle solve() throws RemoteException, PuzzleItemSetException {

            PuzzleAsList puzzle = new PuzzleAsList();
            String next = null;

            for(Iterator<Item> it = map.values().iterator(); it.hasNext() && next == null;) {
                Item item = it.next();
                if(item.getNorth().equals(empty) && item.getWest().equals(empty)) {
                    next = item.getId();
                }
            }
            if(next == null){
                throw new PuzzleItemSetException();
            }



            while(!next.equals(empty)) {
                Item temp = map.get(next);

                if(temp == null) {
                    throw new PuzzleItemSetException();
                }

                ArrayList<Item> list = new ArrayList<Item>();
                list.add(temp);
                puzzle.items.add(list);
                next = temp.getSouth();
            }


            for(List<Item> list : puzzle.items) {
                next = list.get(0).getEst();

                while(!next.equals(empty)) {
                    Item temp = map.get(next);

                    if(temp == null) {
                        throw new PuzzleItemSetException();
                    }

                    list.add(temp);
                    next = temp.getEst();
                }
            }

            if(!check(puzzle)) {
                throw new PuzzleItemSetException();
            }

            return (Puzzle) UnicastRemoteObject.exportObject(puzzle, 0);
        }

    }

}
