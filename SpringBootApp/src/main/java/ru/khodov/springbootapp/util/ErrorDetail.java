package ru.khodov.springbootapp.util;

import lombok.Data;

@Data
public class ErrorDetail {
    private int code;
    private String message;
}
