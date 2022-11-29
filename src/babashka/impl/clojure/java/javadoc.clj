;;  Modified / stripped version of clojure.main for use with babashka on
;;  GraalVM.
;;
;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
(ns
  ^{:author "Christophe Grand, Stuart Sierra",
    :doc    "A repl helper to quickly open javadocs."}
  babashka.impl.clojure.java.javadoc
  (:require 
            [clojure.java.javadoc :as javadoc]
            [sci.core :as sci]))

(def javadoc-ns (sci/create-ns 'clojure.java.javadoc))

(def javadoc-namespace 
  {'*core-java-api* (sci/copy-var javadoc/*core-java-api* javadoc-ns)
   '*local-javadocs* (sci/copy-var javadoc/*local-javadocs* javadoc-ns)
   '*remote-javadocs* (sci/copy-var javadoc/*remote-javadocs* javadoc-ns)
   '*feeling-lucky-url* (sci/copy-var javadoc/*feeling-lucky-url* javadoc-ns)
   '*feeling-lucky* (sci/copy-var javadoc/*feeling-lucky* javadoc-ns)
   'add-local-javadoc (sci/copy-var javadoc/add-local-javadoc javadoc-ns)
   'add-remote-javadoc (sci/copy-var javadoc/add-remote-javadoc javadoc-ns)
   'javadoc (sci/copy-var javadoc/javadoc javadoc-ns)})
