package org.springframework.samples.parchisoca.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime creationTime;

    public String token;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    public VerificationToken(User user) {
        this.user = user;
        this.creationTime = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    public VerificationToken() {

    }
}
