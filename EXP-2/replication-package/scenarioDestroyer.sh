#!/bin/bash

# Number of publishers, subscribers, topics, messages
PUBLISHERS=$1
SUBSCRIBERS=$2
TOPICS=$3
MESSAGES=$4
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
MASTERLAUNCHERFILE="launcherMaster_"$PUBLISHERS"publishers"$SUBSCRIBERS"subscribers"$MESSAGES"messages.sh"

# Initializer launcher
INITIALIZERLAUNCHERFILE="launcherInitializer_"$PUBLISHERS"publishers"$SUBSCRIBERS"subscribers"$TOPICS"topics.sh"

# Starter script CryptoAC
STARTERFILE="starterScriptCryptoAC_"$USERS"users.sh"



# Delete files
rm -f "compose/"$COMPOSEFILE

rm -f "states/"$STATEFILE

rm -f "launchers/"$CLIENTSLAUNCHERFILE

rm -f "launchers/"$MASTERLAUNCHERFILE

rm -f "launchers/"$INITIALIZERLAUNCHERFILE

rm -f "scripts/"$STARTERFILE

for i in $(seq 1 $TOPICS); do 

  FILENAMEPUB=$WORKFLOWBASEFILE"_pub_"$i".json"
  rm -f "workflows/"$FILENAMEPUB

  FILENAMESUB=$WORKFLOWBASEFILE"_sub_"$i".json"
  rm -f "workflows/"$FILENAMESUB

done