#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

POM=$SCRIPT_DIR/../pom.xml

hash xmllint 2>/dev/null || { echo >&2 "Command not found: xmllint.  Aborting."; exit 1; }

xmllint --xpath "/*[namespace-uri()='http://maven.apache.org/POM/4.0.0' and local-name()='project']/*[namespace-uri()='http://maven.apache.org/POM/4.0.0' and local-name()='version']/text()" $POM
