(ns borkdude.dynaload.macros)

;; From macrovich
(defmacro deftime
  "This block will only be evaluated at the correct time for macro definition, at other times its content
   are removed.
   For Clojure it always behaves like a `do` block.
   For Clojurescript/JVM the block is only visible to Clojure.
   For self-hosted Clojurescript the block is only visible when defining macros in the pseudo-namespace."
  [& body]
  (when #?(:clj (not (:ns &env)) :cljs (re-matches #".*\$macros" (name (ns-name *ns*))))
    `(do ~@body)))
