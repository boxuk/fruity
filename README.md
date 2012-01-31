# Fruity - The Box UK Package Manager

Fruity is a tool for monitoring repositories which contain libraries that can be
built as packages.  When tags are made in these repositories Fruity handles
building the packages and adding them to whatever distribution method that library
may be using.  At the moment Fruity supports PEAR packages for PHP, and CLojars libraries
for Clojure projects.

## Usage

When you have everything set up running Fruity is as simple as editing the config
file and then calling the binary.

```bash
java -jar fruity.jar /path/to/config.clj
```

It's suggested to run this via a CI server (eg. Jenkins), or maybe just cron.
Fruity is essentially just a wrapper for a bunch of other tools like...

* PEAR
* SVN
* Pirum
* Leiningen
* Git

## Building Fruity

To build the Fruity JAR file just use [Leiningen](https://github.com/technomancy/leiningen).

```bash
lein uberjar
```

## Fruity Config File

Fruity uses a single configuration file, and provides a sample one you can copy (config.clj-sample).
It's reccomended you take a copy of this file and put it in version control somewhere so
it's safe.

## PEAR Libraries

For PEAR libraries Fruity requires that you keep your entire PEAR channel in a
Subversion repository. PEAR libraries have the type *:pear*.  So first create a
repository on your network to store this in.  Then you'll need to set up a channel
using Pirum.

<pre>
http://pirum.sensiolabs.org/
</pre>

When your channel is created you can add the info to your config file as explained in
the sample file.  Your library config will then look something like this...

```clojure
{ :name "lib-name"
  :type :pear
  :scm :git
  :url "http://github.com/me/repo" }
```

You can also optionally specify a *:packageCommand* that will be used to build the PEAR
package.  If you don't specify this it defaults to _pear package_.

## Clojars Projects

Adding a Clojure library for distribution on Clojars.org is easy.  The library config
will be something like this:

```clojure
{ :name "some-name"
  :type :clojars
  :scm :svn
  :url "http://svn.mydomain.com/myproject" }
```

## SVN/Git

Fruity supports storing projects in either Subversion of Git.  For SVN projects specify
the path to the root below the usual */trunk* */tags* and */branches*.  And for Git
projects just give the path to the repository.

### Git Tag Names

For Git projects tag names are assumed to be in the format *vX.X.X*.  ie. a *v* as a
prefix and then the version number.

## License

Dual licensed under GPLv2 and MIT.

