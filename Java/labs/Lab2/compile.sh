#!/bin/bash

shopt -s globstar

javac -cp lib/Pokemon.jar -d out src/**/*.java

jar cfm lab2.jar MANIFEST.MF -C out/ .    