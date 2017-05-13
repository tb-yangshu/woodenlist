
(ns server.updater.core
  (:require [server.updater.session :as session]
            [server.updater.user :as user]
            [server.updater.router :as router]
            [server.updater.task-group :as task-group]
            [server.updater.task :as task]
            [server.updater.message :as message]))

(defn updater [db op op-data session-id op-id op-time]
  (case op
    :session/connect (session/connect db op-data session-id op-id op-time)
    :session/disconnect (session/disconnect db op-data session-id op-id op-time)
    :user/log-in (user/log-in db op-data session-id op-id op-time)
    :user/sign-up (user/sign-up db op-data session-id op-id op-time)
    :user/log-out (user/log-out db op-data session-id op-id op-time)
    :user/avatar (user/set-avatar db op-data session-id op-id op-time)
    :session/remove-notification
      (session/remove-notification db op-data session-id op-id op-time)
    :router/change (router/change db op-data session-id op-id op-time)
    :task-group/create (task-group/create db op-data session-id op-id op-time)
    :task-group/rename (task-group/rename db op-data session-id op-id op-time)
    :task-group/delete (task-group/delete db op-data session-id op-id op-time)
    :task-group/add-member (task-group/add-member db op-data session-id op-id op-time)
    :task-group/delete-member (task-group/delete-member db op-data session-id op-id op-time)
    :task-group/change-role (task-group/change-role db op-data session-id op-id op-time)
    :session/toggle-hidden (session/toggle-hidden db op-data session-id op-id op-time)
    :task/create (task/create db op-data session-id op-id op-time)
    :task/toggle (task/toggle db op-data session-id op-id op-time)
    :task/edit (task/edit db op-data session-id op-id op-time)
    :task/delete (task/delete db op-data session-id op-id op-time)
    :task/touch (task/touch db op-data session-id op-id op-time)
    :message/create (message/create db op-data session-id op-id op-time)
    db))