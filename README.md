
# Box UK Pear Package Manager

BPPM is a tool for monitoring repositories which contain libraries that can be
built as PEAR packages.  When tags are made in these repositories BPPM handles
building the packages and adding them to a PEAR channel.

## Usage

When you have everything set up Running BPPM is as simple as editing the config
file and then calling the binary.

<pre>
java -jar bppm-1.0.0.jar /path/to/config.clj
</pre>

It's suggested to run this via a CI server (eg. Jenkins) or maybe just cron.
BPPM is essentially just a wrapper for a bunch of other tools.

* PEAR
* SVN
* Pirum
* Git

## Setting It Up

BPPM requires that you keep your entire PEAR channel in a Subversion repository.
So first create a repository on your network to store this in.  Then you'll
need to set up a channel using Pirum.

<pre>
http://pirum.sensiolabs.org/
</pre>

When your channel is created you can create your config file.  You can copy
the config.clj-sample file provided with BPPM, rename it to whatever you like
and go put it in version control somewhere.

When you've edited your config file you can then run BPPM as indicated above.

## License

Dual licensed under GPL and MIT.

