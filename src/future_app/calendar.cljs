(ns future-app.calendar
  (:require-macros [future-app.macros :refer [promise->]]))

(def Cal (.-default (js/require "react-native-calendar-events")))

(defn save-lesson [{teacher-first-name "tne"
                    teacher-second-name "tsne"
                    lesson-name "lt"
                    lesson-contents "tn"
                    time "lt_time"}]
  (let [start-time (js/Date. (str (.replace time " " "T") ".000+07:00"))
        end-time (-> start-time .getTime (+ (* 90 60 1000)) js/Date.)]
    (.saveEvent Cal
                (str lesson-name " with " teacher-first-name)
                #js {:location "British Council"
                     :notes (str lesson-contents " with " teacher-first-name " " teacher-second-name)
                     :startDate start-time
                     :endDate end-time})))

(defn remove-event [event]
  (.removeEvent Cal (.-id event)))

(defn all-events []
  (.fetchAllEvents Cal "2017-01-01T00:00:00.000Z" "2020-01-01T00:00:00.000Z"))

(defn remove-classes []
  (promise-> (all-events)
             (doseq [event then :when (= "British Council" (.-location event))]
               (remove-event event))))

(defn update-classes [classes]
  (promise-> (remove-classes)
             (doseq [class classes] (save-lesson class))))
