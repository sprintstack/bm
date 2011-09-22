(ns bm.core
  (:use [clojure.contrib.http.agent])
  (:gen-class))

(defn benchmark [body]
  `(let [now# ~(System/nanoTime)]
     (println "Benchmarking...")
     ~body
     (println "That took: " (- (System/nanoTime now)))))

(defn -main [& args]
  (println "TODO"))