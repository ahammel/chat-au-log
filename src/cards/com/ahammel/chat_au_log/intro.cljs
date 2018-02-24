(ns chat-au-log.intro
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [chat-au-log.ui.components :as comp]))

(defcard SVGPlaceholder
  "# SVG Placeholder"
  (comp/ui-placeholder {:w 200 :h 200}))
