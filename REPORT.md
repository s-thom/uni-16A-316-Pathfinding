# COMP316 Assignment 2

---

# Report
## Function Results

**Depth First Search**

Map    | # Evaluations | Cost of Path
------ | ------------- | ------------
Easy   |  9            |  8.65
Medium | 23            | 21.72
Hard   | 66            | 42.46

**A\***

Map    | # Evaluations | Cost of Path
------ | ------------- | ------------
Easy   |   5           |  5.24
Medium |  39           | 19.72
Hard   | 231           | 35.04

**Extra: Breadth First Search**

Map    | # Evaluations | Cost of Path
------ | ------------- | ------------
Easy   |  15           |  5.24
Medium |  41           | 19.72
Hard   | 349           | 35.04

As expected, Depth First Search did not chose a path intelligently. In all cases it had to travel further along its path than either of the other algorithms. One advantage it did have in the `:medium` and `:hard` scenarios is that it took the lowest amount of calculation. Not only does it not need to calculate a heuristic, but expanded states only need to be added to the front of the open list, which Clojure's `conj` does to lists.

I did encounter some interesting behavior with DFS, which I've documented in Appendix 1.

A* did the best out of all the algorithms in terms of finding the best path. In all cases it found the shortest path, and with fewer expansions than BFS. In most cases it did need to expand more than Depth First Search, but it makes up for this be choosing the lowest cost path.

I found the Breadth First Search case a little interesting. In the `:hard` scenario it showed that it doesn't take any sort of heuristic into account. It didn't choose the lowest cost path, rather it chose a path with the lowest number of steps. While the lowest cost path had the same number of steps, BFS chose the first solution it encountered. Implementing BFS was trivial, as all I had to do was convert the open list to a vector, which makes `conj` add to the end of the list.

## Program Design

The `expand-<algorithm>` functions hold the differences between the algorithms. While DFS and BFS just add to the list, A* requires a bit more calculation. It uses insertion sort to order the open list in the sequence to expand.

`find-next-steps` creates the states around the one being expanded, and, if a goal is given, calculates the heuristic needed for the A* algorithm. This is used by the `expand-<x>` functions to get the states to add to the list. I had to be careful, as the `map` function returned `nil` if the state shouldn't be added.

Everything was run by, well, `run`, which deals with starting, looping, and finishing the program. This function is passed an `expand-<x>` function to use, which determines which algorithm is used.

I decided to use a closed list to hold the states that had been expanded instead of modifying the map data itself. This saved me having to modify the matrix of the map and have to deal with the overhead of that. It did mean that I had to check for a state's existence in the closed list, but overall made very little difference to the program.

However, not modifying the matrix lead to a small challenge when printing the completed map out at the end. Instead of being able to just dump it out, I had to go through each position of the matrix and see if that position was in the closed list.

## Calculated Paths

Direct output from REPL.

Start marked by `S`. End marked by `v`.

### Easy

**DFS**
```
---vR
-R-R*
----*
R-*R*
S*R*-
```

**A\***
```
---vR
-R*R-
--*--
R*-R-
S-R--
```

**BFS**
```
---vR
-R*R-
-*---
R*-R-
S-R--
```

### Medium

**DFS**
```
---*****-
-RvRRRRR*
RRRRRR-*R
R---RRRR*
--R-R-R*R
-RR---R*-
RRRRRR*RR
-SR---R*-
R-*****-R
```

**A\***
```
---*****-
-RvRRRRR*
RRRRRR-*R
R---RRRR*
--R-R-R*R
-RR---R*-
RRRRRR*RR
-SR***R--
R-*-----R
```

**BFS**
```
---*****-
-RvRRRRR*
RRRRRR-*R
R---RRRR*
--R-R-R*R
-RR---R*-
RRRRRR*RR
-SR***R--
R-*-----R
```


### Hard

**DFS**
```
---R----R--------R--------
---R----R----R---R----R---
---R---------R-----RRRR---
-RRRRR--RRRRRR---R----R---
-------------R---R----R-RR
-------------R---R----R--R
RRRRRRRR---------R----R--R
---------R-RRRRRRRRRR-*--R
---------R-----------*R*-R
R-RRRRRRRRRRRR------*RRR*R
---------R----------*R-*-R
---------R---R------*R*R--
RRRRRRRR-----R------*RvR--
---------RRRRRRRRRR*RRRRRR
---------R---R------*-R---
---------R---R-------*R---
-------------R-------*R---
--------RRRRRR----***-R---
-------------R---*RRRRRRR-
--------R----R---*R----R--
RRRR-RRRRRRRRR---*R----R--
--------R----****-RRRR-R--
--RRRRR-*---*RRRRRR-------
----S***R*--*R----R---R---
---R-RRRR-*-*R--------R---
--------R--*-R----R---R---
```

**A\***
```
---R----R--------R--------
---R----R----R---R----R---
---R---------R-----RRRR---
-RRRRR--RRRRRR---R----R---
-------------R---R----R-RR
-------------R---R----R--R
RRRRRRRR---------R----R--R
---------R-RRRRRRRRRR-*--R
---------R-----------*R*-R
R-RRRRRRRRRRRR------*RRR*R
---------R----------*R-*-R
---------R---R------*R*R--
RRRRRRRR-----R------*RvR--
---------RRRRRRRRRR*RRRRRR
---------R---R------*-R---
---------R---R-----*--R---
-------------R----*---R---
--------RRRRRR---*----R---
-------------R--*-RRRRRRR-
--------R----R-*--R----R--
RRRR-RRRRRRRRR*---R----R--
--------R*****----RRRR-R--
--RRRRR**----RRRRRR-------
----S**-R----R----R---R---
---R-RRRR----R--------R---
--------R----R----R---R---
```

**BFS**
```
---R----R--------R--------
---R----R----R---R----R---
---R---------R-----RRRR---
-RRRRR--RRRRRR---R----R---
-------------R---R----R-RR
-------------R---R----R--R
RRRRRRRR---------R----R--R
---------R-RRRRRRRRRR-*--R
---------R-----------*R*-R
R-RRRRRRRRRRRR------*RRR*R
---------R---------*-R-*-R
---------R---R----*--R*R--
RRRRRRRR-----R----*--RvR--
---------RRRRRRRRRR*RRRRRR
---------R---R----*---R---
---------R---R---*----R---
-------------R--*-----R---
--------RRRRRR-*------R---
-------------R*---RRRRRRR-
--------R----R*---R----R--
RRRR-RRRRRRRRR*---R----R--
--------R*****----RRRR-R--
--RRRRR**----RRRRRR-------
----S**-R----R----R---R---
---R-RRRR----R--------R---
--------R----R----R---R---
```

## Appendix

### Appendix 1

The following is the path that DFS took on the `:empty` scenario (included in the project).

```
v---*---*---*---*---*---*-
-*-*-*-*-*-*-*-*-*-*-*-*-*
-*-*-*-*-*-*-*-*-*-*-*-*-*
-*-*-*-*-*-*-*-*-*-*-*-*-*
*--*-*-*-*-*-*-*-*-*-*-*-*
-**--*-*-*-*-*-*-*-*-*-*-*
-----*-*-*-*-*-*-*-*-*-*-*
-****--*-*-*-*-*-*-*-*-*-*
*------*-*-*-*-*-*-*-*-*-*
-******--*-*-*-*-*-*-*-*-*
---------*-*-*-*-*-*-*-*-*
-********--*-*-*-*-*-*-*-*
*----------*-*-*-*-*-*-*-*
-**********--*-*-*-*-*-*-*
-------------*-*-*-*-*-*-*
-************--*-*-*-*-*-*
*--------------*-*-*-*-*-*
-**************--*-*-*-*-*
-----------------*-*-*-*-*
-****************--*-*-*-*
*------------------*-*-*-*
-******************--*-*-*
---------------------*-*-*
-********************--*-*
*----------------------*-*
-**********************--S
```

The seemingly strange pattern in a result of two factors:

1. Items in the open list are not added again. This explains the 1 space gap between the lines.
2. The order in which directions are evaluated has a major factor on DFS.

I specifically "cooked" this example to show off the second point. I wanted to compare the absolute worst case for DFS, which was extremely easily solvable by A*. BFS had trouble too, needing to expand almost all of the states in order to reach the goal.

Algorithm | Evaluations
--------- | -----------
DFS       | 351
A*        |  26
BFS       | 626

While in this case DFS was extremely unsuccessful, it can also be highly successful. If you swap the start and goal positions (which is in the scenario `:empty2`) the DFS is the most efficient, as it takes less computation time than A*.
