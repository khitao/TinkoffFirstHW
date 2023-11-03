package ru.khodov.springbootapp;


import org.testcontainers.containers.PostgreSQLContainer;


public abstract class AbstractSingletonPostgresContainer {

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    static {
        postgres.start();
    }

}
