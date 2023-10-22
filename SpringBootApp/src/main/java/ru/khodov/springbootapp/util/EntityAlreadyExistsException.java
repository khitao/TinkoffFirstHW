package ru.khodov.springbootapp.util;

import org.springframework.dao.DataAccessException;

public class EntityAlreadyExistsException extends DataAccessException {
    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }
}
