(ns chat-au-log.client-main
  (:require [chat-au-log.client :as client]
            [fulcro.client :as core]
            [chat-au-log.ui.root :as root]))

; This is the production entry point. In dev mode, we do not require this file at all, and instead mount (and
; hot code reload refresh) from cljs/user.cljs
(reset! client/app (core/mount @client/app root/Root "app"))
