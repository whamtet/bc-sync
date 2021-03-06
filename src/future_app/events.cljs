(ns future-app.events
  (:require
   [re-frame.core :refer [reg-event-db after]]
   [clojure.spec.alpha :as s]
   [future-app.db :as db :refer [app-db]]
   [future-app.http :as http]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 (fn [_ _]
   app-db))

(reg-event-db
  :set-username
  (fn [db [_ value]]
    (assoc db :username value)))

(reg-event-db
  :set-password
  (fn [db [_ value]]
    (assoc db :password value)))

(reg-event-db
 :set-greeting
 (fn [db [_ value]]
   (assoc db :greeting value)))

(reg-event-db
 :update-calendar
 (fn [db [_ username password]]
   (http/update-classes username password)
   (assoc db :loading? true)))

(reg-event-db
  :finish-loading
  (fn [db _]
    (dissoc db :loading?)))
