(ns future-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
  :get-creds
  (fn [db _]
    [(:username db) (:password db)]))

(reg-sub
  :loading?
  (fn [db _]
    (:loading? db)))
