(ns babashka.impl.clojure.java.javadoc
  {:no-doc true}
  (:require [babashka.impl.clojure.java.browse :refer [browse-url]]
            [clojure.java.javadoc :as javadoc]
            [sci.core :as sci])
  (:import [java.io File]))

(def javadoc-ns (sci/create-ns 'clojure.java.javadoc))

(def feeling-lucky (sci/new-dynamic-var '*feeling-lucky* true {:ns javadoc-ns}))
(def feeling-lucky-url (sci/new-dynamic-var '*feeling-lucky-url* 
                         "http://www.google.com/search?btnI=I%27m%20Feeling%20Lucky&q=allinurl:" {:ns javadoc-ns}))

(alter-meta! #'javadoc/fill-in-module-name dissoc :private)
(defn javadoc-url
  "Searches for a URL for the given class name.  Tries
  *local-javadocs* first, then *remote-javadocs*.  Returns a string."
  {:tag String,
   :added "1.2"}
  [^String classname]
  (let [file-path (.replace classname \. File/separatorChar)
        url-path (.replace classname \. \/)]
    (if-let [file ^File (first
                          (filter #(.exists ^File %)
                            (map #(File. (str %) (str file-path ".html"))
                              @javadoc/*local-javadocs*)))]
      (-> file .toURI str)
      ;; If no local file, try remote URLs:
      (or (some (fn [[prefix url]]
                  (when (.startsWith classname prefix)
                    (str (javadoc/fill-in-module-name url classname)
                      url-path ".html")))
            @javadoc/*remote-javadocs*)
        ;; if *feeling-lucky* try a web search
        (when @feeling-lucky (str @feeling-lucky-url url-path ".html"))))))

; almost a direct copy - refer to javadoc ns for javadoc-url
(defn javadoc
  "Opens a browser window displaying the javadoc for the argument.
  Tries *local-javadocs* first, then *remote-javadocs*."
  [class-or-object]
  (let [^Class c (if (instance? Class class-or-object)
                   class-or-object
                   (class class-or-object))]
    (if-let [url (javadoc-url (.getName c))]
      (browse-url url)
      (println "Could not find Javadoc for" c))))

(def javadoc-namespace 
  {'*core-java-api* (sci/copy-var javadoc/*core-java-api* javadoc-ns)
   '*local-javadocs* (sci/copy-var javadoc/*local-javadocs* javadoc-ns)
   '*remote-javadocs* (sci/copy-var javadoc/*remote-javadocs* javadoc-ns)
   '*feeling-lucky-url* feeling-lucky-url
   '*feeling-lucky* feeling-lucky
   'add-local-javadoc (sci/copy-var javadoc/add-local-javadoc javadoc-ns)
   'add-remote-javadoc (sci/copy-var javadoc/add-remote-javadoc javadoc-ns)
   'javadoc (sci/copy-var javadoc javadoc-ns)})
