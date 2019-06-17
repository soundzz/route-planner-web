{\rtf1\ansi\ansicpg1252\cocoartf1671\cocoasubrtf200
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 Routenplaner program Phase 1 handover date: 20.06.2019\
\
GENERAL NOTES\
\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\
- the elapse time is in ms\
- create a folder mandate and a folder Benchs\
- the mapdata filename must be in the folder mapdata\
- the .que files must be in the folder Benchs\
- if the Query file is not found you are back in the main menu, check if the Query filname was right, or      if it is in the folder /Benchs \
- if you load a different Query file than the mapdata file, you might get a index out of bounce exception if the Query file has lager node numbers then the nodes in the mapdata file \
- If you can select the options 1,2,3, or 4, don\'92t put any other character\
- If you can enter a start node do not enter a number outside of 0- 25115476\
\
\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\
\
\
HOW TO USE NOTES\
\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\'97\
\
1) Enter the mapdata filename (for example germany.fmi)\
-the output on the console is the elapsed time it took to import the graph\
\
2) Select an action with 1,2,3 or 4.\
-1 is for a Query file that has many different starting nodes (for example germany2.que)\
-2 is for a Query file that has many request with the same starting node (for example germany.que)\
-3 is for the One-to- all Dijkstra starting by a start node that you put in the console in step 3)\
-4 you exit the program\
\
3) \
If you chose 1:\
You must enter a query filename (for example germany.que)\
- if the process is done the result can be found in the Solution file ouput.sol\
- to check if its the same as the solution enter the Diff- command in the console\
If you chose 2:\
You must enter a query filename (for example germany.que)\
- if the process is done the result can be found in the Solution file ouput.sol\
- to check if its the same as the solution enter the Diff- command in the console\
If you chose 3:\
You must enter a start node with a number from 0 until 25115476\
- the output on the console is the elapsed time it took to do the One-to-all Dijkstra from the start node\
- afterwards you can enter a target node and you get the path costs as an output or you can enter -1 to return to the main menu\
	If you chose to put a target node, then you get the costs and can enter another target node or -1\
	If you chose to enter -1 you return to the main menu\
If you chose 4:\
the progam stops. }