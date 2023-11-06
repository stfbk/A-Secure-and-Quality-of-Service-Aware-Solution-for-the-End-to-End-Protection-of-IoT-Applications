This is the replication package for the second experiment ("EXP-2 - Communication Overhead") of the article "Optimizing Security and Quality of Service for End-to-End Cryptographic Access Control in IoT Applications"


# Prerequisites

A device running a linux-based distribution (e.g., Ubuntu 22.04) with the following packages installed:
* [docker](https://docs.docker.com/get-docker/);
* [docker-compose](https://docs.docker.com/compose/install/);
* [python3](https://www.python.org/downloads/) (version 3.8 or later);
* [openjdk](https://openjdk.org/) (Java 11+);

The following Python3 packages must be installed thorugh, e.g., pip3:
* [locust](https://docs.locust.io/en/stable/installation.html);
* [names](https://pypi.org/project/names/);
* [websocket-client](https://github.com/websocket-client/websocket-client);
* [gevent](https://pypi.org/project/gevent/);
* [requests](https://pypi.org/project/requests/);

Download in this directory:
* ACME by cloning https://github.com/stfbk/ACME/tree/CryptoAC_IoT_Evaluation/ (note that the branch is not "main" but "CryptoAC_IoT_Evaluation"; after cloning, remember to run `git checkout CryptoAC_IoT_Evaluation`)
* CryptoAFC by cloning https://github.com/stfbk/CryptoAC/tree/CryptoAC_IoT_Evaluation/ (note that the branch is not "main" but "CryptoAC_IoT_Evaluation"; after cloning, remember to run `git checkout CryptoAC_IoT_Evaluation`)

Generate the TLS certificate for the MQTT broker:
* go to `CryptoAC/docs/source/gettingstarted/installation/DMInterfaceMQTT/`;
* run `./createCertificates.sh` (this command requires `keytool` from the OpenJDK); whenever you need to insert a pass phrase, insert the passphrase `password` (without quotes); leave all other (optional) fields as they are. Finally, answer `yes` to the question `Trust this certificate? [no]:`.



# SCENARIO GENERATION
1. run `./scenarioGenerator.sh <|pub|> <|sub|> <|topics|> <|minutes|> <|size|>`, where `<|pub|>` is the number of publishers, `<|sub|>` is the number of subscribers, `<|topics|>` is the number of topics, `<|minutes|>` the number of minutes the experiment will last, `<|size|>` the size of MQTT messages (in bytes). Publishers -- as well as subscribers -- are equally divided based on the number of topics. For instance:
* if `<|pub|>=4` and `<|topics|>=2`, then each topic will have two publishers;
* if `<|pub|>=5` and `<|topics|>=2`, then the first topic will have three publishers, while the second topic will have two publishers;
* if `<|pub|>=2` and `<|topics|>=5`, then the first publisher will have three topics, while the second publisher will have two topics.

> Example: `./scenarioGenerator.sh 1 1 1 1 1024`



# SCENARIO CONFIGURATION
1. Enable and disable TLS:
    * go to the file `ACME/simulator/adapters/CryptoAC/CryptoACRBACMQTT.py`;
    * search in the file for the following string `"tls":`;
    * to enable TLS, set the value of `"tls":` to `True` and replace the port 1883 with 1884;
    * to disable TLS, set the value of `"tls":` to `False` and replace the port 1884 with 1883;
2. Enable and disable Cryptographic Access Control (CAC):
    * go to the file `ACME/simulator/adapters/CryptoAC/CryptoACRBAC.py`;
    * to disable CAC, replace all occurrences of the word `COMBINED` with `TRADITIONAL`;
    * to enable CAC, replace all occurrences of the word `TRADITIONAL` with `COMBINED`;
3. Modify the time that publishers wait between the publishing of two messages (i.e., how many seconds each publisher "sleeps" after having published a message).
    * go to the file `ACME/simulator/Engine.py`;
    * search in the file for the following string `wait_time = constant(`;
    * replace the time within parenthesis with the time that publishers wait between the publishing of two messages. The time is espressed in seconds.



# SCENARIO LAUNCHING
1. run `./scenarioGetLaunchers.sh <|pub|> <|sub|> <|topics|> <|minutes|> <|size|>` to get in console the commands tailored for your scenario.

    > Example: `./scenarioGetLaunchers.sh 1 1 1 1 1024`

    > Example output: 
    
    ```bash
    E_1. Launch CryptoAC, wait that containers are up
    cd ./scripts && chmod +x starterScriptCryptoAC_2users.sh && ./starterScriptCryptoAC_2users.sh


    E_2. Launch the Initializer for CryptoAC, what for script to finish
    cd ./launchers && chmod +x launcherInitializer_1publishers1subscribers1topics.sh && ./launcherInitializer_1publishers1subscribers1topics.sh


    E_3. Distribute user profiles to all CryptoAC instances, what for script to finish
    cd ./scripts && chmod +x copyprofiles.sh && ./copyprofiles.sh


    E_4. Launch Locust Master
    cd ./launchers && chmod +x ./launcherMaster_1publishers1subscribers1minutes.sh && ./launcherMaster_1publishers1subscribers1minutes.sh


    E_5. Launch Locust Clients
    cd ./launchers && chmod +x launcherClients_1publishers1subscribers.sh && ./launcherClients_1publishers1subscribers.sh
    ```

2. Once all workers started, the experimentation starts automatically. You should be able to see in real-time the results of the experimentation at 'http://0.0.0.0:8089/'. The experimentation ends in `<|minutes|>` minutes; collect the results from the UI ("Download Data" tab, "Download request statistics CSV" link). Data of MQTT messages are in the second column of the CSV (column "Name"), and strings in each row contain the following information separated by an underscore character ("_"):
- publisher name;
- subscriber name;
- topic name;
- message ID (should be unique across all messages);
- unix timestamp (in milliseconds) taken just before (possibily encrypting and) publishing the message;
- unix timestamp (in milliseconds) taken just after receiving (and possibily decrypting the) message.
