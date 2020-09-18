(defproject borkdude/dynaload "0.2.2"
  :description "The dynaload logic from clojure.spec.alpha as a library."
  :url "https://github.com/borkdude/dynaload"
  :scm {:name "git"
        :url "https://github.com/borkdude/dynaload"}
  :license {:name "Eclipse Public License 1.0"
            :url "http://opensource.org/licenses/eclipse-1.0.php"}
  :source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass
                                    :sign-releases false}]])
