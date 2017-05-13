
(ns client.comp.group
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.alias :refer [create-comp div span input]]
            [respo.comp.debug :refer [comp-debug]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-code comp-text]]
            [client.comp.task-draft :refer [comp-task-draft]]
            [client.comp.task :refer [comp-task]]))

(def style-icon {:cursor :pointer})

(defn on-toggle-hidden [group-id]
  (fn [e dispatch!] (dispatch! :session/toggle-hidden group-id)))

(def style-draft {})

(defn on-edit-group [group-id]
  (fn [e dispatch!] (dispatch! :router/change {:name :group-editor, :params group-id})))

(def style-list {})

(def style-container {:width "100%"})

(def style-empty
  {:font-size 20, :font-weight 100, :font-family "Josefin Sans", :color colors/texture-light})

(def style-sidebar {:width 320})

(def style-name {:font-size 24, :font-weight 100, :font-family "Josefin Sans"})

(defn on-group-manage [group-id]
  (fn [e dispatch!] (dispatch! :router/change {:name :group-manager, :params group-id})))

(defn render [task-group show-done?]
  (fn [state mutate!]
    (div
     {:style (merge ui/row style-container)}
     (div
      {:style (merge ui/flex style-list)}
      (let [tasks (:tasks task-group), done-tasks (:done-tasks task-group)]
        (div
         {}
         (div {:style style-draft} (comp-task-draft (:id task-group)))
         (comp-space nil 32)
         (if (empty? tasks)
           (div {:style style-empty} (comp-text "No tasks" nil))
           (div
            {}
            (->> (vals tasks)
                 (sort (fn [a b] (compare (:updated-time b) (:updated-time a))))
                 (map (fn [task] [(:id task) (comp-task task)])))))
         (comp-space nil 32)
         (div
          {}
          (comp-text "Done tasks:" nil)
          (comp-space 8 nil)
          (div
           {:style ui/clickable-text, :event {:click (on-toggle-hidden (:id task-group))}}
           (comp-text "Toggle" nil)))
         (if (empty? done-tasks)
           (div {:style style-empty} (comp-text (if show-done? "No tasks" "Hidden") nil))
           (div
            {}
            (->> (vals done-tasks)
                 (sort (fn [a b] (compare (:updated-time b) (:updated-time a))))
                 (map (fn [task] [(:id task) (comp-task task)]))))))))
     (div
      {:style style-sidebar}
      (div
       {:style style-name}
       (comp-text (:name task-group) nil)
       (comp-space 8 nil)
       (span
        {:attrs {:class-name "icon ion-md-create"},
         :style style-icon,
         :event {:click (on-edit-group (:id task-group))}})
       (comp-space 8 nil)
       (span
        {:attrs {:class-name "icon ion-md-people"},
         :style style-icon,
         :event {:click (on-group-manage (:id task-group))}}))))))

(def comp-group (create-comp :group render))