package com.betting.javawalletsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ErrorDto {
    @NotBlank
    String message;
}
