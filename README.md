# COMP316 Assignment 2
Stuart Thomson - swdt1 - 1229521

---

# How to run the functions

1. Open the REPL
2. Use one of the following commands,
  - For Depth First Search: `(dfs :scenario <scenario as keyword> :print-states <boolean>)`
  - For A*: `(dfs :scenario <scenario as keyword> :print-states <boolean>)`
3. The output will be in the format below:
  ```
  ; If :print-states was true, include...
  Expanded States:
  <List of reduced-info states in the following format>
  {:x <column> :y <row> :heur <heuristic value, only included in A*>}
  ; The following is always included
  Total States Evaluated: <# of states expanded>
  Number of Steps in Path: <# of states that existed in the path>
  Path Length: <Length of path>
  History
  <Print out of the map with '*' marking where the robot traveled>
  true
  ```
4. If it is impossible for the robot to reach the goal (i.e. the open list is empty), the output will look like:
  ```
  Expanded States:
  ; If :print-states was true, include...
  Expanded States:
  <List of reduced-info states in the following format>
  {:x <column> :y <row> :heur <heuristic value, only included in A*>}
  ; The following is always included
  Total States Evaluated: <# of states expanded>
  Unable to find a path
  false
  ```
