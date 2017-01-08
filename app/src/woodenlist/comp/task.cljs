
(ns woodenlist.comp.task
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.alias :refer [create-comp div span input]]
            [respo.comp.debug :refer [comp-debug]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-code comp-text]]))

(def style-container {:align-items :center, :color colors/texture, :margin-top 8})

(def style-done
  {:background-color colors/attractive, :width 40, :cursor :pointer, :height 40})

(defn on-toggle [group-id task-id done?]
  (fn [e dispatch!]
    (dispatch! :task/toggle {:task-id task-id, :group-id group-id, :done? done?})))

(def style-text
  {:line-height "40px",
   :min-width 400,
   :background-color colors/paper,
   :cursor :pointer,
   :padding "0 8px",
   :height 40})

(defn on-edit [group-id task-id]
  (fn [e dispatch!]
    (dispatch!
     :router/change
     {:name :task-editor, :params {:task-id task-id, :group-id group-id}})))

(defn render [task]
  (fn [state mutate!]
    (div
     {:style (merge ui/row style-container)}
     (div
      {:style (merge style-done (if (:done? task) {:background-color colors/verdant})),
       :event {:click (on-toggle (:group-id task) (:id task) (:done? task))}})
     (comp-space 8 nil)
     (div
      {:style style-text, :event {:click (on-edit (:group-id task) (:id task))}}
      (comp-text (:text task) nil)))))

(def comp-task (create-comp :task render))
