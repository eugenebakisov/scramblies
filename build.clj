(ns build
  (:require [clojure.string :as string]
            [clojure.tools.build.api :as b]
            [clojure.java.shell :refer [sh]]))

(defn execute-or-throw
  "Takes a string and executes it via sh. In case of error - throws an exception. Returns the output of the executed command"
  ([command]
   (execute-or-throw nil command))
  ([description command]
   (println (or description (str "Executing [" command "]...")))
   (let [{:keys [exit out] :as s} (apply sh (string/split command #" "))]
     (when-not (zero? exit)
       (throw (ex-info (str "failed to execute: " command) s)))
     (string/trim out))))

(defn build-cljs
  "Builds cljs and tailwindcss and copies result to the target/classes to be part of uberjar"
  []
  (execute-or-throw "npm run release")
  (execute-or-throw "Copying cljs..."
                    "cp -r target/classes/cljsbuild/public target/classes/")
  (execute-or-throw "Copying css..."
                    "cp -r target/classes/tailwindcss/public target/classes/"))

(def version (or (try
                   (execute-or-throw "git describe --tags --always")
                   (catch Exception _ignored (fn [_])))
                 "0.0.1"))
(def basis (b/create-basis {:project "deps.edn"}))

(defn clean
  "Delete the build target directory"
  []
  (println "Cleaning target...")
  (b/delete {:path "target"}))

(defn prep []
  (println "Writing Pom...")
  (b/write-pom {:class-dir "target/classes"
                :lib 'eugene.bakisov/scramblies
                :version (format "0.0.1-SNAPSHOT")
                :basis basis
                :src-dirs ["src/clj"]})
  (b/copy-dir {:src-dirs ["src/clj" "resources" "env/prod/clj"]
               :target-dir "target/classes"}))

(defn uber []
  (println "Compiling Clojure...")
  (b/compile-clj {:basis basis
                  :src-dirs ["src/clj" "env/prod/clj"]
                  :class-dir "target/classes"})
  (build-cljs)
  (println "Making uberjar...")
  (b/uber {:class-dir "target/classes"
           :uber-file "target/scramblies-standalone.jar"
           :main "eugene.bakisov.scramblies.core"
           :basis basis}))

(defn all [_]
  (clean)
  (prep)
  (uber))

(defn- build-docker-image
  [tag]
  (execute-or-throw (str "docker build -t scramblies:" tag " .")))

(defn- push-to-docker-hub
  [tag]
  (execute-or-throw (str "docker tag localhost/scramblies:" tag " eugenebakisov/scramblies:" tag))
  (execute-or-throw (str "docker push eugenebakisov/scramblies:" tag)))

(defn docker [_]
  (build-docker-image version))

(defn docker-push [_]
  (push-to-docker-hub version))
