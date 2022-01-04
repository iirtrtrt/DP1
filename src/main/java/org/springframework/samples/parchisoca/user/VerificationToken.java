package org.springframework.samples.parchisoca.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Entity
@Setter
@Getter
public class VerificationToken
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime creationTime;


    String token;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    public VerificationToken(User user) {
        this.user = user;
        this.creationTime = LocalDateTime.now();
        this.token =  UUID.randomUUID().toString();
    }


    public VerificationToken() {

    }
}
