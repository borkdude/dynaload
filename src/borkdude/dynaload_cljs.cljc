(ns borkdude.dynaload-cljs)

(defmacro dynaload [[_quote s]]
  `(borkdude.dynaload-cljs/LazyVar.
    (fn []
      (if (cljs.core/exists? ~s)
        ~(vary-meta s assoc :cljs.analyzer/no-resolve true)
        (throw
         (js/Error.
          (str "Var " '~s " does not exist, "
               (namespace '~s) " never required")))))
    nil))
