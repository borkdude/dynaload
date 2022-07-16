(ns nbb-test-runner
  (:require
   [cljs.test :refer [run-tests]]
   [nbb.classpath :refer [add-classpath]]))

(add-classpath "src")
(add-classpath "test")
(require '[borkdude.dynaload-test])
(run-tests 'borkdude.dynaload-test)
