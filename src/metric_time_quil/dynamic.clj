(ns metric-time-quil.dynamic
  (:require [quil.core :as q]))

(def background-color 255)
(def foreground-color 0)

(defn rotate-fraction [phi] (q/rotate (* phi q/TWO-PI)))
(defn scale-x [x] (* x (q/width)))
(defn scale-y [y] (* y (q/height)))
(defn scaled [fun xa ya xb yb]
  (fun (scale-x xa) (scale-y ya) (scale-x xb) (scale-y yb)))

(defn clock-base []
  (q/no-fill)
  (q/stroke foreground-color)

  (q/stroke-weight 2)
  (scaled q/ellipse 0 0 (/ 1 3) (/ 1 3))
  (scaled q/ellipse 0 0 (/ 2 3) (/ 2 3))

  (q/stroke-weight 1)
  (q/push-matrix)
  (dotimes [i 100]
    (scaled q/line (if (= 0 (mod i 10)) (/ 9 30) (/ 3))
            0 (/ 11 30) 0)
    (rotate-fraction (/ 100)))
  (q/pop-matrix))


(defn clock-hands [frac]
  (q/stroke foreground-color)
  (q/stroke-weight 2)
  (q/push-matrix)
  (rotate-fraction (- (* 100 frac) (Math/floor (* 100 frac))))
  (scaled q/line 0 0 (/ 11 30) 0)
  (q/pop-matrix)

  (q/push-matrix)
  (q/stroke-weight 3)
  (rotate-fraction frac)
  (scaled q/line 0 0 (/ 11 30) 0)
  (q/pop-matrix))


(defn setup []
  (q/frame-rate 30)
  {:fraction 0.0})

(defn update [state]
  {:fraction (-> (q/hour)
                 (* 60)
                 (+ (q/minute))
                 (* 60)
                 (+ (q/seconds))
                 (/ 86400))})

(defn draw [state]
  ;; this would be a good place to use prior state to clean up intelligently
  (q/stroke background-color)
  (q/fill background-color)
  (scaled q/rect 0 0 1 1)
  (q/translate (scale-x (/ 2)) (scale-y (/ 2)))
  (q/rotate (- q/HALF-PI))
  (clock-base)
  (clock-hands (:fraction state)))
