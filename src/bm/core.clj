(ns bm.core
  (:import (java.io InputStream InputStreamReader BufferedReader)
           (java.net URL HttpURLConnection))
  (:gen-class))

(defmacro benchmark [body]
  `(let [now# ~(System/nanoTime)]
     (println "Benchmarking...")
     ~body
     (println "That took: " (/ (- ~(System/nanoTime) now#) 1E6) "ns")))


(defn fetch [url]
  "synchronously fetches a url, returns same url if successful
  to preserve agent state"
    (let [conn (.openConnection (URL. url))]
      (.setRequestMethod conn "GET")
      (.connect conn)
      (with-open [stream (BufferedReader.
                          (InputStreamReader. (.getInputStream conn)))]
        (.toString (reduce #(.append %1 %2)
                           (StringBuffer.) (line-seq stream)))))
    url)

(defn spawn-agents [url c n]
  "url being the url to hit, c being the number of concurrent connections,
  n being the number of requests per connection"
  (let [agents (take c (cycle [(agent url)]))]
    (doseq [a agents]
      (dotimes [x n] (send-off a fetch)))
    (apply await agents)))

(defn -main [& args]
  (println "TODO"))
