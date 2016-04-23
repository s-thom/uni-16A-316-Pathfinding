(ns assignment2.core
  (:gen-class))
; Data Formats
;
; Position:
; {
;   :x number
;   :y number
;   :depth number (not needed in map)
;   :clear boolean (only used in map)
;   :weight number (optional, calculate if needed)
; }

; Global to translate keywords into file names
(def maps {
  :easy "map-small"
  :medium "map-med"
  :hard "map-large"
  ; Some extra maps I used for testing
  :316 "map-316"
  :impossible "map-impossible"
  :empty "map-empty"
  })

;  GENERIC UTILITIES

(defn reload
  "Just a quick function to reload the project in the REPL"
  []
  (use 'assignment2.core :reload)
  true) ; Give it a return value to indicate a success

(defn square
  "Returns the square of the number"
  [number]
  (* number number))

(defn get-distance
  "Returns the Euclidean distance between two points"
  [a b]
  (Math/sqrt
    (+
      (square (- (:x a) (:x b)))
      (square (- (:y a) (:y b)))
      )))

(defn ret-print
  [a]
  (println a)
  a)

; OTHER UTILITIES

(defn load-map
  "Returns a matrix (seq of seq) defining the map"
  [key]
  (mapv
    (fn [line]
      (vec (clojure.string/split line #"")))
    (clojure.string/split-lines (slurp (key maps)))
    ))

(defn find-at
  "Finds the map position data at the two coordinates"
  [map-matrix x y]
  (nth (nth map-matrix y) x)
  ; TODO: write function
  )

(defn give-start
  "Returns a start entry at the given position"
  [pos]
  (conj pos {
    :history '()
    :cost 0
    :depth 0
    }))

(defn give-start-list
  "Returns a starter \"open list\""
  [pos]
  (conj '() (give-start pos)))

(defn reduce-info
  "Cuts out a lot of information for readability"
  [item]
  (let [x {
    :x (:x item)
    :y (:y item)
    }]
    (if (:heur item)
      (conj x {:huer (:heur item)})
      x))
  )

(defn goal?
  "Predicate to check if current position is goal"
  [pos map-matrix]
  (= "v" (find-at map-matrix (:x pos) (:y pos))))

(defn pos-of
  "Returns the position of a string in the map, or nil if not found. Uses DFS"
  [str map-matrix]
  (loop
    [y 0]
    ; Make sure we're within the bounds of the map
    (if (< y (count map-matrix))
      ; Check entire row (see inner loop), if value returned, yay!
      (if-let
        [
          x-val (loop
            [x 0]
            ; Stick between the lines
            (if (< x (count (nth map-matrix y)))
              ; Check to see if the current position is the right string
              (if (= str (find-at map-matrix x y))
                x
                ; Incorrect? Try again with the next column
                (recur (inc x)))
              false
              ))
        ]
        ; Yay!
        {:x x-val :y y}
        ; Try next row
        (recur (inc y))
        )
      nil
      )))

(defn has-pos
  [list pos]
  (loop
    [
      index 0
    ]
    (if (< index (count list))
      (let [item (nth list index)]
        (if
          (and
            (=
              (:x item)
              (:x pos))
            (=
              (:y item)
              (:y pos)))
          true
          (recur (inc index))
          ))
      false)))

(defn print-history
  [history map-matrix]
  ; TODO: Print history
  (println "will print history soon")
  )

; EXPANSION

(defn find-next-steps
  "Returns a sequence of positions to be added to the open list"
  [pos map-matrix closed-list goal]
  ; goal is an optoinal parameter (nil/false for left out)
  ; If defined, calculate cost to continue
  (map
    #(let
      [
        nx (+ (:x pos) (:x %))
        ny (+ (:y pos) (:y %))
      ]
      ; Check is a valid position in map (square) and not the position being tested
      (if
        (and
          (or
            (not= (:x %) 0)
            (not= (:y %) 0))
          (> nx -1)
          (> ny -1)
          (< nx (count (first map-matrix)))
          (< ny (count map-matrix)))

          (if-not (has-pos closed-list {:x nx :y ny})
            ; (do
            ;   (println "not has")
            (if-not (= "R" (find-at map-matrix nx ny))
              ; Add to open list
              (let
                [
                  potential {
                    :x nx
                    :y ny
                    :depth (inc (:depth pos))
                    :cost (+ (get-distance % {:x 0 :y 0}) (:cost pos))
                    :history (conj (:history pos) (reduce-info pos))
                    ; :history '()
                  }
                ]
                ; Add estimated distance if goal given
                (if-not goal
                  potential
                  (conj
                    potential
                    {
                      :est-dist (get-distance potential goal)
                      :heur (+ (:cost potential) (get-distance potential goal))
                    }
                )))
          ))))
    ; List of positions adjacent to this one
    ; TODO: Generate this rather than specify directly
    ;       Why? Because I feel like it
    '(
      {:x -1 :y -1}
      {:x -1 :y 0 }
      {:x -1 :y 1}
      {:x 0 :y -1}
      {:x 0 :y 1}
      {:x 1 :y -1}
      {:x 1 :y 0}
      {:x 1 :y 1}
      )))

(defn expand-astar
  "Expands according to the A* algorithm"
  [map-matrix open-list closed-list goal]
  (let [pos (first open-list)]
    ; This is the interstion function.
    ; For A*, insert items in order of preference
    (reduce
      #(if-not (nil? %2)

        (let
          [
            halves (split-with
              (fn [item]
                (<
                  (:heur item)
                  (:heur %2)))
              %)
          ]
          (reduce
            conj
            (conj
              (reverse (first halves))
              (conj
                (second halves)
                %2
                ))))
        %)
      (conj
        (find-next-steps pos map-matrix closed-list goal)
        (rest open-list)))
        ))

(defn expand-dfs
  "Expands according to the Depth First Search algorithm"
  [map-matrix open-list closed-list goal]
  ; Goal parameter is here to keep function signatures the same between this and astar
  ; Is not used in this function
  ; Check for goal completion
  (let [pos (first open-list)]
    ; This is the interstion function.
    ; For DFS, insert items to the front
    (reduce
      #(if-not (nil? %2)
        (conj % %2)
        %)
      (conj
        (find-next-steps pos map-matrix closed-list nil)
        (rest open-list)))
        ))

; MAIN FUNCTIONS

(defn run
  [map-key expander cont-prnt]
  (let
    [
      map-mat (load-map map-key)
      start-pos (pos-of "*" map-mat)
      goal-pos (pos-of "v" map-mat)
    ]
    (loop
      [
        open-list (give-start-list start-pos)
        closed-list '()
      ]
      ; (println "open list " open-list)
      ; (println "closed list " closed-list)
      (if (= 0 (count open-list))
        (do
          (println "Unable to find a path")
          false
          )
        (do
          (if cont-prnt ; Print state if param specified
            (println (reduce-info (first open-list))))
          (if-not (goal? (first open-list) map-mat)
              (recur  ; Go in for another recursion
                (expander
                  map-mat
                  open-list
                  (conj closed-list (first open-list))
                  goal-pos
                  )
                (conj closed-list (first open-list))
                )
            (do ; Finish up
              (print-history (:history (first open-list)) map-mat)

              true
            ))))
      ))
  )

(defn astar
  [& params]
  (let
    [args (apply assoc {} params)]
    (run (:scenario args) expand-astar (:print-states args))
    ))

(defn dfs
  [& params]
  (let
    [args (apply assoc {} params)]
    (run (:scenario args) expand-dfs (:print-states args))
    ))

; MISC.

(defn -main
  "Does pretty much nothing"
  [& args]
  (println "No, you need to use the REPL. Go there instead of here."))
