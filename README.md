# A Secure and Quality of Service-Aware Solution for the End to End Protection of IoT Applications

Internet of Things (IoT) applications generate and exchange a huge amount of — often **sensitive** — data, which are threatened by external attackers, malicious insiders and **honest but curious** Edge and Cloud providers. Hence, the lack of proper security mechanisms may result in the loss of the confidentiality and the integrity of such data, leading to — also depending on the application field (e.g., eHealth, intelligent transportation systems, smart buildings) — **exfiltration of privileged information, violation of critical functions and even life-threatening situations**. Moreover, when security mechanisms are present, they should be configured and deployed to both respect the trust assumptions (e.g., on the presence of certain attackers) and suit the performance goals (e.g., low latency, high scalability) relevant to the underlying scenario. 

Therefore, in the paper entitled **Optimizing Security and Quality of Service for End-to-End Cryptographic Access Control in IoT Applications** we propose a security mechanism consisting of a Cryptographic Access Control (CAC) scheme providing both protection (i.e., confidentiality and integrity guarantees) and enforcement of Access Control (AC) policies for sensitive data exchanged (i.e., in transit) in IoT applications that employ topic-based publish-subscribe protocols. By building on previous work, we also formalize an **optimization problem** aiming at fine-tuning the deployment of the entities composing our CAC scheme into 4 different (logical or physical) security domains — i.e., clients, on-premise, Edge and Cloud — to strike the best possible balance between security and quality of service. To demonstrate the benefits of our contributions, we consider three different scenarios for IoT applications as running examples: Remote Patient Monitoring, Cooperative Maneuvering and Smart Lock. In detail, we show how solving our optimization problem allows selecting each time the best **architecture** — that is, the best assignment of entities to domains — for our scheme according to the assumptions and the goals of the underlying scenario. Finally, we implement our scheme into an open-source tool called **CryptoAC** and conduct a thorough performance evaluation to assess its performance and scalability with respect to a baseline configuration (i.e., with no security mechanism) and a TLS-based configuration. Moreover, we discuss and compare in detail the security properties (e.g., end-to-end encryption, integrity, forward secrecy) offered by our scheme with respect to TLS.

---

This repository contains the replication package and the results o of the experimental evaluation of CryptoAC for IoT applications. Please refer to the paper for more details:

* [**EXP-1 - Communication Setup**](./EXP-1) - replication package and results for **EXP-1**;

* [**EXP-2 - Communication Overhead**](./EXP-2) - replication package and results for **EXP-2**;

* [**EXP-3 - Administrative Actions**](./EXP-3) - replication package and results for **EXP-3**.

The experiment **EXP-4 - Administrative Actions** does not involve an actual experiment; please refer to the paper for more details.
