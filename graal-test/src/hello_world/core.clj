(ns hello-world.core
  (:gen-class)
  (:require [borkdude.dynaload :as dyn]
            [sci.core]))

;; without sci.core and with and borkdude.dynaload.aot=true: 8MB
;; with sci.core and borkdude.dynaload.aot=true: 17MB
;; with sci.core and without borkdude.dynaload.aot=true: 33MB
;; without sci.core and without borkdude.dynaload.aot=true: 25MB

(def eval-string (dyn/dynaload 'sci.core/eval-string))

(defn -main [& _args]
  (prn (eval-string "(+ 1 2 3)")))
