(ns borkdude.dynaload
  #?(:cljs (:require-macros [borkdude.dynaload :refer [dynaload if-bb]])))

(defmacro if-bb
  [then else]
  (if #?(:clj (System/getProperty "babashka.version")
         :cljs false)
    then
    else))

(if-bb
    #?(:clj
       (defn ->LazyVar [f _]
         (let [cached (volatile! nil)]
           (reify
             clojure.lang.IDeref
             (deref [_this]
               (if-not (nil? @cached)
                 cached
                 (let [x (f)]
                   (when-not (nil? x)
                     (vreset! cached x))
                   x)))
             clojure.lang.IFn
             (invoke [this]
               (@this))
             (invoke [this a]
               (@this a))
             (invoke [this a b]
               (@this a b))
             (invoke [this a b c]
               (@this a b c))
             (invoke [this a b c d]
               (@this a b c d))
             (invoke [this a b c d e]
               (@this a b c d e))
             (invoke [this a b c d e f]
               (@this a b c d e f))
             (invoke [this a b c d e f g]
               (@this a b c d e f g))
             (invoke [this a b c d e f g h]
               (@this a b c d e f g h))
             (invoke [this a b c d e f g h i]
               (@this a b c d e f g h i))
             (invoke [this a b c d e f g h i j]
               (@this a b c d e f g h i j))
             (invoke [this a b c d e f g h i j k]
               (@this a b c d e f g h i j k))
             (invoke [this a b c d e f g h i j k l]
               (@this a b c d e f g h i j k l))
             (invoke [this a b c d e f g h i j k l m]
               (@this a b c d e f g h i j k l m))
             (invoke [this a b c d e f g h i j k l m n]
               (@this a b c d e f g h i j k l m n))
             (invoke [this a b c d e f g h i j k l m n o]
               (@this a b c d e f g h i j k l m n o))
             (invoke [this a b c d e f g h i j k l m n o p]
               (@this a b c d e f g h i j k l m n o p))
             (invoke [this a b c d e f g h i j k l m n o p q]
               (@this a b c d e f g h i j k l m n o p q))
             (invoke [this a b c d e f g h i j k l m n o p q r]
               (@this a b c d e f g h i j k l m n o p q r))
             (invoke [this a b c d e f g h i j k l m n o p q r s]
               (@this a b c d e f g h i j k l m n o p q r s))
             ;; for some reason not working yet in bb
             #_(invoke [this a b c d e f g h i j k l m n o p q r s t]
                 (@this a b c d e f g h i j k l m n o p q r s t))
             #_(invoke [this a b c d e f g h i j k l m n o p q r s t rest]
                 (apply @this a b c d e f g h i j k l m n o p q r s t rest))
             (applyTo [this args]
               (apply @this args)))))
       :cljs nil)
  #?(:org.babashka/nbb nil
     :default
     (deftype LazyVar #?(:clj [f ^:volatile-mutable cached] :cljs [f ^:mutable cached])
       #?(:clj clojure.lang.IDeref :cljs IDeref)
       (#?(:clj deref :cljs -deref) [_this]
         (if-not (nil? cached)
           cached
           (let [x (f)]
             (when-not (nil? x)
               (set! cached x))
             x)))
       #?(:clj clojure.lang.IFn :cljs IFn)
       (#?(:clj invoke :cljs -invoke) [this]
         (@this))
       (#?(:clj invoke :cljs -invoke) [this a]
         (@this a))
       (#?(:clj invoke :cljs -invoke) [this a b]
         (@this a b))
       (#?(:clj invoke :cljs -invoke) [this a b c]
         (@this a b c))
       (#?(:clj invoke :cljs -invoke) [this a b c d]
         (@this a b c d))
       (#?(:clj invoke :cljs -invoke) [this a b c d e]
         (@this a b c d e))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f]
         (@this a b c d e f))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g]
         (@this a b c d e f g))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h]
         (@this a b c d e f g h))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i]
         (@this a b c d e f g h i))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j]
         (@this a b c d e f g h i j))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k]
         (@this a b c d e f g h i j k))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l]
         (@this a b c d e f g h i j k l))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m]
         (@this a b c d e f g h i j k l m))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n]
         (@this a b c d e f g h i j k l m n))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o]
         (@this a b c d e f g h i j k l m n o))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p]
         (@this a b c d e f g h i j k l m n o p))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p q]
         (@this a b c d e f g h i j k l m n o p q))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p q r]
         (@this a b c d e f g h i j k l m n o p q r))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p q r s]
         (@this a b c d e f g h i j k l m n o p q r s))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p q r s t]
         (@this a b c d e f g h i j k l m n o p q r s t))
       (#?(:clj invoke :cljs -invoke) [this a b c d e f g h i j k l m n o p q r s t rest]
         (apply @this a b c d e f g h i j k l m n o p q r s t rest))
       #?(:clj
          (applyTo [this args]
                   (apply @this args))))))

(defmacro ? [& {:keys [cljs clj]}]
  (if (contains? &env '&env)
    `(if (:ns ~'&env) ~cljs ~clj)
    (if #?(:clj (:ns &env) :cljs true)
      cljs
      clj)))

#?(:clj
   (def resolve-at-compile-time? (= "true"
                                    (System/getProperty "borkdude.dynaload.aot"))))

#?(:clj (defonce ^:private dynalock (Object.)))

#?(:clj
   (defmacro ^:private locking2
     "Executes exprs in an implicit do, while holding the monitor of x.
  Will release the monitor of x in all circumstances."
     {:added "1.0"}
     [x & body]
     #?(:bb
        `(locking ~x ~body)
        :default
        `(let [lockee# ~x]
           (try
             (let [locklocal# lockee#]
               (monitor-enter locklocal#)
               (try
                 ~@body
                 (finally
                   (monitor-exit locklocal#)))))))))

#?(:clj (def resolve*
          (if resolve-at-compile-time?
            (constantly nil)
            (fn [sym]
              (let [ns (namespace sym)]
                (assert ns)
                (try (locking2 dynalock
                               (require (symbol ns)))
                     (catch Exception _ nil))
                (resolve sym))))))

(defmacro dynaload
  ([s] `(dynaload ~s {}))
  ([[_quote s] opts]
   #?(:org.babashka/nbb
      `(let [d# (delay (or (resolve '~s)
                           (if-let [e# (find ~opts :default)]
                             (val e#)
                             (throw
                              (ex-info
                               (str "Var " '~s " does not exist, "
                                    (namespace '~s) " never required")
                               {})))))]
         (fn
           ([]
            (@d#))
           ([a0]
            (@d# a0))
           ([a0 a1]
            (@d# a0 a1))
           ([a0 a1 a2]
            (@d# a0 a1 a2))
           ([a0 a1 a2 a3]
            (@d# a0 a1 a2 a3))
           ([a0 a1 a2 a3 a4]
            (@d# a0 a1 a2 a3 a4))
           ([a0 a1 a2 a3 a4 & args]
            (apply @d# a0 a1 a2 a3 a4 args))))
      :default
      #_{:clj-kondo/ignore[:redundant-let]}
      (let [#?@(:clj [resolved-at-compile-time (when resolve-at-compile-time?
                                                 (resolve s))])]
        `(->LazyVar
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
          nil)))))
