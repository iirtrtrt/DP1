package org.springframework.samples.parchisoca.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
public class VerificationToken
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime expirationDate;


    String token;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName ="username")
    private User user;



}
