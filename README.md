# chat-au-log


```
                                              .
                                   .         ;
      .              .              ;%     ;;
        ,           ,                :;%  %;
         :         ;                   :;%;'     .,
,.        %;     %;            ;        %;'    ,;
  ;       ;%;  %%;        ,     %;    ;%;    ,%'
   %;       %;%;      ,  ;       %;  ;%;   ,%;'
    ;%;      %;        ;%;        % ;%;  ,%;'
     `%;.     ;%;     %;'         `;%%;.%;'
      `:;%.    ;%%. %@;        %; ;@%;%'
         `:%;.  :;bd%;          %;@%;'
           `@%:.  :;%.         ;@@%;'
             `@%.  `;@%.      ;@@%;
               `@%%. `@%%    ;@@%;
                 ;@%. :@%%  %@@%;
                   %@bd%%%bd%%:;
                     #@%%%%%:;;
                     %@@%%%::;
                     %@@@%(o);  . '
                     %@@@o%;:(.,'
                 `.. %@@@o%::;     _                ___       _.--.
                    `)@@@o%::;     \`.|\..----...-'`   `-._.-'_.-'`
                     %@@(o)::;     /  ' `         ,       __.--'
                    .%@@@@%::;     )/' _/     \   `-_,   /
                    ;%@@@@%::;.    `-'" `"\_  ,_.-;_.-\_ ',
                   ;%@@@@%%:;;;.       _.-'_./   {_.'   ; /
               ...;%@@@@@%%:;;;;,..   {_.-``-'         {_/
```

## What is chat-au-log?

It's a tool for creating [controlled vocabularies](https://en.wikipedia.org/wiki/Controlled_vocabulary),
and using them to catalogue works.

### Why?

My goal is to create a tool that technical teams can use to organize the random
assortment of documentation, FAQs, research papers, blog posts, functional
specifications, mock-ups, wire-frames, memoranda, and blurry cell phone
pictures of whiteboards that make up the institutional knowledge of every
company I've ever worked for. Making all this information accessible to the
folks who need it is a huge challenge.

An index using a controlled vocabulary offers a lot of promise over the
traditional wiki/Google Drive/Dropbox/Slack approaches:

* It brings a way to search for things that can't be easily indexed by text
  (like pictures and PDFs)
* It offers a way to build and document your organization's
  [Ubiquitous language](https://en.wikipedia.org/wiki/Domain-driven_design#Concepts).
* It offers the possibility of bringing together information across services
  (not everything has to live in the company wiki).

### Can I use it?

There's not much to use yet, but go for it. The library functions live under
the `com.ahammel.chat-au-log.domain` namespaces. Everything public in a
namespace that doesn't end in `alpha` will be [spec'd](https://clojure.org/guides/spec)
and [backwards compatible forever](https://github.com/matthiasn/talk-transcripts/blob/master/Hickey_Rich/Spec_ulation.md).

#### Alpha graduation criteria

I'll make a non-alpha release when:

- [ ] There are library functions to create, and update a controlled vocabulary.
- [ ] All of those are spec'd documented and tested.
- [ ] I've imported a sample controlled vocabulary.
- [ ] I think of a less stupid name.

## The Project

The main project source is in `src/main`.

```
├── config                    server configuration files. See Fulcro docs.
│   ├── defaults.edn
│   ├── dev.edn
│   └── prod.edn
├── chat_au_log
│   ├── api
│   │   ├── mutations.clj      server-side version of mutations
│   │   ├── mutations.cljs     client-side version of mutations
│   │   └── read.clj           server implementation of reads
│   ├── client.cljs            client creation (shared among dev/prod)
│   ├── client_main.cljs       production client main
│   ├── server.clj             server creation (shared among dev/prod)
│   ├── server_main.clj        production server main
│   └── ui
│       ├── components.cljc    a sample component
│       └── root.cljc          the root UI
└── translations
    └── es.cljc                Spanish translations of strings on client UI
```

## Development Mode

Special code for working in dev mode is in `src/dev`, which is not on
the build for production builds.

Running all client builds:

```
JVM_OPTS="-Ddev -Dtest -Dcards" lein run -m clojure.main script/figwheel.clj
dev:cljs.user=> (log-app-state) ; show current state of app
dev:cljs.user=> :cljs/quit      ; switch REPL to a different build
```

Or use a plain REPL in IntelliJ with JVM options of `-Ddev -Dtest -Dcards` and parameters of
`script/figwheel.clj`.

For a faster hot code reload experience, run only the build that matters to you at the time,

Running multiple builds in one figwheel can slow down hot code reload. You can also
run multiple separate figwheel instances to leverage more of your CPU cores, and
an additional system property can be used to allow this (by allocating different network ports
to figwheel instances):

```
# Assuming one per terminal window...each gets a REPL that expects STDIN/STDOUT.
JVM_OPTS="-Ddev -Dfigwheel.port=8081" lein run -m clojure.main script/figwheel.clj
JVM_OPTS="-Dtest -Dfigwheel.port=8082" lein run -m clojure.main script/figwheel.clj
JVM_OPTS="-Dcards -Dfigwheel.port=8083" lein run -m clojure.main script/figwheel.clj
```

Running the server:

Start a clj REPL in IntelliJ, or from the command line:

```
lein run -m clojure.main
user=> (go)
...
user=> (restart) ; stop, reload server code, and go again
user=> (tools-ns/refresh) ; retry code reload if hot server reload fails
```

The URLs are:

- Client (using server): [http://localhost:3000](http://localhost:3000)
- Cards: [http://localhost:3449/cards.html](http://localhost:3449/cards.html)
- Tests: See below.

## Fulcro Inspect

The Fulcro inspect to will preload. You can activate it by pressing CTRL-F while
in the application (dev mode only). If you need
a different keyboard shortcut (e.g. for Windows) see the docs on github.

## Tests

Tests are in `src/test`

```
src/test
└── chat_au_log
    ├── CI_runner.cljs            entry point for CI (doo) runner for client tests
    ├── client_test_main.cljs     entry point for dev-mode client tests
    ├── sample_spec.cljc          spec runnable by client and server. No "main" needed for server (clj) tests
    └── tests_to_run.cljs         shared requires of all specs (used by CI and dev mode)
```

### Server tests:

Interacting with tests resuts via a browser (also allows test focusing, etc):

From a CLJ REPL:

```
user=> (start-server-tests) ; start a server on port 8888 showing the server tests
```

then navigate to [http://localhost:8888/fulcro-spec-server-tests.html](http://localhost:8888/fulcro-spec-server-tests.html)

If you'd instead like to see them pop up over and over again in a terminal:

```
lein test-refresh
```

### Client Tests

Run the figwheel build and include the `-Dtest` JVM option. The URL to run/view the
tests will be
[http://localhost:3449/fulcro-spec-client-tests.html](http://localhost:3449/fulcro-spec-client-tests.html)

### CI Tests

Use the Makefile target `tests`:

```
make tests
```

You must have `npm` and Chrome installed. See the documentation on `doo` for more information on
using alternatives (phantom, firefox, etc).

## Dev Cards

The source is in `src/cards`.

```
JVM_OPTS="-Dcards" lein run -m clojure.main script/figwheel.clj
```

Or use a plain REPL in IntelliJ with JVM options of `-Dcards` and parameters of
`script/figwheel.clj`.

To add a new card namespace, remember to add a require for it to the `cards.cljs` file.

## I18N

The i18n process is codified into the Makefile as two targets. The first extracts strings from
the source (which must build the js, and run xgettext on it, which you must
have installed, perhaps from brew):

```
make i18n-extract
```

and gives you instructions on generating translations.

The second takes the translations and generates a cljs namespace for
them:

```
make i18n-generate
```

## Standalone Runnable Jar (Production, with advanced optimized client js)

```
lein uberjar
java -jar target/chat_au_log.jar
```
