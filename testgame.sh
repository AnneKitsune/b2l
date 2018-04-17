#!/bin/bash
gnome-terminal -x sh -c 'java -jar ./dist/MyGame.jar; exec bash'
gnome-terminal -x sh -c 'java -jar ./dist/MyGame.jar; exec bash'
java -jar ./dist/MyGame.jar -server
