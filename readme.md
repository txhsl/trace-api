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

#### `/system`

##### `/system/reset`

Redeploy all the contracts and reset the entry point.

Method: `Post`

RequestBody: `null`

ResponseBody: `{ "result": true }`

##### `/system/addRole`

Add a new role to the logged-in account.

Method: `Post`

RequestBody: `{ "roleName": "string" }`

ResponseBody: `{
  "blockHash": "string",
  "blockNumber": 0,
  "blockNumberRaw": "string",
  "contractAddress": "string",
  "cumulativeGasUsed": 0,
  "cumulativeGasUsedRaw": "string",
  "from": "string",
  "gasUsed": 0,
  "gasUsedRaw": "string",
  "logs": [
    {
      "address": "string",
      "blockHash": "string",
      "blockNumber": 0,
      "blockNumberRaw": "string",
      "data": "string",
      "logIndex": 0,
      "logIndexRaw": "string",
      "removed": true,
      "topics": [
        "string"
      ],
      "transactionHash": "string",
      "transactionIndex": 0,
      "transactionIndexRaw": "string",
      "type": "string"
    }
  ],
  "logsBloom": "string",
  "root": "string",
  "status": "string",
  "statusOK": true,
  "to": "string",
  "transactionHash": "string",
  "transactionIndex": 0,
  "transactionIndexRaw": "string"
}`

#### `/user`
##### `/user/signIn`

Sign in and unlock an acount.

Method: `Post`

RequestBody: `{
  "address": "string",
  "password": "string"
}`

ResponseBody: `{
  "result": true
}`

##### `/user/addProperty`

Create a new property and add the storage contract to the user with both read and write permission.

Method: `Post`

RequestBody: `{
  "propertyName": "string"
}`

ResponseBody: `[
  {
    "blockHash": "string",
    "blockNumber": 0,
    "blockNumberRaw": "string",
    "contractAddress": "string",
    "cumulativeGasUsed": 0,
    "cumulativeGasUsedRaw": "string",
    "from": "string",
    "gasUsed": 0,
    "gasUsedRaw": "string",
    "logs": [
      {
        "address": "string",
        "blockHash": "string",
        "blockNumber": 0,
        "blockNumberRaw": "string",
        "data": "string",
        "logIndex": 0,
        "logIndexRaw": "string",
        "removed": true,
        "topics": [
          "string"
        ],
        "transactionHash": "string",
        "transactionIndex": 0,
        "transactionIndexRaw": "string",
        "type": "string"
      }
    ],
    "logsBloom": "string",
    "root": "string",
    "status": "string",
    "statusOK": true,
    "to": "string",
    "transactionHash": "string",
    "transactionIndex": 0,
    "transactionIndexRaw": "string"
  }
]`

##### `/user/assign`

Assign a read/write permission to a specific account。

Method: `Post`

RequestBody: `{
  "isRead": true,
  "propertyName": "string",
  "target": "string"
}`

ResponseBody: `{
  "blockHash": "string",
  "blockNumber": 0,
  "blockNumberRaw": "string",
  "contractAddress": "string",
  "cumulativeGasUsed": 0,
  "cumulativeGasUsedRaw": "string",
  "from": "string",
  "gasUsed": 0,
  "gasUsedRaw": "string",
  "logs": [
    {
      "address": "string",
      "blockHash": "string",
      "blockNumber": 0,
      "blockNumberRaw": "string",
      "data": "string",
      "logIndex": 0,
      "logIndexRaw": "string",
      "removed": true,
      "topics": [
        "string"
      ],
      "transactionHash": "string",
      "transactionIndex": 0,
      "transactionIndexRaw": "string",
      "type": "string"
    }
  ],
  "logsBloom": "string",
  "root": "string",
  "status": "string",
  "statusOK": true,
  "to": "string",
  "transactionHash": "string",
  "transactionIndex": 0,
  "transactionIndexRaw": "string"
}`

#### `/data`
##### `/data/read`

Read a data record of a specific id and a specfic property.

Method: `Get`

RequestBody: `{
  "id": "string",
  "propertyName": "string"
}`

ResponseBody: `{
  "id": "string",
  "propertyName": "string",
  "data": "string"
}`

##### `/data/write`

Read a data record to a specific id and a specfic property.

Method: `Post`

RequestBody: `{
  "id": "string",
  "propertyName": "string",
  "data": "string"
}`

ResponseBody: `{
  "blockHash": "string",
  "blockNumber": 0,
  "blockNumberRaw": "string",
  "contractAddress": "string",
  "cumulativeGasUsed": 0,
  "cumulativeGasUsedRaw": "string",
  "from": "string",
  "gasUsed": 0,
  "gasUsedRaw": "string",
  "logs": [
    {
      "address": "string",
      "blockHash": "string",
      "blockNumber": 0,
      "blockNumberRaw": "string",
      "data": "string",
      "logIndex": 0,
      "logIndexRaw": "string",
      "removed": true,
      "topics": [
        "string"
      ],
      "transactionHash": "string",
      "transactionIndex": 0,
      "transactionIndexRaw": "string",
      "type": "string"
    }
  ],
  "logsBloom": "string",
  "root": "string",
  "status": "string",
  "statusOK": true,
  "to": "string",
  "transactionHash": "string",
  "transactionIndex": 0,
  "transactionIndexRaw": "string"
}`

##### `/data/readMultiple`

Read a group of data records of specific ids and specfic properties.

Method: `Get`

RequestBody: `{
  "ids": [
    "string"
  ],
  "propertyNames": [
    "string"
  ]
}`

ResponseBody: `{
  "ids": [
    "string"
  ],
  "propertyNames": [
    "string"
  ],
  "data": {
    "propertyName1": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    },
    "propertyName2": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    },
    "propertyName3": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    }
  }
}`

##### `/data/writeMultiple`

Write a group of data records of specific ids and specfic properties.

Method: `Post`

RequestBody: `{
  "data": {
    "propertyName1": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    },
    "propertyName2": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    },
    "propertyName3": {
      "id1": "string",
      "id2": "string",
      "id3": "string"
    }
  }
}`

ResponseBody: `[
  {
    "blockHash": "string",
    "blockNumber": 0,
    "blockNumberRaw": "string",
    "contractAddress": "string",
    "cumulativeGasUsed": 0,
    "cumulativeGasUsedRaw": "string",
    "from": "string",
    "gasUsed": 0,
    "gasUsedRaw": "string",
    "logs": [
      {
        "address": "string",
        "blockHash": "string",
        "blockNumber": 0,
        "blockNumberRaw": "string",
        "data": "string",
        "logIndex": 0,
        "logIndexRaw": "string",
        "removed": true,
        "topics": [
          "string"
        ],
        "transactionHash": "string",
        "transactionIndex": 0,
        "transactionIndexRaw": "string",
        "type": "string"
      }
    ],
    "logsBloom": "string",
    "root": "string",
    "status": "string",
    "statusOK": true,
    "to": "string",
    "transactionHash": "string",
    "transactionIndex": 0,
    "transactionIndexRaw": "string"
  }
]`

#### Notice
1. The response body in the format like: `{
    "blockHash": "string",
    "blockNumber": 0,
    "blockNumberRaw": "string",
    "contractAddress": "string",
    "cumulativeGasUsed": 0,
    "cumulativeGasUsedRaw": "string",
    "from": "string",
    "gasUsed": 0,
    "gasUsedRaw": "string",
    "logs": [
      {
        "address": "string",
        "blockHash": "string",
        "blockNumber": 0,
        "blockNumberRaw": "string",
        "data": "string",
        "logIndex": 0,
        "logIndexRaw": "string",
        "removed": true,
        "topics": [
          "string"
        ],
        "transactionHash": "string",
        "transactionIndex": 0,
        "transactionIndexRaw": "string",
        "type": "string"
      }
    ],
    "logsBloom": "string",
    "root": "string",
    "status": "string",
    "statusOK": true,
    "to": "string",
    "transactionHash": "string",
    "transactionIndex": 0,
    "transactionIndexRaw": "string"
  }`
is actually a reception on the ETH network to prove that the transcation succeeded. Treat it the same as `true` if you want to ignore the part of blockchain.
2. Any other failed/error message will be presented by springboot itself in the format of expertion.