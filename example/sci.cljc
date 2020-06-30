(ns example.sci
  (:require
   #?(:clj  [borkdude.dynaload-clj  :refer        [dynaload]]
      :cljs [borkdude.dynaload-cljs :refer-macros [dynaload]])))

(def eval-string (dynaload 'sci.core/eval-string))

(println (@eval-string "(+ 1 2 3)"))

