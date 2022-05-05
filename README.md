
# Table of Contents

1.  [Scramblies](#org69d3ed4)
    1.  [Intro](#orgf22df52)
    2.  [Stack](#org0c1fa5f)
    3.  [Local Development](#orge50e221)
        1.  [Docker](#orgc08ef75)
        2.  [Building local version](#org03a8a03)
        3.  [REPL-driven local development via CIDER](#orgf2a3a1d)
        4.  [REPL-driven local development via other nrepl client](#org3ae05dd)


<a id="org69d3ed4"></a>

# Scramblies


<a id="orgf22df52"></a>

## Intro

Clojure web server bundled with ClojureScript web applicaiton.
Web server contains a single API endpoint which takes two strings and returns if one string can be arranged out of letters of the second one.
Web application contains a single form for inputing mentioned strings.
At the moment of writing it&rsquo;s been deployed to [scramblies.repl-driven.dev](https://scramblies.repl-driven.dev)


<a id="org0c1fa5f"></a>

## Stack

-   Clojure 1.11.1
-   Managing deps via [tools.deps](https://github.com/clojure/tools.deps.alpha)
-   ClojureSript toolchain: [shadow-cljs](https://github.com/thheller/shadow-cljs)
-   [Undertow](https://undertow.io/) web server
-   [Integrant](https://github.com/weavejester/integrant)+[Aero](https://github.com/juxt/aero) for system configuration/management
-   [Reitit](https://github.com/metosin/reitit) for routes and spec
-   [Reagent](https://github.com/reagent-project/reagent) for creating React components (would like to try [Helix](https://github.com/lilactown/helix) or [UIX](https://github.com/pitch-io/uix))
-   [Re-frame](https://github.com/day8/re-frame) for managing state and controlling effects in web app
-   [Tailwindcss](https://github.com/tailwindlabs/tailwindcss) for efficient styling (tried to make [macrocss](https://github.com/HealthSamurai/macrocss) work but there is an issue with @media queries)


<a id="orge50e221"></a>

## Local Development


<a id="orgc08ef75"></a>

### Docker

In order to quickly lauch project locally one may use docker

    docker run -p 3000:3000 eugenebakisov/scramblies


<a id="org03a8a03"></a>

### Building local version

Local development requires Node.js/npm and JDK/Clojure CLI to be installed.

In order to build local version you should install js deps and starting tailwindcss and shadow-cljs in watch mode

    npm install
    npm run dev

Then start Clojure web server

    make repl

If everything went successful you should be able to open [localhost:3000](http://localhost:3000) and see web application with some css. You can also check out [localhost:3000/api](http://localhost:300/api) for Swagger UI.
Shadow-cljs configured to start web server in watch mode on [localhost:8020](http://localhost:8020) - which can be useful for working with styles in cljs views as they will be compiled on the fly by tailwindcss and delived by shadow-cljs dev server automaticaly instead of requiring you to manually reload index.html served by clojure web server.


<a id="orgf2a3a1d"></a>

### REPL-driven local development via CIDER

1.  Start tailwindcss in watch mode
    
        npm run postcss:watch
2.  `cider-jack-in-clj` (select clojure-cli)
3.  `cider-jack-in-cljs` (select shadow-cljs)
4.  open either [localhost:3000](http://localhost:3000) or [localhost:8020](http://localhost:8020)


<a id="org3ae05dd"></a>

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

