#!/bin/bash

TOPICS=$1
WORKFLOWBASEFILE=$2
BYTES=$3

for i in $(seq 1 $TOPICS); do 

  FILENAMEPUB=$PWD/$WORKFLOWBASEFILE"_pub_"$i".json"

  rm -f $FILENAMEPUB

  touch $FILENAMEPUB

  printf "{\"name\": \"Test_Workflow_Pub_$i\",\n" >> "$FILENAMEPUB"
  printf " \"numberOfPaths\": 1,\n" >> "$FILENAMEPUB"
  printf " \"numberOfDiscardedPaths\": 0,\n" >> "$FILENAMEPUB"
  printf " \"numberOfValidPaths\": 1,\n" >> "$FILENAMEPUB"
  printf " \"numberOfDistinctValidPaths\": 1,\n" >> "$FILENAMEPUB"
  printf " \"discardedPathIDs\":[],\n" >> "$FILENAMEPUB"
  printf " \"paths\":[\n" >> "$FILENAMEPUB"
  printf "    {\"pathID\": \"1.1\",\n" >> "$FILENAMEPUB"
  printf "     \"duplicatePathIDs\":[],\n" >> "$FILENAMEPUB"
  printf "     \"ops\":[\n" >> "$FILENAMEPUB"
  printf "        { \"vertex\":\"Test\", \"op\":\"writeResource\", \"resourceName\":\"Topic_Name_$i\", \"roleName\":\"Role_Name_$i\", \"type\":\"persistent\", \"resourceSize\":$BYTES, \"measure\":false}\n" >> "$FILENAMEPUB"
  printf "    ]}\n" >> "$FILENAMEPUB"
  printf "]}\n" >> "$FILENAMEPUB"



  FILENAMESUB=$PWD/$WORKFLOWBASEFILE"_sub_"$i".json"

  rm -f $FILENAMESUB

  touch $FILENAMESUB

  printf "{\"name\": \"Test_Workflow_Sub_$i\",\n" >> "$FILENAMESUB"
  printf " \"numberOfPaths\": 1,\n" >> "$FILENAMESUB"
  printf " \"numberOfDiscardedPaths\": 0,\n" >> "$FILENAMESUB"
  printf " \"numberOfValidPaths\": 1,\n" >> "$FILENAMESUB"
  printf " \"numberOfDistinctValidPaths\": 1,\n" >> "$FILENAMESUB"
  printf " \"discardedPathIDs\":[],\n" >> "$FILENAMESUB"
  printf " \"paths\":[\n" >> "$FILENAMESUB"
  printf "    {\"pathID\": \"1.1\",\n" >> "$FILENAMESUB"
  printf "     \"duplicatePathIDs\":[],\n" >> "$FILENAMESUB"
  printf "     \"ops\":[\n" >> "$FILENAMESUB"
  printf "        { \"vertex\":\"Test\", \"op\":\"readResource\", \"resourceName\":\"Topic_Name_$i\", \"roleName\":\"Role_Name_$i\", \"type\":\"persistent\", \"measure\":false}\n" >> "$FILENAMESUB"
  printf "    ]}\n" >> "$FILENAMESUB"
  printf "]}\n" >> "$FILENAMESUB"
done
