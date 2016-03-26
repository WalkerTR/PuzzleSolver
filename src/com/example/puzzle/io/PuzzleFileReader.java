package com.example.puzzle.io;

import com.example.puzzle.Puzzle.Item;
import com.example.puzzle.exception.PuzzleFormatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;




public class PuzzleFileReader {

    private Path path;

    public PuzzleFileReader(String path) {
        this.path = Paths.get(path);
    }

    public Set<Item> read() throws IOException, PuzzleFormatException {

        HashSet<Item> set = new HashSet<Item>();

        for(String s : Files.readAllLines(path, StandardCharsets.UTF_8)) {

            String[] fields = s.split("\t",6);

            if( fields.length < 6
             || fields[0].length() == 0
             || fields[1].length() == 0
             || fields[2].length() == 0
             || fields[3].length() == 0
             || fields[4].length() == 0
             || fields[5].length() == 0)
                    throw new PuzzleFormatException("Invalid input format");

            set.add(new Item(
                            fields[0],
                            fields[1].charAt(0),
                            fields[2],
                            fields[3],
                            fields[4],
                            fields[5]
                        )
                    );
        }

        return set;

    }

}
