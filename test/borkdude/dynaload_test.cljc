(ns borkdude.dynaload-test
  (:require
   #?(:clj  [borkdude.dynaload :refer [dynaload]]
      :cljs [borkdude.dynaload :refer-macros [dynaload]])
   [borkdude.lib] ;; borkdude.lib must be required, else dynaload will throw
   [clojure.test :as t :refer [deftest is testing]]))

(deftest dynaload-test
  (let [f (dynaload 'borkdude.lib/foo)]
    (is (= 1 (f))))
  (is (thrown-with-msg?
       #?(:clj Exception :cljs js/Error)
       #"borkdude.bar" ((dynaload 'borkdude.bar/foo))))
  (let [f (dynaload 'borkdude.bar/foo {:default (fn [] 1)})]
    (is (= 1 (f))))
  (let [f (dynaload 'borkdude.bar/foo {:default (fn [_])})]
    (is (nil? (f))))
  (testing "nothing happens when you don't use it"
    (is (dynaload 'non-existing/foo))))
