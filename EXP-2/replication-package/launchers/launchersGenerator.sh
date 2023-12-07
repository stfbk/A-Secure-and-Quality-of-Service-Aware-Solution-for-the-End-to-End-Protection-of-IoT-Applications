#!/bin/bash

PUBLISHERS=$1
SUBSCRIBERS=$2
TOPICS=$3
MASTERLAUNCHERFILE=$4
CLIENTSLAUNCHERFILE=$5
STATEFILE=$6
INITIALIZERLAUNCHERFILE=$7
WORKFLOWBASEFILE=$8
MINUTES=$9

USERS=$(($PUBLISHERS+$SUBSCRIBERS))

rm -f $CLIENTSLAUNCHERFILE
rm -f $MASTERLAUNCHERFILE
rm -f $INITIALIZERLAUNCHERFILE



# Generate the clients launcher
printf "#!/bin/sh\n" >> "$CLIENTSLAUNCHERFILE"
printf "\n" >> "$CLIENTSLAUNCHERFILE"

index=0
for i in $(seq 1 $PUBLISHERS); do 

  # Get the list of all workflow files to assign to this client
  workflowFilesPUB="../workflows/"$WORKFLOWBASEFILE"_pub_"$(($index+1))".json;"
  index=$(($index+1))
  index=$(($index % $TOPICS))
  if [[($TOPICS > $PUBLISHERS)]] ; then
    otherWorkflows=$(($TOPICS / $PUBLISHERS))
    otherWorkflows=$(($otherWorkflows-1))
    mod=$(($TOPICS % $PUBLISHERS))
    if [[ ($i < $mod) || ($i = $mod)]] ; then
      otherWorkflows=$(($otherWorkflows+1))
    fi
    for j in $(seq 1 $otherWorkflows); do 
      workflowFilesPUB+="../workflows/"$WORKFLOWBASEFILE"_pub_"$(($index+1))".json"
      index=$(($index+1))
      index=$(($index % $TOPICS))
      workflowFilesPUB+=";"
    done
  fi  
  workflowFilesPUB=${workflowFilesPUB::-1}

  port=$((40001+$i))

  printf "    env operations=\"$workflowFilesPUB\" host=https://127.0.0.1:$port locust -f ../ACME/simulator/Engine.py \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --operations=\"$workflowFilesPUB\" \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --adapter=CryptoACMQTT \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --loglevel=WARNING \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --reservePolicy \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --syncPolicyAcrossWorkers \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --host=https://127.0.0.1:$port \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --headless \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --worker \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --master-host=127.0.0.1 \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --master-port=5557 &\n" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"

done


printf "\n" >> "$CLIENTSLAUNCHERFILE"
printf "\n" >> "$CLIENTSLAUNCHERFILE"
printf "\n" >> "$CLIENTSLAUNCHERFILE"
printf "\n" >> "$CLIENTSLAUNCHERFILE"

index=0
for i in $(seq 1 $SUBSCRIBERS); do 

  # Get the list of all workflow files to assign to this client
  workflowFilesSUB="../workflows/"$WORKFLOWBASEFILE"_sub_"$(($index+1))".json;"
  index=$(($index+1))
  index=$(($index % $TOPICS))
  if [[($TOPICS > $SUBSCRIBERS)]] ; then
    otherWorkflows=$(($TOPICS / $SUBSCRIBERS))
    otherWorkflows=$(($otherWorkflows-1))
    mod=$(($TOPICS % $SUBSCRIBERS))
    if [[ ($i < $mod) || ($i = $mod)]] ; then
      otherWorkflows=$(($otherWorkflows+1))
    fi
    for j in $(seq 1 $otherWorkflows); do 
      workflowFilesSUB+="../workflows/"$WORKFLOWBASEFILE"_sub_"$(($index+1))".json"
      index=$(($index+1))
      index=$(($index % $TOPICS))
      workflowFilesSUB+=";"
    done
  fi  
  workflowFilesSUB=${workflowFilesSUB::-1}

  port=$((40001+$i+$PUBLISHERS))

  printf "    env operations=\"$workflowFilesSUB\" host=https://127.0.0.1:$port locust -f ../ACME/simulator/Engine.py \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --operations=\"$workflowFilesSUB\" \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --adapter=CryptoACMQTT \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --loglevel=WARNING \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --reservePolicy \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --syncPolicyAcrossWorkers \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --host=https://127.0.0.1:$port \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --headless \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --worker \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --master-host=127.0.0.1 \\" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"
  printf "        --master-port=5557 &\n" >> "$CLIENTSLAUNCHERFILE"
  printf "\n" >> "$CLIENTSLAUNCHERFILE"

done



# Get the list of all workflow files (this list will
# be feed to the Master and also to the Initializer)
workflowFiles=""
for i in $(seq 1 $TOPICS); do 
  workflowFiles+="../workflows/"$WORKFLOWBASEFILE"_pub_"$i".json"
  workflowFiles+=";"
  workflowFiles+="../workflows/"$WORKFLOWBASEFILE"_sub_"$i".json"
  workflowFiles+=";"
done
workflowFiles=${workflowFiles::-1}

# Generate the master launcher
printf "#!/bin/sh\n" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "env operations=\"$workflowFiles\" host=https://127.0.0.1:40002 locust -f ../ACME/simulator/Engine.py \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --operations=\"$workflowFiles\" \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --adapter=CryptoACMQTT \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --loglevel=WARNING \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --reservePolicy \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --syncPolicyAcrossWorkers \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --host=https://127.0.0.1:40002 \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --master \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --autostart \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --expect-workers=$USERS \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --numberOfWorkers=$USERS \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    --master-bind-port=5557 \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    -t ${MINUTES}m \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    -u $USERS \\" >> "$MASTERLAUNCHERFILE"
printf "\n" >> "$MASTERLAUNCHERFILE"
printf "    -r 100\n" >> "$MASTERLAUNCHERFILE"



# Generate the initializer
printf "#!/bin/sh\n" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "python3 ../ACME/simulator/Initializer.py \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    ../states/$STATEFILE \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    CryptoACMQTT \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    https://127.0.0.1:40002 \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    --logLevel=WARNING \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    --seed=1 \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    --doInitialize \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    --flexibleACState \\" >> "$INITIALIZERLAUNCHERFILE"
printf "\n" >> "$INITIALIZERLAUNCHERFILE"
printf "    --operations=\"$workflowFiles\"\n" >> "$INITIALIZERLAUNCHERFILE"
