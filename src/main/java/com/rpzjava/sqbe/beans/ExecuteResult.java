package com.rpzjava.sqbe.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteResult {
    Boolean status;
    Object payload;
}
