package com.example.puzzle;

import com.example.puzzle.exception.PuzzleItemSetException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


class PuzzleAsMap extends AbstractPuzzle {

    HashMap<Position,Item> items = new HashMap<Position,Item>();;

    public static ConcurrentSolver getConcurrentSolver() {
        return new PuzzleAsMapConcurrentSolver();
    }

    private PuzzleAsMap() {}

    @Override
    public Item getItem(int row, int column) {
        return items.get(new Position(row,column));
    }

    private static final class Position {

        final private int row;
        final private int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Position other = (Position) obj;
            if (column != other.column) {
                return false;
            }
            if (row != other.row) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + column;
            result = prime * result + row;
            return result;
        }
    }

    private static class PuzzleAsMapConcurrentSolver extends AbstractConcurrentSolver {

        @Override
        public Puzzle solve() throws RemoteException, PuzzleItemSetException {
            PuzzleAsMap puzzle = new PuzzleAsMap();

            String first = null;
            for(Iterator<Item> it = map.values().iterator(); it.hasNext() && first == null;) {
                Item item = it.next();
                if(item.getNorth().equals(empty) && item.getWest().equals(empty)) {
                    first = item.getId();
                }
            }
            if(first == null) {
                throw new PuzzleItemSetException();
            }
            Item temp = map.get(first);
            if(temp == null) {
                throw new PuzzleItemSetException();
            }

            puzzle.items.put(new Position(0,0), temp);

            Thread[] threads = new Thread[getThreadsNumber()];
            Runner runner = new Runner(puzzle);
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(runner);
                threads[i].start();
            }

            for(Thread t : threads) {
                try {t.join();}
                catch(InterruptedException e) {}
            }

            if(runner.hasError() || !runner.hasDone()) {
                throw new PuzzleItemSetException();
            }

            if(!check(puzzle)) {
                throw new PuzzleItemSetException();
            }

            return (Puzzle) UnicastRemoteObject.exportObject(puzzle, 0);

        }

        private class Runner implements Runnable {

            private HashMap<Position,Item> items;
            private ConcurrentLinkedQueue<Position> todo = new ConcurrentLinkedQueue<Position>();
            private AtomicBoolean error = new AtomicBoolean(false);
            private AtomicBoolean done = new AtomicBoolean(false);

            public Runner(PuzzleAsMap puzzle) {
                this.items = puzzle.items;
                todo.add(new Position(0,0));
            }

            public boolean hasError() {
                return error.get();
            }

            public boolean hasDone() {
                return done.get();
            }

            @Override
            public void run() {
                Position position = null,tempPosition;
                Item item,tempItem;
                String next;

                try {
                    while(!done.get() && !error.get() && !Thread.interrupted()) {

                        if(!done.get() && !error.get() && (position = todo.poll()) == null) {

                            synchronized(this) {
                                while(!done.get() && !error.get() && (position = todo.poll()) == null) {
                                    if(items.size() < map.size()) {
                                        this.wait();
                                    } else {
                                        done.set(true);
                                        this.notifyAll();
                                    }
                                }
                            }

                        }


                        if(!done.get() && !error.get()) {

                            synchronized(this) {
                                item = items.get(position);
                            }
                            if(item == null) {
                                throw new PuzzleItemSetException();
                            }

                            next = item.getEst();
                            if(!next.equals(empty)) {
                                tempPosition = new Position(position.getRow(),position.getColumn()+1);
                                synchronized(this) {
                                    if(!items.containsKey(tempPosition)) {
                                        tempItem = map.get(next);
                                        if(tempItem == null) {
                                            throw new PuzzleItemSetException();
                                        }
                                        items.put(tempPosition, tempItem);
                                        todo.add(tempPosition);
                                        this.notify();
                                    }
                                }
                            }

                            next = item.getSouth();
                            if(!next.equals(empty)) {
                                tempPosition = new Position(position.getRow()+1,position.getColumn());
                                synchronized(this) {
                                    if(!items.containsKey(tempPosition)) {
                                        tempItem = map.get(next);
                                        if(tempItem == null) {
                                            throw new PuzzleItemSetException();
                                        }
                                        items.put(tempPosition, tempItem);
                                        todo.add(tempPosition);
                                        this.notify();
                                    }
                                }
                            }


                        }
                    }

                }
                catch (InterruptedException e) {}
                catch (PuzzleItemSetException e) {
                    error.set(true);
                    synchronized(this) {
                        this.notifyAll();
                    }
                }
            }
        }
    }
}
