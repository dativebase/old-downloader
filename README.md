# old-downloader

A simple tool for downloading all of the data from an OLD (Online Linguistic
Database) instance.


## Installation

Clone the source:

    $ git clone https://github.com/dativebase/old-downloader.git

Using Leiningen, create the standalone .jar file:

    $ lein uberjar

Note: you must have Leiningen and Clojure installed.


## Usage

To download an OLD's data, supply the URL, username and password for the OLD
instance whose data you want to download:

    $ java -jar target/uberjar/old-downloader-0.1.0-SNAPSHOT-standalone.jar \
      https://some.domain.com/path/to/old/instance/ \
      someusername \
      somepassword

Alternatively, use `lein run`:

    $ lein run \
      https://some.domain.com/path/to/old/instance/ \
      someusername \
      somepassword

If successful, your OLD data should be present under a local `olds/` directory,
within a subdirectory named after the URL you supplied and the current
timestamp. Successful output will look something like this:

    Attempting to download the data from the OLD at 'https://some.domain.com/path/to/old/instance/' using username 'someusername' and password 'somepassword'.
     - 1834 forms downloaded and stored at olds/https-some-domain-com-path-to-old-instance-1553205324/forms.json.
    Success!

**Note: file data are not currently downloaded by this tool.**


## License

Copyright © 2019 Joel Dunham

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
