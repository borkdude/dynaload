# dynaload

Dynaload functionality blatantly stolen from
[clojure.spec.alpha](https://github.com/clojure/spec.alpha).

## Why

It's often useful to make libraries light-weight and transitive dependencies
optional. For example,
[clojure.spec.alpha](https://github.com/clojure/spec.alpha) makes
[test.check](https://github.com/clojure/test.check) optional in both the Clojure
and ClojureScript implementations. This project extracts that logic and packages
it as a library.

## Usage

This library exposes two namespaces, one for Clojure (`borkdude.dynaload-clj`)
and one for ClojureScript (`borkdude.dynaload-cljs`). Each namespace exposes one
public var, `dynaload`. In Clojure `dynaload` is a regular function, in
ClojureScript it's macro. Both versions return a delay that will either contain
a value or will throw upon deref. It lets you dynamically refer to a var that
may or may not be there. In Clojure it will require the namespace for you and
throw if the namespace is not there. In ClojureScript you will have to require
the namespace manually before deref, since ClojureScript namespaces cannot be
loaded dynamically (outside of a REPL).

Consider this example from `examples/sci.cljc`

``` clojure
(ns example.sci
  (:require
   #?(:clj  [borkdude.dynaload-clj  :refer        [dynaload]]
      :cljs [borkdude.dynaload-cljs :refer-macros [dynaload]])))

(def eval-string (dynaload 'sci.core/eval-string))

(println (@eval-string "(+ 1 2 3)"))
```

First we run this without having the [sci](https://github.com/borkdude/sci)
library on the classpath.

On the JVM:

``` clojure
$ clojure example/sci.cljc
Syntax error (FileNotFoundException) compiling at (example/sci.cljc:8:1).
Could not locate sci/core__init.class, sci/core.clj or sci/core.cljc on classpath.

Full report at:
/var/folders/2m/h3cvrr1x4296p315vbk7m32c0000gp/T/clojure-3838719710831994824.edn
```

ClojureScript:

``` clojure
$ plk example/sci.cljc
Execution error (Error) at (<cljs repl>:1).
Var sci.core/eval-string does not exist, sci.core never required
```

And now we load it with sci on the classpath.

JVM:

``` clojure
$ clj -Sdeps '{:deps {borkdude/sci {:mvn/version "0.1.0"}}}' example/sci.cljc
6
```

ClojureScript:

``` clojure
$ plk -Sdeps '{:deps {borkdude/sci {:mvn/version "0.1.0"}}}' -e "(require '[sci.core])" example/sci.cljc
6
```

Again, note: in ClojureScript we had to require sci manually, whereas in Clojure it was required for us.

In addition to a fully qualified symbol, `dynaload` accepts an option map with
currently one options: `:default`, a value that is returned if the var cannot be
found. If no default is provided, `dynaload` will throw instead.

## Test

``` shell
$ script/test
```

## License

Copyright © 2020 Michiel Borkent

Distributed under the EPL License. See LICENSE.

This project is based on code from:
- [clojure.spec.alpha](https://github.com/clojure/spec.alpha), which is licensed under the same EPL License.
- [clojurescript](https://github.com/clojure/spec.alpha), which is licensed under the same EPL License.
