(ns borkdude.dynaload-test
  (:require
   #?(:clj  [borkdude.dynaload-clj  :refer        [dynaload]]
      :cljs [borkdude.dynaload-cljs :refer-macros [dynaload]])
   #?(:cljs [borkdude.lib]) ;; CLJS requires users to require the lib manually
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

