(ns future-app.http
  (:require
    [future-app.calendar :as calendar])
  (:require-macros
    [future-app.macros :refer [promise->]]))

(defn update-classes [username password]
;  (prn "updating calendar" username password)
  (promise->
    (js/fetch (str "https://myclass.britishcouncil.org/index.php?id="
                   username "&pw=" password "&action=login")
              #js {:method "POST"
                   :credentials "include"})
    (js/fetch "https://myclass.britishcouncil.org/index.php?action=student_dashboard"
              #js {:method "GET"
                   :credentials "include"})
    (.text then)
    (let [[_ sid] (re-find #"sid = (\d+)" then)]
      (js/fetch (str "https://myclass.britishcouncil.org/index.php?action=getBookingByStudentId&sid=" sid)
                #js {:method "GET"
                     :credentials "include"}))
    (.json then)
    (let [edn (js->clj then)]
      (if (get edn "session_expired")
        (js/alert "Incorrect username or password.  Try again")
        (promise-> (calendar/update-classes edn) (js/alert "Updated"))))))
