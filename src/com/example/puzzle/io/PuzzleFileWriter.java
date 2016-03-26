package com.example.puzzle.io;

import com.example.puzzle.Puzzle;
import com.example.puzzle.Puzzle.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



public class PuzzleFileWriter {

    private Path path;

    public PuzzleFileWriter(String path) {
        this.path = Paths.get(path);
    }

    public void write(Puzzle puzzle) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            writer.write(puzzle.stringify() + "\n");

            writer.write("\n");

            for(List<Item> I : puzzle.getItems()) {
                for(Item i : I) {
                    writer.write(i.toString());
                }
                writer.write("\n");
            }

            writer.write("\n");

            writer.write(puzzle.getRowsCount() + " " + puzzle.getColumnsCount());

        }

    }

}
