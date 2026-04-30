ResourceBundle Editor
===========

Eclipse plugin for editing Java resource bundles. Lets you manage all localized .properties files in one screen. Some features: sorted keys, warning icons on missing keys/values, conversion to/from Unicode, hierarchical view of keys, and more.


<img src="http://essiembre.github.io/eclipse-rbe/img/screenshots/main-screen.png">

Go to ResourceBundle Editor web site for more screenshots and other information: http://essiembre.github.io/eclipse-rbe/


How to install
--------------

**Update Site:**

Create a new update site in Eclipse with the following:

* Site name:  ``Tlcsdm ResourceBundle Editor``
* Site URL:   ``https://raw.githubusercontent.com/tlcsdm/eclipse-rbe/update_site/``


Build
-----

This project uses [Tycho](https://github.com/eclipse-tycho/tycho) with [Maven](https://maven.apache.org/) to build. It requires Maven 3.9.0 or higher version.

Dev build:

```
mvn clean verify
```

Release build:

```
mvn clean org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.0.0 verify
```

