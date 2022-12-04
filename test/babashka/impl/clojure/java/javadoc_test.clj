(ns babashka.impl.clojure.java.javadoc-test
  (:require [babashka.impl.clojure.java.javadoc :refer :all]
            [babashka.test-utils :as tu]
            [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]))

(deftest no-javadoc-found
  (is (= "Could not find Javadoc for clojure.lang.IFn\n"
        (tu/bb nil
          "(require '[clojure.java.javadoc :as javadoc])
           (import [clojure.lang IFn])
           (binding [javadoc/*feeling-lucky* false]
             (javadoc/javadoc IFn))"))))

(deftest ^:skip-windows change-feeling-lucky-url
  (is (= "foo/clojure/lang/IFn.html\n"
        (tu/bb nil
          "(require '[clojure.java.browse :as browse]
                    '[clojure.java.javadoc :as javadoc])
           (import [clojure.lang IFn])
           (reset! browse/*open-url-script* \"echo\")
           (binding [javadoc/*feeling-lucky-url* \"foo/\"]
             (print (:out (javadoc/javadoc IFn))))"))))

(deftest local-javadoc-find
  (is (str/includes?
        (tu/bb nil
          "(require '[clojure.java.browse :as browse]
                    '[clojure.java.javadoc :as javadoc])
           (import [clojure.lang IFn])
           (reset! browse/*open-url-script* \"echo\")
           (javadoc/add-local-javadoc \"./test-resources/mock-javadoc\")
           (print (:out (javadoc/javadoc IFn)))")
        "./test-resources/mock-javadoc/clojure/lang/IFn.html\n")))
