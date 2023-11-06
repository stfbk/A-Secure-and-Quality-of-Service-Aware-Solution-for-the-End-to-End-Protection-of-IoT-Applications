# EXP-3 - Administrative Actions

The results of **EXP-3** are available in the [dedicated folder](./results/).

To replicate the experiments, please download **CryptoAC** from the [dedicated repository](https://github.com/stfbk/CryptoAC/tree/CryptoAC_IoT_Evaluation) (please note that the branch is `CryptoAC_IoT_Evaluation`). Then, start the Mosquitto MQTT broker and the Redis datastore by running --- in the `docs/source/gettingstarted/installation` path of the **CryptoAC** repository --- the following command:

```bash
./cleanAllAndBuild.sh && ./startCryptoAC_ALL.sh "cryptoac_redis cryptoac_mosquitto_no_dynsec"
```

Then, to run the benchmarks in the [benchmark file](./replication-package/CoreRBACMQTTBenchmark.kt): 
* open **CryptoAC** in an IDE — e.g., [IntelliJ Idea](https://www.jetbrains.com/help/idea/get-started-with-kotlin.html);
* include the [benchmark file](./replication-package/CoreRBACMQTTBenchmark.kt) in the `jvmMain` source set (package `benchmark`);
* run the benchmark using [Gradle](https://gradle.org/https://gradle.org/) (i.e., the `benchmark` task).
