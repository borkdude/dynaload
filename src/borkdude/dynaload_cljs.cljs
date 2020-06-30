(ns borkdude.dynaload-cljs
  {:no-doc true})

(deftype LazyVar [f ^:mutable cached]
  IDeref
  (-deref [this]
    (if-not (nil? cached)
      cached
      (let [x (f)]
        (when-not (nil? x)
          (set! cached x))
        x))))
