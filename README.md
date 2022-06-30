# dynaload

[![Clojars Project](https://img.shields.io/clojars/v/borkdude/dynaload.svg)](https://clojars.org/borkdude/dynaload)

Dynaload functionality inspired by
[clojure.spec.alpha](https://github.com/clojure/spec.alpha).

This library works in:

- Clojure JVM
- Clojure JVM + GraalVM `native-image`
- ClojureScript
- [Babashka](https://babashka.org/)

## Why

It's often useful to make libraries light-weight and transitive dependencies
optional. For example,
[clojure.spec.alpha](https://github.com/clojure/spec.alpha) makes
[test.check](https://github.com/clojure/test.check) optional in both the Clojure
and ClojureScript implementations. This project extracts that logic and packages
it as a library.

## Usage

This library exposes one namespaces: `borkdude.dynaload` with one macro:
`dynaload`. The macro returns a delay that will either contain a value or will
throw upon deref (unless provided a `:default` in a map in the second
argument). It lets you dynamically refer to a var that may or may not be
there. In Clojure it will require the namespace for you and throw if the
namespace is not there. In ClojureScript you will have to require the namespace
manually before deref, since ClojureScript namespaces cannot be loaded
dynamically (outside of a REPL).

### GraalVM

When using this library with GraalVM `native-image` it is recommended to set the
Java property `borkdude.dynaload.aot` to `true` both during Clojure compilation
and GraalVM `native-image` compilation. This will avoid using `require` at
runtime, which has a beneficial effect on binary size and compile time memory
usage.  An example can be found in [graal-test](graal-test) which shows these
differences in binary size:

| With `sci.core` | `borkdude.dynaload.aot=true` | binary size in MB |
| ---             | ---                          |               --- |
| yes             | no                           |                33 |
| no              | no                           |                25 |
| yes             | yes                          |                17 |
| no              | yes                          |                 8 |

Because setting `borkdude.dynaload.aot` to `true` will avoid runtime require,
you will have to require the dynaloaded namespaces before namespaces where the vars are
dynaloaded.

### Options

In addition to a fully qualified symbol, `dynaload` accepts an option map with
currently one options: `:default`, a value that is returned if the var cannot be
found. If no default is provided, `dynaload` will throw instead.

## Example

Consider this example from `examples/sci.cljc`

``` clojure
(ns example.sci
  (:require
   [borkdude.dynaload :refer [dynaload]]))

(def eval-string (dynaload 'sci.core/eval-string))

(println (eval-string "(+ 1 2 3)"))
```

First we run this without having the [sci](https://github.com/borkdude/sci)
library on the classpath.

On the JVM:

``` clojure
$ clojure example/sci.cljc
Syntax error (FileNotFoundException) compiling at (example/sci.cljc:8:1).
Var sci.core/eval-string does not exist, sci.core never required
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

Note: in ClojureScript we had to require sci manually, whereas in Clojure it was required for us.

## Test

``` shell
$ script/test
```

## License

Copyright © 2020 - 2022 Michiel Borkent

Distributed under the EPL License. See LICENSE.

This project is based on code from:
- [clojure.spec.alpha](https://github.com/clojure/spec.alpha), which is licensed under the same EPL License.
- [clojurescript](https://github.com/clojure/spec.alpha), which is licensed under the same EPL License.
