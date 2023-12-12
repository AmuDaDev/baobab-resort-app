package com.baobab.resort.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author AmuDaDev
 * @created 11/12/2023
 */
@Data
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
