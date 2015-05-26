(ns metric-time-quil.dynamic
  (:require [quil.core :as q])
  (:import [org.joda.time Instant]
           [org.joda.time.chrono ISOChronology]))


;; ## default colors

(def background-color 255)
(def foreground-color 0)


;; ## scaling/rotation convenience functions

(defn rotate-fraction [phi] (q/rotate (* phi q/TWO-PI)))
(defn scale-x [x] (* x (q/width)))
(defn scale-y [y] (* y (q/height)))
(defn scaled [fun xa ya xb yb]
  (fun (scale-x xa) (scale-y ya) (scale-x xb) (scale-y yb)))


;; ## convenience macros

(defmacro operation [& body]
  `(do
     (q/push-matrix)
     ~@body
     (q/pop-matrix)))


;; ## drawing logic

(defn clock-base
  "Clock static elements that don't change frame by frame." []
  (q/no-fill)
  (q/stroke foreground-color)

  (q/stroke-weight 2)
  (scaled q/ellipse 0 0 (/ 1 3) (/ 1 3))
  (scaled q/ellipse 0 0 (/ 2 3) (/ 2 3))

  (q/stroke-weight 1)
  (operation
   (dotimes [i 100]
     (scaled q/line (if (= 0 (mod i 10)) (/ 9 30) (/ 3))
             0 (/ 11 30) 0)
     (rotate-fraction (/ 100))))

  (q/fill foreground-color)
  #_(scaled q/ellipse 0 0 (/ 80) (/ 80)))


(defn clock-hands [frac]
  (q/stroke foreground-color)

  (q/stroke-weight 1)
  (operation
   (rotate-fraction (- (* 10000 frac) (Math/floor (* 10000 frac))))
   (scaled q/line 0 0 (/ 11 30) 0))

  (comment
    (q/stroke-weight 1)
    (q/no-fill)
    (let [scaled-frac (- (* 1000 frac) (Math/floor (* 1000 frac)))
          actual (if (even? (int (Math/floor (* 1000 frac)))) scaled-frac (- 1 scaled-frac))]
      (scaled q/ellipse 0 0
              (+ (/ 3) (* actual (/ 3)))
              (+ (/ 3) (* actual (/ 3))))))

  (q/stroke-weight 2)
  (operation
   (rotate-fraction (- (* 100 frac) (Math/floor (* 100 frac))))
   (scaled q/line 0 0 (/ 11 30) 0))

  (operation
   (q/stroke-weight 3)
   (rotate-fraction frac)
   (scaled q/line 0 0 (/ 11 30) 0)))


(defn fraction-from-instant
  "Turn a joda.time Instant into the amount of the day that has elapsed."
  [instant]
  (/ (. instant get
        (. (ISOChronology/getInstance) millisOfDay)) 86400000))


;; ## quil foundation functions

(defn setup []
  (q/frame-rate 30)
  {:fraction 0.0})

(defn update [state]
  {:fraction (fraction-from-instant (Instant/now))})

(defn draw [state]
  ;; this would be a good place to use prior state to clean up intelligently
  (q/stroke background-color)
  (q/fill background-color)
  (scaled q/rect 0 0 1 1)
  (q/translate (scale-x (/ 2)) (scale-y (/ 2)))
  (q/rotate (- q/HALF-PI))
  (clock-base)
  (clock-hands (:fraction state)))
