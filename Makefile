build: bin bin/com/example/PuzzleSolverServer.class bin/com/example/PuzzleSolverClient.class puzzlesolverserver.sh puzzlesolverclient.sh

clean:
	rm -rf bin puzzlesolverserver.sh puzzlesolverclient.sh

bin:
	mkdir bin


puzzlesolverserver.sh:
	echo '#!/bin/sh' > puzzlesolverserver.sh
	echo 'java -cp bin com.example.PuzzleSolverServer $$1' >> puzzlesolverserver.sh
	chmod +x puzzlesolverserver.sh

puzzlesolverclient.sh:
	echo '#!/bin/sh' > puzzlesolverclient.sh
	echo 'java -cp bin com.example.PuzzleSolverClient $$1 $$2 $$3' >> puzzlesolverclient.sh
	chmod +x puzzlesolverclient.sh


bin/com/example/PuzzleSolverServer.class:
	javac -d bin -cp src src/com/example/PuzzleSolverServer.java

bin/com/example/PuzzleSolverClient.class:
	javac -d bin -cp src src/com/example/PuzzleSolverClient.java


















