(ns metric-time-quil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [metric-time-quil.dynamic :as dyn]))

(q/defsketch metric-time-quil
  :title "metric time"
  :size [500 500]
  :setup dyn/setup
  :update dyn/update
  :draw dyn/draw
  :middleware [m/fun-mode])
