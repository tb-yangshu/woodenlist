
(ns server.twig.container
  (:require [recollect.bunch :refer [create-twig]]
            [server.twig.user :refer [twig-user]]
            [server.twig.group :refer [twig-group]]
            [server.twig.group-brief :refer [twig-group-brief]]
            [server.twig.core :refer [twig-message]]))

(def twig-container
  (create-twig
   :container
   (fn [db session]
     (let [user-id (:user-id session), router (:router session)]
       (if (some? user-id)
         (let [user (get-in db [:users user-id])]
           {:session session,
            :logged-in? true,
            :user (twig-user user),
            :router (assoc
                     router
                     :data
                     (case (:name router)
                       :portal
                         (let [group-ids (:involved-groups user)]
                           (->> group-ids
                                (map
                                 (fn [group-id]
                                   [group-id
                                    (select-keys
                                     (get-in db [:task-groups group-id])
                                     [:id :name])]))
                                (into {})))
                       :group
                         (twig-group
                          (get-in db [:task-groups (:params router)])
                          (:show-done? session))
                       :group-editor (get-in db [:task-groups (:params router)])
                       :group-manager
                         (let [task-group (get-in db [:task-groups (:params router)])
                               read-user (fn [user-ids]
                                           (->> user-ids
                                                (map
                                                 (fn [user-id]
                                                   [user-id
                                                    (twig-user (get-in db [:users user-id]))]))
                                                (into {})))]
                           (-> task-group
                               (update :admins read-user)
                               (update :users read-user)))
                       :task-editor
                         (let [params (:params router)]
                           (get-in
                            db
                            [:task-groups (:group-id params) :tasks (:task-id params)]))
                       :person
                         (let [member (get-in db [:users (:params router)])]
                           {:user (twig-user member),
                            :groups (->> (:involved-groups member)
                                         (map
                                          (fn [group-id]
                                            [group-id
                                             (twig-group-brief
                                              (get-in db [:task-groups group-id]))]))
                                         (into {}))})
                       :groups
                         (->> (vals (:task-groups db))
                              (map
                               (fn [task-group]
                                 [(:id task-group) (twig-group-brief task-group)]))
                              (into {}))
                       :people
                         (->> (vals (:users db))
                              (map (fn [member] [(:id member) (twig-user member)]))
                              (into {}))
                       :messages
                         (->> (vals (:messages db))
                              (map
                               (fn [message]
                                 [(:id message)
                                  (twig-message message (get-in db [:users user-id]))]))
                              (into {}))
                       nil)),
            :statistics {:sessions-count (count (:sessions db)),
                         :users-count (count (:users db))}})
         {:session session, :logged-in? false})))))