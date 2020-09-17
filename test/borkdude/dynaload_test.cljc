(ns borkdude.dynaload-test
  (:require
   #?(:clj  [borkdude.dynaload :refer [dynaload]]
      :cljs [borkdude.dynaload :refer-macros [dynaload]])
   [borkdude.lib] ;; borkdude.lib must be required, else dynaload will throw
   [clojure.test :as t :refer [deftest is]]))

(deftest dynaload-test
  (let [f @(dynaload 'borkdude.lib/foo)]
    (is (= 1 (f))))
  (is (thrown-with-msg?
       #?(:clj Exception :cljs js/Error)
       #"borkdude\.bar/foo" @(dynaload 'borkdude.bar/foo)))
  (let [f @(dynaload 'borkdude.bar/foo {:default (fn [] 1)})]
    (is (= 1 (f))))
  (let [f @(dynaload 'borkdude.bar/foo {:default nil})]
    (is (nil? f))))

