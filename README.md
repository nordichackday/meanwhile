# meanwhile

Api that provides YLE archive contents as JSON file from the selected decade.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Endpoints

* All the material from a decade (use only full 10 years)
    http://localhost:3000/archive/1970

* All the material from a decade AND genre (use only full decades, provide
  genre with capitals)
    http://localhost:3000/archive/1980/Musiikki



