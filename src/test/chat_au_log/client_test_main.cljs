(ns chat-au-log.client-test-main
  (:require chat-au-log.tests-to-run
            [fulcro-spec.selectors :as sel]
            [fulcro-spec.suite :as suite]))

(enable-console-print!)

(suite/def-test-suite client-tests {:ns-regex #"chat-au-log..*-spec"}
  {:default   #{::sel/none :focused}
   :available #{:focused}})

