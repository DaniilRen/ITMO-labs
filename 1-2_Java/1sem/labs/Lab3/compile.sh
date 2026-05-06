#!/bin/bash

shopt -s globstar

javac -d out src/**/*.java

jar cfm lab3.jar MANIFEST.MF -C out/ .    