(ns future-app.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [future-app.events]
            [future-app.http :as http]
            [future-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/loading.gif"))

(defn app-root []
  (let [creds (subscribe [:get-creds])
        loading? (subscribe [:loading?])]
    (fn []
      (let [[username password] @creds]
        [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
         [text {:style {:font-size 20 :font-weight "100" :margin-bottom 20 :text-align "center"}}
          "British Council MyClass Calendar Sync"]
         [text {:style {:font-size 15 :font-weight "100" :margin-bottom 20 :text-align "center"}}
          "Enter Username and Password"]
         ;[image {:source logo-img :style {:width 80 :height 80 :margin-bottom 30}}]
         [text-input {:style {:height 40 :border-color "gray" :border-width 1 :width 300 :padding 10}
                      :on-change-text #(dispatch [:set-username %]) :default-value username
                      :keyboard-type "email-address"}]
         [text-input {:style {:height 40 :border-color "gray" :border-width 1 :width 300 :padding 10}
                      :on-change-text #(dispatch [:set-password %]) :default-value password
                      :secure-text-entry true}]
         (if @loading?
           [image {:source logo-img :style {:width 80 :height 80 :margin-bottom 30}}]
           [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5
                                         :margin-top 10}
                                 :on-press #(dispatch [:update-calendar username password])}
            [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "Go"]])
         ]))))


(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "FutureApp" #(r/reactify-component app-root)))
