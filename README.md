# TCP-Network-Tic-Tac-Toe
Arithmatic Tic-Tac-Toe project. 
Purpose: Functional Tic-Tac-Toe with arithmatic between two users over a network. 
- Game serves as a small-scale practice in multithreading in Java as there are multiple race conditions that must be accounted for in order for the game of tic-tac-toe to progress without bugs
- The game utilizes an arithmatic interpretation of the classic game. The goal is for the first player to add up their numbers to 15 within any given roll/column/diagonal for the game to end.

File Summaries:

board: Basic serializable object used as data between the players of the game. 
serverThread: Purpose is to initiate the game of tic-tac-toe. 
- If two players request to the server to 'start' the game, then the server assigns information to the players, but is not responsible for the actual game between the players
tcpClient 1-2: main function threads for the two respective players of the game.
- Clients communicate with each other through TCP pipelines where they pass 'threadSharedMemory' as the game
threadSharedMemory: Object that forces the client code to be synchronized.
- Various flags are used to prevent deadlocks and race conditions

