package org.springframework.samples.parchisoca.game;


import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name = "oca")
public class Oca extends GameBoard{
    @Id
    private Integer id;

    

    
    


}
