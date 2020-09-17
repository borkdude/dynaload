(ns borkdude.dynaload
  #?(:cljs (:require-macros [borkdude.dynaload :refer [dynaload]])))

(deftype LazyVar #?(:clj [f ^:volatile-mutable cached] :cljs [f ^:mutable cached])
  #?(:clj clojure.lang.IDeref :cljs IDeref)
  (#?(:clj deref :cljs -deref) [this]
    (if-not (nil? cached)
      cached
      (let [x (f)]
        (when-not (nil? x)
          (set! cached x))
        x))))

(defmacro ? [& {:keys [cljs clj]}]
  (if (contains? &env '&env)
    `(if (:ns ~'&env) ~cljs ~clj)
    (if #?(:clj (:ns &env) :cljs true)
      cljs
      clj)))

#?(:clj
   (def resolve-at-compile-time? (= "true"
                                    (System/getProperty "borkdude.dynaload.aot"))))

#?(:clj (def resolve* (if resolve-at-compile-time?
                        (constantly nil)
                        requiring-resolve)))

(defmacro dynaload
  ([s] `(dynaload ~s {}))
  ([[_quote s] opts]
   #_{:clj-kondo/ignore[:redundant-let]}
   (let [#?@(:clj [resolved-at-compile-time (when resolve-at-compile-time?
                                              (resolve s))])]
     `(#?(:clj borkdude.dynaload.LazyVar.
          :cljs borkdude.dynaload/LazyVar.)
       (fn []
         (? :clj
            (if-let [v# (or #?(:clj ~resolved-at-compile-time)
                            (resolve* '~s))]
              v#
              (if-let [e# (find ~opts :default)]
                (val e#)
                (throw
                 (ex-info
                  (str "Var " '~s " does not exist, "
                       (namespace '~s) " never required")
                  {}))))
            :cljs
            (if (cljs.core/exists? ~s)
              ~(vary-meta s assoc :cljs.analyzer/no-resolve true)
              (if-let [e# (find ~opts :default)]
                (val e#)
                (throw
                 (js/Error.
                  (str "Var " '~s " does not exist, "
                       (namespace '~s) " never required")))))))
       nil))))
