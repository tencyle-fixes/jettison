
# Jettison Backports Repository by Tencyle

## Note: This is *not* the official Jettison repository

This repository contains security backports to various CVE's found in Jettison.

Please note, that while the backports are *based* on the official fixes, they were created by Tencyle and not by the maintainers of Jettison.

### backports

Backport branchs stem from a tagged version, and are named "backport-TAG"

for example: the backport branch for tag **jettison-1.1** is **backport-jetisson-1.1** 

### The original jettison readme:


Jettison is a Java library for converting XML to JSON and vice-versa with the help of StAX (https://en.wikipedia.org/wiki/StAX).
It implements XMLStreamWriter and XMLStreamReader and supports Mapped and BadgerFish conventions. Latest release is 1.4.0.

For example, with a Mapped convention, JAXB processes JAXB beans and emits XMLStreamWriter events which are processed by Jettison
with the XML data being converted to JSON. Likewise, when it reads JSON, it reports XMLStreamReader events for JAXB to populate JAXB
beans.

Note improving and supporting the Mapped convention code is the main focus of this project. However the pull requests from 
BadgerFish convention users are welcomed. 

Jettison was originally created by Dan Diephouse and hosted at Codehause.


