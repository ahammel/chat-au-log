(ns com.ahammel.chat-au-log.client
  (:require [fulcro.client :as fc]))

(defonce app (atom (fc/new-fulcro-client)))
