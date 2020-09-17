(ns hello-world.core
  (:gen-class)
  (:require [borkdude.dynaload :as dyn]))

(def eval-string* (dyn/dynaload 'sci.core/eval-string {:default (fn [_] :dude)}))

(defn eval-string [s]
  (@eval-string* s))

(defn -main [& _args]
  (prn (eval-string "(+ 1 2 3)")))
