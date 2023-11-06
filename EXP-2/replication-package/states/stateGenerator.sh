#!/bin/bash

CONTAINERS=$1
TOPICS=$2
STATEFILE=$3

rm -f $STATEFILE

PA=$(( $TOPICS*$TOPICS ))

printf "{\n" >> "$STATEFILE"
printf "    \"U\":$CONTAINERS,\n" >> "$STATEFILE"
printf "    \"R\":$TOPICS,\n" >> "$STATEFILE"
printf "    \"P\":$TOPICS,\n" >> "$STATEFILE"
printf "    \"UR\":$CONTAINERS,\n" >> "$STATEFILE"
printf "    \"PA\":$PA,\n" >> "$STATEFILE"
printf "    \"roles/user\": {\n" >> "$STATEFILE"
printf "        \"max\":$TOPICS,\n" >> "$STATEFILE"
printf "        \"min\":1\n" >> "$STATEFILE"
printf "    },\n" >> "$STATEFILE"
printf "    \"users/role\": {\n" >> "$STATEFILE"
printf "        \"max\":$CONTAINERS,\n" >> "$STATEFILE"
printf "        \"min\":1\n" >> "$STATEFILE"
printf "    },\n" >> "$STATEFILE"
printf "    \"permissions/role\": {\n" >> "$STATEFILE"
printf "        \"max\":$TOPICS,\n" >> "$STATEFILE"
printf "        \"min\":1\n" >> "$STATEFILE"
printf "    },\n" >> "$STATEFILE"
printf "    \"roles/permission\": {\n" >> "$STATEFILE"
printf "        \"max\":$TOPICS,\n" >> "$STATEFILE"
printf "        \"min\":1\n" >> "$STATEFILE"
printf "    }\n" >> "$STATEFILE"
printf "}\n" >> "$STATEFILE"
