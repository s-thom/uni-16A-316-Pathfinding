# COMP316 Assignment 2
Stuart Thomson - swdt1 - 1229521

---

# Report
## Function Results
### Depth First Search

Map    | # Evaluations | Cost of Path
------ | ------------- | ------------
Easy   | 9             | 8.65
Medium | 23            | 21.72
Hard   | 66            | 42.46

What I find interesting in the Depth First searches is that, especially in the Hard one, the robot had to go back and recalculate a path. At some points it had to go back and start from a different point because it had gone down a path that couldn't get to the victim. DFS guarantees that a path will be found, assuming one exists, but doesn't necessarily give the shortest path. However, it is quick to find a path, considering it works 'blind'.

### A*

Map    | # Evaluations | Cost of Path
------ | ------------- | ------------
Easy   | 5             | 5.24
Medium | 39            | 19.72
Hard   | 231           | 35.04

In the medium and hard maps, A* had to evaluate more states than DFS. This was mostly due to the design of the maps, in that they "tricked" A* into doing into a different direction, while the dumb DFS just so happened to go a better way due to the order in which states are added to the stack.

## Program Design

I decided to use a closed list to hold the states that had been expanded instead of modifying the map data itself. I found it much easier to prepend the expanded state to the list than have to modify the matrix of the map and have to deal with the overhead with that. It did mean that I had to check for a state's existence in the closed list, but overall made very little difference to the program.

However, not modifying the matrix lead to a small challenge when printing the completed map out at the end. Instead of being able to just dump it out, I had to go through each position of the matrix and see if that position was in the closed list.

I also decided to add Breadth First Search as an extra. Since `conj` adds items to the end of a vector, it was not difficult to implement.

## Calculated Paths

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
