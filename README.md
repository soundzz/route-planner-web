Routenplaner program Phase 1 handover date: 20.06.2019

GENERAL NOTES
————————————————————————
- the folders mapdata and Benchs need to be in the folder src
- execute build.sh in the src folder to open the program:
	chmod -x build.sh
	./build.sh)
- the elapse time is in ms
- create a folder mandate and a folder Benchs
- the mapdata filename must be in the folder mapdata
- the .que files must be in the folder Benchs
- if the Query file is not found you are back in the main menu, check if the Query filname was right, or if it is in the folder /Benchs 
- if you load a different Query file than the mapdata file, you might get a index out of bounds exception if the Query file has lager node numbers then the nodes in the mapdata file 
- If you can select the options 1,2,3, or 4, don’t put any other character
- If you can enter a start node do not enter a number outside of 0- 25115476
- We decided to divide the Query file input in two cases, one with many different start nodes and the other one with request with many start nodes that are the same (for optimization purposes)

————————————————————————


HOW TO USE NOTES
————————————————————————

1) Enter the mapdata filename (for example germany.fmi)
-the output on the console is the elapsed time it took to import the graph

2) Select an action with 1,2,3 or 4.
-1 is for a Query file that has many different starting nodes (for example germany2.que)
-2 is for a Query file that has many request with the same starting node (for example germany.que)
-3 is for the One-to- all Dijkstra starting by a start node that you put in the console in step 3)
-4 you exit the program

3) 
If you chose 1:
You must enter a query filename (for example germany.que)
- if the process is done the result can be found in the Solution file ouput.sol
- to check if its the same as the solution enter the Diff- command in the console
If you chose 2:
You must enter a query filename (for example germany.que)
- if the process is done the result can be found in the Solution file ouput.sol
- to check if its the same as the solution enter the Diff- command in the console
If you chose 3:
You must enter a start node with a number from 0 until 25115476
- the output on the console is the elapsed time it took to do the One-to-all Dijkstra from the start node
- afterwards you can enter a target node and you get the path costs as an output or you can enter -1 to return to the main menu
	If you chose to put a target node, then you get the costs and can enter another target node or -1
	If you chose to enter -1 you return to the main menu
If you chose 4:
the progam stops. 
