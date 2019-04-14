## Introduction to Blockchain with Java using Ethereum, web3j and Spring Boot  [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

Detailed description can be found here: [Introduction to Blockchain with Java using Ethereum, web3j and Spring Boot](https://piotrminkowski.wordpress.com/2018/06/22/introduction-to-blockchain-with-java-using-ethereum-web3j-and-spring-boot/)

## Deploy Guide
### Preparation

1. Java Development Environment.
2. Maven.
3. A reachable ETH network.
4. Postman.

### Modification

1. Change the connection configration in `/src/main/resources/application.yml` to a reachable ETH node url.
2. If a Rec system has already deployed on the network, change the address in the `/src/main/resources/system.json` to that deployed contract as the entrypoint.

### Start

1. Import the project into an IDE.
2. Download the dependencies.
3. Run.
4. Send JSON requests to `localhost:8090`.

### API

An available Swagger UI is on the port of 8090.

Find the UI: https://github.com/txhsl/IoT-tracking-system-ui