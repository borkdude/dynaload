(ns borkdude.dynaload-cljs)

(defmacro dynaload
  ([s] `(dynaload ~s {}))
  ([[_quote s] opts]
   `(borkdude.dynaload-cljs/LazyVar.
     (fn []
       (if (cljs.core/exists? ~s)
         ~(vary-meta s assoc :cljs.analyzer/no-resolve true)
         (if-let [e# (find ~opts :default)]
           (val e#)
           (throw
            (js/Error.
             (str "Var " '~s " does not exist, "
                  (namespace '~s) " never required"))))))
     nil)))
