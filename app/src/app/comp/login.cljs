
(ns app.comp.login
  (:require-macros [respo.macros :refer [defcomp div input button <> span]])
  (:require [respo.core :refer [create-comp]]
            [respo.comp.space :refer [=<]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.style :as ui]
            [app.schema :as schema]))

(defn on-toggle [state] (fn [e dispatch! mutate!] (mutate! (update state :signup? not))))

(defn on-submit [username password signup?]
  (fn [e dispatch!]
    (do
     (dispatch! (if signup? :user/sign-up :user/log-in) [username password])
     (.setItem js/localStorage (:storage-key schema/configs) (pr-str [username password])))))

(defn on-input [state k] (fn [e dispatch! mutate!] (mutate! (assoc state k (:value e)))))

(def style-title {:font-size 24, :font-weight 300, :font-family "Josefin Sans"})

(def initial-state {:signup? false, :username "", :password ""})

(defcomp
 comp-login
 (states)
 (let [state (or (:data states) initial-state)]
   (div
    {:style (merge ui/flex ui/column)}
    (div
     {}
     (<> span (if (:signup? state) "Sign up" "Log in") style-title)
     (if (:signup? state)
       (div
        {}
        (<> span "Want to log in?" nil)
        (=< 8 nil)
        (div
         {:style ui/clickable-text, :event {:click (on-toggle state)}}
         (<> span "Log in" nil)))
       (div
        {}
        (<> span "No account yet?" nil)
        (=< 8 nil)
        (div
         {:style ui/clickable-text, :event {:click (on-toggle state)}}
         (<> span "Sign up" nil)))))
    (div
     {:style {}}
     (div
      {}
      (input
       {:style ui/input,
        :attrs {:placeholder "Username", :value (:username state)},
        :event {:input (on-input state :username)}}))
     (=< nil 8)
     (div
      {}
      (input
       {:style ui/input,
        :attrs {:placeholder "Password", :value (:password state)},
        :event {:input (on-input state :password)}})))
    (=< nil 8)
    (div
     {:style ui/flex}
     (button
      {:style (merge ui/button {:outline :none, :border :none}),
       :event {:click (on-submit (:username state) (:password state) (:signup? state))}}
      (<> span "Submit" nil)))
    (comment comp-inspect state nil))))
