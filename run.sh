#!/bin/bash

# Load environment variables from .env file and run the application
export SPRING_DATASOURCE_URL='jdbc:postgresql://ep-odd-bar-ah7n0ouv-pooler.c-3.us-east-1.aws.neon.tech/authservice?sslmode=require'
export SPRING_DATASOURCE_USERNAME='neondb_owner'
export SPRING_DATASOURCE_PASSWORD='npg_gv2hANV6nRdp'
export JWT_SECRET='h9DolXR+go5BoyWOXxDdLhmsuDHxZwUgEnXwisCDKQw='
export JWT_ACCESS_TOKEN_EXPIRATION='900000'
export JWT_REFRESH_TOKEN_EXPIRATION='604800000'
export SPRING_JPA_SHOW_SQL='true'

# Run the application
mvn spring-boot:run
