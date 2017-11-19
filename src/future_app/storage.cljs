(ns future-app.storage)

(def storage (.-AsyncStorage (js/require "react-native")))

(defn set-item [k v]
  (.setItem storage k v))
(defn get-item [k]
  (.getItem storage k))
