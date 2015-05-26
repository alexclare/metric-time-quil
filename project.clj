(defproject metric-time-quil "0.1.0-SNAPSHOT"
  :description "metric clock"
  :url "http://github.com/alexclare/metric-time-quil"
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [quil "2.2.4"]
                 [joda-time/joda-time "2.7"]]
  :plugins [[lein-marginalia "0.8.0"]]
  :main metric-time-quil.core
  :aot [metric-time-quil.core])
