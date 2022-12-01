(ns babashka.impl.clojure.java.javadoc
  {:no-doc true}
  (:require [babashka.impl.clojure.java.browse :refer [browse-url]]
            [clojure.java.javadoc :as javadoc]
            [sci.core :as sci]))

(def javadoc-ns (sci/create-ns 'clojure.java.javadoc))

(alter-meta! #'javadoc/javadoc-url dissoc :private)

; almost a direct copy - refer to javadoc ns for javadoc-url
(defn javadoc
  "Opens a browser window displaying the javadoc for the argument.
  Tries *local-javadocs* first, then *remote-javadocs*."
  [class-or-object]
  (let [^Class c (if (instance? Class class-or-object)
                   class-or-object
                   (class class-or-object))]
    (if-let [url (javadoc/javadoc-url (.getName c))]
      (browse-url url)
      (println "Could not find Javadoc for" c))))

(def javadoc-namespace 
  {'*core-java-api* (sci/copy-var javadoc/*core-java-api* javadoc-ns)
   '*local-javadocs* (sci/copy-var javadoc/*local-javadocs* javadoc-ns)
   '*remote-javadocs* (sci/copy-var javadoc/*remote-javadocs* javadoc-ns)
   '*feeling-lucky-url* (sci/copy-var javadoc/*feeling-lucky-url* javadoc-ns)
   '*feeling-lucky* (sci/copy-var javadoc/*feeling-lucky* javadoc-ns)
   'add-local-javadoc (sci/copy-var javadoc/add-local-javadoc javadoc-ns)
   'add-remote-javadoc (sci/copy-var javadoc/add-remote-javadoc javadoc-ns)
   'javadoc (sci/copy-var javadoc javadoc-ns)})
