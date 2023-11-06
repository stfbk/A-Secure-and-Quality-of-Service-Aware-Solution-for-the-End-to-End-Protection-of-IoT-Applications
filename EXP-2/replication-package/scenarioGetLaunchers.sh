#!/bin/bash

# Number of publishers, subscribers, topics, minutes
PUBLISHERS=$1
SUBSCRIBERS=$2
TOPICS=$3
MINUTES=$4
BYTES=$5
USERS=$(($PUBLISHERS+$SUBSCRIBERS))

echo "E_1. Launch CryptoAC, wait that containers are up"
echo "cd ./scripts && chmod +x starterScriptCryptoAC_${USERS}users.sh && ./starterScriptCryptoAC_${USERS}users.sh"
echo ""
echo ""
echo "E_2. Launch the Initializer for CryptoAC, what for script to finish"
echo "cd ./launchers && chmod +x launcherInitializer_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers${TOPICS}topics.sh && ./launcherInitializer_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers${TOPICS}topics.sh"
echo ""
echo ""
echo "E_3. Distribute user profiles to all CryptoAC instances, what for script to finish"
echo "cd ./scripts && chmod +x copyprofiles.sh && ./copyprofiles.sh"
echo ""
echo ""
echo "E_4. Launch Locust Master"
echo "cd ./launchers && chmod +x ./launcherMaster_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers${MINUTES}minutes.sh && ./launcherMaster_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers${MINUTES}minutes.sh"
echo ""
echo ""
echo "E_5. Launch Locust Clients"
echo "cd ./launchers && chmod +x launcherClients_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers.sh && ./launcherClients_${PUBLISHERS}publishers${SUBSCRIBERS}subscribers.sh"
