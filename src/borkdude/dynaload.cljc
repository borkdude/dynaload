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

(defmacro dynaload
  ([s] `(dynaload ~s {}))
  ([[_quote s] opts]
   `(#?(:clj borkdude.dynaload.LazyVar.
        :cljs borkdude.dynaload/LazyVar.)
     (fn []
       #?(:clj
          (if-let [v# (resolve '~s)]
            v#
            (if-let [e# (find ~opts :default)]
              (val e#)
              (throw
               (ex-info
                (str "Var " '~s " does not exist, "
                     (namespace '~s) " never required")
                {}))))
          :cljs
          (if (cljs.core/exists? '~s)
            ~(vary-meta s assoc :cljs.analyzer/no-resolve true)
            (if-let [e# (find ~opts :default)]
              (val e#)
              (throw
               (js/Error.
                (str "Var " '~s " does not exist, "
                     (namespace '~s) " never required")))))))
     nil)))
