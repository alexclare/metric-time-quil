(ns metric-time-quil.dynamic
  (:require [quil.core :as q]))

(def background-color 255)
(def foreground-color 0)

(defn rotate-fraction [phi] (q/rotate (* phi q/TWO-PI)))
(defn scale-x [x] (* x (q/width)))
(defn scale-y [y] (* y (q/height)))

(defn clock-base []
  (q/no-fill)
  (q/stroke foreground-color)

  (q/stroke-weight 2)
  (q/ellipse 0 0 (scale-x (/ 1 3)) (scale-y (/ 1 3)))
  (q/ellipse 0 0 (scale-x (/ 2 3)) (scale-y (/ 2 3)))

  (q/stroke-weight 1)
  (q/push-matrix)
  (dotimes [i 100]
    (q/line (scale-x
             (if (= 0 (mod i 10)) (/ 9 30) (/ 3))) 0
             (scale-x (/ 11 30)) 0)
    (rotate-fraction (/ 100)))
  (q/pop-matrix))


(defn clock-hands [frac]
  (q/stroke foreground-color)
  (q/stroke-weight 2)
  (q/push-matrix)
  (rotate-fraction (- (* 100 frac) (Math/floor (* 100 frac))))
  (q/line 0 0 (scale-x (/ 11 30)) 0)
  (q/pop-matrix)

  (q/push-matrix)
  (q/stroke-weight 3)
  (rotate-fraction frac)
  (q/line 0 0 (scale-x (/ 11 30)) 0)
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
  (q/rect 0 0 (scale-x 1) (scale-y 1))
  (q/translate (scale-x (/ 2)) (scale-y (/ 2)))
  (q/rotate (- q/HALF-PI))
  (clock-base)
  (clock-hands (:fraction state)))
