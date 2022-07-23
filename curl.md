# Curl commands

## MealRestController

### GET

- get all meals

```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'
```

- get meal with id 100003

```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100003'
```

- filter meals by date and time

```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=00:00:00&endDate=2020-01-31&endTime=13:00:00'
```

### POST

- add meal

```shell
curl --location --request POST 'http://localhost:8080/topjava/rest/meals/' \
--header 'Content-Type: application/json;charset=UTF-8' \
--data-raw '{
    "dateTime": "2022-07-23T14:31",
    "description": " New Curl dinner",
    "calories": 560
}'
```

### PUT

- update meal with id=100003

 ```shell
 curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100003' \
--header 'Content-Type: application/json' \
--data-raw '{
"dateTime": "2020-01-30T10:00:00",
"description": "Завтрак Curl Updated",
"calories": 500
}'
```

### DELETE

- delete meal

```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/10003'
```

