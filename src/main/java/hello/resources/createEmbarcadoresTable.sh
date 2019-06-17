#!/usr/bin/env bash
aws dynamodb create-table ^
    --table-name Embarcadores ^
    --attribute-definitions ^
        AttributeName=id,AttributeType=S ^
        AttributeName=nome,AttributeType=S ^
    --key-schema AttributeName=id,KeyType=HASH AttributeName=nome,KeyType=RANGE ^
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 ^
    --endpoint-url http://localhost:8000
