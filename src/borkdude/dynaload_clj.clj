(ns borkdude.dynaload-clj
  {:no-doc true})

(defonce ^:private dynalock (Object.))

(defmacro ^:private locking2
  "Executes exprs in an implicit do, while holding the monitor of x.
  Will release the monitor of x in all circumstances."
  {:added "1.0"}
  [x & body]
  `(let [lockee# ~x]
     (try
       (let [locklocal# lockee#]
         (monitor-enter locklocal#)
         (try
           ~@body
           (finally
             (monitor-exit locklocal#)))))))

(defn dynaload
  ([s] (dynaload s {}))
  ([s opts]
   (delay
     (let [ns (namespace s)]
       (assert ns)
       (try (locking2 dynalock
                      (require (symbol ns)))
            (catch Exception _ nil))
       (let [v (resolve s)]
         (if v
           @v
           (if-let [e (find opts :default)]
             (val e)
             (throw (RuntimeException. (str "Var " s " is not on the classpath"))))))))))
