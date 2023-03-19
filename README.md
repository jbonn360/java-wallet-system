# Java Wallet System
## Description
This is a simple wallet API that can be used to deposit and withdraw funds to a wallet, place bets, update bets, and perform administrative tasks.

It uses an in-memory H2 database instance to persist information.

## Requirements
JRE 11+

Maven 3+

## Run the app
To run the application, run the main method in the source file 'JavaWalletSystemApplication.java' from an IDE. This file is present in the following directory: '../java-wallet-system/src/main/java/com/betting/javawalletsystem/'. If running from an IDE, annotation processing has to be enabled due to the following dependencies: lombok and mapstruct.

Alternatively, the application can also be built into a JAR file and run with the following commands: 

	mvn package
    java -jar java-wallet-system-0.0.1-SNAPSHOT.jar

By default, the application binds to port 8080.

## Credentials
The application ships with 2 accounts, removing the need to create accounts every time the application is started. One is a regular player account and the other is an admin account. These accounts' credentials are listed below:

	PlayerId: 1
	Username: lucky
	Password: mypassword123

	PlayerId: 2
	Username: admin
	Password: admin


## Run the tests
The tests can be run by opening the files under directory 
'../java-wallet-system/src/test/java/com/betting/javawalletsystem/' in an IDE and running them in there.

Alternatively, they can also be run via the following maven command in the project's root directory:

    mvn test

## REST Endpoints

## Deposit Funds

### Request

`POST /api/v1/funds/deposit`

    curl -i -H "Content-Type: application/json" -u lucky:mypassword123 -X POST -d '{"transactionId":1,"playerId":1,"amount":200}' http://localhost:8080/api/v1/funds/deposit

### Response

    HTTP/1.1 201 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 10:02:39 GMT
	{"transactionId":1,"playerId":1,"cashBalance":200.00,"bonusBalance":200.00}

## Withdraw Funds

### Request

`POST /api/v1/funds/withdraw`

    curl -i -H "Content-Type: application/json" -u lucky:mypassword123 -X POST -d '{"transactionId":2,"playerId":1,"amount":100}' http://localhost:8080/api/v1/funds/withdraw

### Response

    HTTP/1.1 201 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 10:10:34 GMT
	{"transactionId":2,"playerId":1,"cashBalance":100.00,"bonusBalance":200.00}

## Place Bet

### Request

`POST /api/v1/bet`

    curl -i -H "Content-Type: application/json" -u lucky:mypassword123 -X POST -d '{"transactionId":3,"playerId":1,"amount": 300}' http://localhost:8080/api/v1/bet

### Response

    HTTP/1.1 201 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 10:14:57 GMT

	{"transactionId":3,"playerId":1,"cashBalance":0,"bonusBalance":0.00}

## Update Bet Status

### Request

`POST /api/v1/admin/bet/win`

    curl -i -H "Content-Type: application/json" -u admin:admin -X POST -d '{"transactionId":4,"playerId":1,"amount":600,"betTransactionId":3}' http://localhost:8080/api/v1/admin/bet/win

### Response

    HTTP/1.1 200 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 10:29:55 GMT

	{"transactionId":4,"playerId":1,"cashBalance":200.00,"bonusBalance":400.00}

## Get List of All Transactions

### Request

`GET /api/v1/admin/transactions?limit={limitValue}&offset={offsetValue}`

    curl -i -H 'Accept: application/json' -u admin:admin "http://localhost:8080/api/v1/admin/transactions?limit=2&offset=0"

### Response

    HTTP/1.1 200 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 12:57:30 GMT


	{"transactionList":[{"transactionId":4,"transactionDt":"2023-03-19T10:29:55.311213Z","cashAmount":200.00,"cashBalanceAfter":200.00,"bonusAmount":400.00,"bonusBalanceAfter":400.00,"type":"BET_WIN","playerId":1,"player":"lucky"},{"transactionId":3,"transactionDt":"2023-03-19T10:29:48.369523Z","cashAmount":100.00,"cashBalanceAfter":0.00,"bonusAmount":200.00,"bonusBalanceAfter":0.00,"type":"BET_PLACEMENT","playerId":1,"player":"lucky"}]}

## Get Player Balance

### Request

`GET /api/v1/admin/player/balance?playerId={playerId}`

    curl -i -H 'Accept: application/json' -u admin:admin "http://localhost:8080/api/v1/admin/player/balance?playerId=1"

### Response

    HTTP/1.1 200 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Sun, 19 Mar 2023 13:00:29 GMT


	{"username":"lucky","cashBalance":200.00,"bonusBalance":400.00}