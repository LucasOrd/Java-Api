package com.coderhouse.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto implements Serializable {
    private Long id;
    private String description;

}