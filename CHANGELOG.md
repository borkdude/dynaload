# CHANGELOG

## v0.2.2

- Implement `IFn` on `LazyVar`.

## v0.2.1

Same as v0.2.0 but fixes support for Clojure 1.9.0.

## v0.2.0

Enhancements:

- Better support for GraalVM `native-image` through Java property `borkdude.dynaload.aot`.

Breaking changes:

- There is now only one single namespace `borkdude.dynaload`, no separate ones
  for the JVM and CLJS.

## v0.1.0

Initial release.
