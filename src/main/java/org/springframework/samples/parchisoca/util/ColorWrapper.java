package org.springframework.samples.parchisoca.util;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
public class ColorWrapper
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String test;
    @NotNull
    private String colorName;


    public ColorWrapper( ) {
    }

    public ColorWrapper(String error) {
        colorName = error;
    }
}
