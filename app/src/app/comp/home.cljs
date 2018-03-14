
(ns app.comp.home
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo-ui.colors :as colors]
            [respo.macros
             :refer
             [defcomp <> div span button input mutation-> action-> list->]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.comp.space :refer [=<]]
            [respo.util.list :refer [map-val]]
            [clojure.string :as string]
            [respo-ui.comp.icon :refer [comp-icon]]))

(defcomp
 comp-task
 (task)
 (div
  {:style (merge ui/row {:line-height "32px", :margin "8px 0px", :width 400})}
  (div
   {:style (merge ui/center {:cursor :pointer}),
    :on-click (action->
               :task/move-task
               {:id (:id task), :from :working-tasks, :to :done-tasks})}
   (comp-icon :android-done))
  (=< 16 nil)
  (input
   {:value (:text task),
    :style (merge ui/input {:width 320}),
    :on-input (fn [e d! m!] (println "on change:" (:value e)))})
  (=< 16 nil)
  (div
   {:style (merge ui/center {:cursor :pointer}),
    :on-click (action->
               :task/move-task
               {:id (:id task), :from :working-tasks, :to :pending-tasks})}
   (comp-icon :ios-time-outline))))

(defcomp
 comp-home
 (states tasks)
 (let [state (or (:data states) {:draft ""})]
   (div
    {:style (merge ui/flex {:padding 16})}
    (div
     {}
     (input
      {:style (merge ui/input {:width 320}),
       :placeholder "new task here...",
       :value (:draft state),
       :on-input (mutation-> (assoc state :draft (:value %e)))})
     (=< 8 nil)
     (button
      {:style ui/button,
       :inner-text "Add",
       :on-click (fn [e d! m!]
         (let [draft (:draft state)]
           (if (not (string/blank? draft))
             (do (d! :task/create draft) (m! (assoc state :draft ""))))))}))
    (list-> {} (->> tasks (map-val (fn [task] (comp-task task))))))))
