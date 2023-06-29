(ns build
  (:require [clojure.tools.build.api :as b]))

(def basis
  (b/create-basis {:project "deps.edn"}))

(def classes-dir
  "classes")

(def jar-file
  "seesaw.jar")

(defn clean
  [& _]
  (b/delete {:path "classes"}))

(defn compile-java
  [& _]
  (clean)
  (b/javac {
            :class-dir     classes-dir
            :basis         basis
            :src-dirs      ["jvm"]
            :javac-options ["--release" "8"]}))

(defn compile-clojure
  [& _]
  (compile-java)
  (b/copy-dir {:src-dirs   ["src"]
               :target-dir classes-dir})
  (b/compile-clj {:basis     basis
                  :class-dir classes-dir
                  :src-dirs  ["src"]}))

(defn jar
  [& _]
  (compile-clojure)
  (b/jar {:class-dir classes-dir
          :jar-file  jar-file}))
