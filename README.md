

# Scramblies

1.  [Intro](#Intro)
2.  [Stack](#Stack)
3.  [Docker](#Docker)
4.  [Local Development](#Local)
    1.  [Building local version](#LocalBuild)
    2.  [REPL-driven local development via CIDER](#LocalCider)
    3.  [REPL-driven local development via other nrepl client](#LocalRepl)


<a id="Intro"></a>

## Intro

Clojure web server bundled with ClojureScript web app.
Web server contains a single API endpoint which takes two strings and returns true if one string can be arranged out of letters of the second one.
Web application contains a single form for inputting mentioned strings.
At the moment of writing it&rsquo;s been deployed to [scramblies.repl-driven.dev](https://scramblies.repl-driven.dev)


<a id="Stack"></a>

## Stack

-   Clojure 1.11.1
-   Managing deps via [tools.deps](https://github.com/clojure/tools.deps.alpha)
-   ClojureSript toolchain: [shadow-cljs](https://github.com/thheller/shadow-cljs)
-   [Undertow](https://undertow.io/) web server
-   [Integrant ](https://github.com/weavejester/integrant)+ [Aero](https://github.com/juxt/aero) for system configuration/management
-   [Reitit](https://github.com/metosin/reitit) for routes and spec
-   [Reagent](https://github.com/reagent-project/reagent) for creating React components (would like to try [Helix](https://github.com/lilactown/helix) or [UIX](https://github.com/pitch-io/uix))
-   [Re-frame](https://github.com/day8/re-frame) for managing state and controlling effects in web app
-   [Tailwindcss](https://github.com/tailwindlabs/tailwindcss) for efficient styling (tried to make [macrocss](https://github.com/HealthSamurai/macrocss) work but there is an issue with @media queries)


<a id="Docker"></a>

## Docker

To quickly launch project locally run

    docker run -P eugenebakisov/scramblies


<a id="Local"></a>

## Local Development


<a id="LocalBuild"></a>

### Building local version

Local development requires Node.js/npm and JDK/Clojure CLI to be installed.

To build a local version you should install js packages and start tailwindcss and shadow-cljs in watch mode

    npm install
    npm run dev

Then start Clojure web server

    make repl

If everything was successful you should be able to open [localhost:3000](http://localhost:3000) and see web app with some css. You may also check out Swagger UI at [localhost:3000/api](http://localhost:300/api).
Shadow-cljs is configured to start web server in watch mode on [localhost:8020](http://localhost:8020) - which can be useful for working with styles in cljs views as they will be compiled on the fly by tailwindcss and delivered by shadow-cljs dev server automatically instead of requiring you to manually reload index.html served by Clojure web server.


<a id="LocalCider"></a>

### REPL-driven local development via CIDER

1.  Start tailwindcss in watch mode
    
        npm run postcss:watch
2.  `cider-jack-in-clj` (select clojure-cli)
3.  `cider-jack-in-cljs` (select shadow-cljs)
4.  open either [localhost:3000](http://localhost:3000) or [localhost:8020](http://localhost:8020)


<a id="LocalRepl"></a>

### REPL-driven local development via other nrepl client

1.  Start tailwindcss in watch mode
    
        npm run postcss:watch
2.  Start shadow-cljs in watch mode
    
        npx shadow-cljs watch app
3.  Now you should be able to connect to shadow-cljs nrepl on port 7002 (or check [shadow-cljs.edn](https://github.com/eugenebakisov/scramblies/blob/master/shadow-cljs.edn) in case I forgot to update port in README)
4.  Start clj nrepl
    
        make repl
5.  Now you should be able to connect to clj nrepl on port specified in the command output (or in .nrepl-port file)
6.  Don&rsquo;t forget to open web app via either [localhost:3000](http://localhost:3000) or [localhost:8020](http://localhost:8020) in order for the cljs-repl to connect to a js runtime.

