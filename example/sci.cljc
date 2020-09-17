(ns example.sci
  (:require
   [borkdude.dynaload :refer [dynaload]]))

(def eval-string (dynaload 'sci.core/eval-string))

(println (@eval-string "(+ 1 2 3)"))
