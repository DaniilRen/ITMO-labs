#!/bin/bash

javac -d classes/ src/Lab1.java && jar -cvfm lab1.jar MANIFEST.mf -C classes/ .