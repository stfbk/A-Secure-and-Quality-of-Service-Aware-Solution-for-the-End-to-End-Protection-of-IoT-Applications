#!/bin/bash

# Number of publishers, subscribers, topics, minutes
PUBLISHERS=$1
SUBSCRIBERS=$2
TOPICS=$3
MINUTES=$4
BYTES=$5


# Derive relevant values

# The sum of the number of publishers wih the number of subscribers
USERS=$(($PUBLISHERS+$SUBSCRIBERS))

# Docker compose output YAML file
COMPOSEFILE="compose_"$USERS"users.yml"

# Base name of the workflows JSON files
WORKFLOWBASEFILE="workflow_"$TOPICS"topics"$BYTES"bytes"

# AC policy state output JSON file
STATEFILE="state_"$USERS"users_"$TOPICS"topics.json"

# Clients (locust workers) launcher file
CLIENTSLAUNCHERFILE="launcherClients_"$PUBLISHERS"publishers"$SUBSCRIBERS"subscribers.sh"

# Master (locust master) launcher file
MASTERLAUNCHERFILE="launcherMaster_"$PUBLISHERS"publishers"$SUBSCRIBERS"subscribers"$MINUTES"minutes.sh"

# Initializer launcher
INITIALIZERLAUNCHERFILE="launcherInitializer_"$PUBLISHERS"publishers"$SUBSCRIBERS"subscribers"$TOPICS"topics.sh"

# Starter script CryptoAC
STARTERFILE="starterScriptCryptoAC_"$USERS"users.sh"



# S_1. Generate the docker compose file
cd compose/ && chmod +x ./dockerComposeGenerator.sh && ./dockerComposeGenerator.sh $USERS "$COMPOSEFILE" && cd ..

# S_2. Generate the $TOPICS*2 workflow files
cd workflows/ && chmod +x ./workflowsGenerator.sh && ./workflowsGenerator.sh $TOPICS "$WORKFLOWBASEFILE" $BYTES && cd ..

# S_3. Generate the state file
cd states/ && chmod +x ./stateGenerator.sh && ./stateGenerator.sh $USERS $TOPICS "$STATEFILE" && cd ..

# S_4. Generate the client laucnher file
cd launchers/ && chmod +x ./launchersGenerator.sh && ./launchersGenerator.sh $PUBLISHERS $SUBSCRIBERS $TOPICS "$MASTERLAUNCHERFILE" "$CLIENTSLAUNCHERFILE" "$STATEFILE" "$INITIALIZERLAUNCHERFILE" "$WORKFLOWBASEFILE" $MINUTES && cd ..

# S_5. Generate the started script for CryptoAC
cd scripts/ && chmod +x ./starterScriptGenerator.sh && ./starterScriptGenerator.sh "$STARTERFILE" "$COMPOSEFILE" && cd ..
