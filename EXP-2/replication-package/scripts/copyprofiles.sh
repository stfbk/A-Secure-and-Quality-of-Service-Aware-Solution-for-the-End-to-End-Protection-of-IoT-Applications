#!/bin/sh

sudo rm -f -r ./profiles && sudo mkdir ./profiles && sudo docker cp $(sudo docker ps -a --filter=name="installation_cryptoac_proxy_1_1" -q):/cryptoac/server/proxy/upload/ ./profiles

containers=$(sudo docker ps -a --filter=name="installation_cryptoac_proxy*" -q)

for container in $containers
do 
    sudo docker cp ./profiles/* $container:/cryptoac/server/proxy/
done