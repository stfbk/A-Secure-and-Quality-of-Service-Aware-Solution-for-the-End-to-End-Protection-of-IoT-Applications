#!/bin/bash

STARTERFILE=$1
COMPOSEFILE=$2

rm -f $STARTERFILE

printf "#!/bin/bash\n" >> "$STARTERFILE"
printf "\n" >> "$STARTERFILE"
printf "cp ../compose/$COMPOSEFILE ../CryptoAC/docs/source/gettingstarted/installation\n" >> "$STARTERFILE"
printf "\n" >> "$STARTERFILE"
printf "newgrp docker << END\n" >> "$STARTERFILE"
printf "    cd ../CryptoAC/docs/source/gettingstarted/installation && ./cleanAll.sh\n" >> "$STARTERFILE"
printf "END\n" >> "$STARTERFILE"
printf "\n" >> "$STARTERFILE"
printf "newgrp docker << END\n" >> "$STARTERFILE"
printf "    cd ../CryptoAC/docs/source/gettingstarted/installation && ./cleanAll.sh\n" >> "$STARTERFILE"
printf "END\n" >> "$STARTERFILE"
printf "\n" >> "$STARTERFILE"
printf "newgrp docker << END\n" >> "$STARTERFILE"
printf "    cd ../CryptoAC/docs/source/gettingstarted/installation && ./build.sh \"CryptoAC/ DMInterfaceMQTT/ MMInterfaceRedis/\"\n" >> "$STARTERFILE"
printf "END\n" >> "$STARTERFILE"
printf "\n" >> "$STARTERFILE"
printf "newgrp docker << END\n" >> "$STARTERFILE"
printf "    cd ../CryptoAC/docs/source/gettingstarted/installation && docker-compose --env-file .all.env.dev -f ./$COMPOSEFILE up \$1\n" >> "$STARTERFILE"
printf "END\n" >> "$STARTERFILE"



