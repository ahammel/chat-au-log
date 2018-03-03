(ns com.ahammel.chat-au-log.domain.core.alpha
  (:require [clojure.spec.alpha :as s]))

(defmulti descriptor
  "A descriptor associates a term with some semantics in the context of a
  controlled vocabulary.

  All descriptors must define an ::authority-id. The authority of a descriptor
  is the organization responsible for defining its semantics.

  ::preferred descriptors define the terms that may be used to categorize works
  using the vocabulary. They may be associated with broader and narrower terms,
  or related terms, and may list non-preferred alternatives.

  ::non-preferred descriptors define terms which are a synonym, near-synonym, or
  spelling variant of a ::preferred term. ::non-preferred descriptors contain a
  link to the ::preferred-term. ::non-preferred terms should not be used in
  cataloguing, but may show up in term searches as a way to guide the cataloguer
  to the preferred term."
  ::descriptor-type)

(def non-preferred-descriptor-spec
  (s/keys :req [::authority-id ::term ::preferred-term
                ::definition]))

(def preferred-descriptor-spec
  (s/keys :req [::authority-id ::term]
          :opt [::broader-term ::narrower-terms ::non-preferred-terms
                ::definition]))

(defmethod descriptor ::non-preferred [_] non-preferred-descriptor-spec)
(defmethod descriptor ::preferred [_] preferred-descriptor-spec)
(defmethod descriptor :default [_] preferred-descriptor-spec)

(s/def ::descriptor (s/multi-spec descriptor ::descriptor-type))

(s/def ::term
  ;; A word or brief phrase which describes the subject of a work.
  ;;
  ;; E.g., "Clojure", "Design automation", "Bridges, pedestrian", "History of
  ;; the European Union"
  (s/and string? #(<= 1 (count %) 1000)))

(s/def ::broader-term
  ;; A broader-term is a preferred term to which the narrower term is
  ;; subordinate in the hierarchy. A broader term is normally in a
  ;; "genus/species" or "part/whole" relationship with a narrower term.
  ;;
  ;; Note that terms should be grouped together using a broader/narrower
  ;; relationship only if they have a subtype relationship. "Lisp" is a good
  ;; broader term for "Clojure" (since Clojure is a kind of Lisp). On the other
  ;; hand, "chat-au-log" should not be in a broader/narrow relationship with
  ;; "Clojure" because it is not a kind of Clojure.
  ;;
  ;; For more general term relationships, see ::related-terms.
  ;;
  ;; A descriptor may have at most one broader term.
  ::term)

(s/def ::narrower-terms
  ;; The inverse of the ::broader-term relationship.
  (s/+ ::term))

(s/def ::preferred-term
  ;; Defines the relationship of a ::non-preferred term to the ::preferred
  ;; variant.
  ::term)

(s/def ::non-preferred-terms
  ;; The inverse of the ::preferred-term relationship.
  (s/+ ::term))

(s/def ::related-terms
  ;; Other preferred terms which are mentally associated with a preferred term,
  ;; but which are not in a broader/narrower relationship (including
  ;; transitively).
  ;;
  ;; Related terms have no particular defined semantics, and may be used
  ;; differently in different vocabularies.
  (s/+ ::term))

(s/def ::definition
  ;; The prose definition of a ::term, possibly including a dictionary
  ;; definition, examples of works which should and should not be categorized
  ;; under the term, how the term's semantics differ from previous versions of
  ;; the vocabulary, and other
  (s/and string? #(<= 1 (count %) 10000)))

(s/def ::authority
  ;; The organization which is responsible for the definition of a vocabulary or
  ;; term.
  ;;
  ;; This may be either the organization which provides a controlled vocabulary
  ;; to the public (such as IEEE or the Library of Congress), or a smaller
  ;; organization that constructs a controlled vocabulary for internal or
  ;; personal use.
  (s/keys :req [::authority-id ::authority-name]
          :opt [::authority-website]))

(s/def ::authority-id
  ;; A short, human-readable, unique identifier for an authority.
  ;;
  ;; I recommend that you use a domain that is controlled by the authority, in
  ;; the style of Clojure namespace identifiers.
  ;;
  ;; For example, the ::authority-id for the IEEE Thesaurus is given as
  ;; "com.ieee".
  ;;
  ;; The ::authority-id namespaces terms which may have different semantics in
  ;; the context of different vocabularies.
  (s/and string? #(<= 3 (count %) 100)))

(s/def ::authority-name
  ;; The human readable name of the authority.
  ;;
  ;; E.g. "IEEE", "Library of Congress", "Alex Hammel"
  (s/and string? #(<= 1 (count %) 1000)))

(s/def ::authority-website
  ;; The authority's official website (i.e., one that is hosted at a domain
  ;; owned by the authority).
  ;;
  ;; If the ::authority-id is a domain (as is recommended), it should be the
  ;; same domain that hosts the ::authority-website.
  uri?)

(s/def ::vocabulary
  ;; A controlled vocabulary which provides a list of terms which may be use to
  ;; categorize documents by subject.
  ;;
  ;; Note that the authority for a vocabulary need not be the authority for all
  ;; (or any) of its terms. A chat-au-log user may wish to appropriate all or
  ;; part of a publicly-available vocabulary for their own use (this is
  ;; highly encouraged). In this case, the original publisher of the term
  ;; maintains their authority over it.
  (s/keys :req [::term-map ::authority-id ::vocabulary-version]
          :opt [::vocabulary-name]))

(s/def ::vocabulary-name
  ;; A short, human-readable string which describes the vocabulary.
  ;;
  ;; E.g: "IEEE Thesaurus", "Library of Congress Subject Headings",
  ;; "Alex's Controlled Vocabulary"
  (s/and string? #(<= 1 (count %) 1000)))

(s/def ::vocabulary-version
  ;; The version of the vocabulary
  ;;
  ;; No particular semantics are defined, but I suggest using the date (and
  ;; time, if necessary) of publication.
  (s/and string? #(<= 1 (count %) 1000)))

(s/def ::term-map
  ;; A description of the terms of a vocabulary.
  ;;
  ;; The term map is described represented as a map associating terms to
  ;; descriptors to ensure that each term appears in the map exactly once.
  ;;
  ;; Each key in the map must be associated with a descriptor where the ::term
  ;; is set to that key.
  ;; TODO ^ encode this in the spec
  ;;
  ;; If a descriptor specifies ::preferred-term, ::broader-term,
  ;; ::narrower-term, or ::related-term relationships, those terms must be
  ;; present in the ::term-map.
  ;; TODO ^ encode this in the spec
  (s/map-of ::term ::descriptor))
