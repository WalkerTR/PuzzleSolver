# Distributed Puzzle Solver

#### Description

This is my "Concurrent and Distributed Programming" course project.

The aim of this project is to provide an example of Java Concurrency and Java RMI.


#### Server arguments
**Syntax:** `puzzlesolverserver.sh [hostname[:port]]`
- **[hostname[:port]]**
The location of the RMI Registry. Default value: `localhost:1099`.

#### Client arguments
**Syntax:** `puzzlesolverclient.sh input output [hostname[:port]] [threads]`
- **input**
Path to the input file
- **output**
Path to the output file
- **[hostname[:port]]**
The location of the RMI Registry. Default value: `localhost:1099`.
- **[threads]**
The number of threads used for solving the puzzle. Default value: `4`.


#### Puzzle format
A puzzle is a matrix NxM, where each item has:
- Universal identifier [String]
- Content [char]
- Top cell's ID (underscore if empty) [String]
- Right cell's ID (underscore if empty) [String]
- Bottom cell's ID (underscore if empty) [String]
- Left cell's ID (underscore if empty) [String]

#### Input file format
Each row represents a single puzzle item. The row syntax is:

`ID \t CONTENT \t NORTHID \t RIGHTID \t BOTTOMID \t LEFTID`

#### Output file format
The output of the program is the following:
- The string of the items' content
- An empty line
- The puzzle matrix representation
- An empty line
- The size of the puzzle matrix


#### Example usage
1. Compile the project using the provided Makefile

    `$> make`

2. Start the RMI Registry from the bin folder

    `$> cd bin`

    `$> rmiregistry`

3. Start the server

    `$> ./puzzlesolverserver.sh`

4. Start the client

    `$> ./puzzlesolverclient.sh input.txt output.txt`
