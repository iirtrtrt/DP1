package org.springframework.samples.parchisoca.game;

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

    @NotNull
    private String colorName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


}
