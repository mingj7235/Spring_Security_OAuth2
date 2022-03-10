package com.joshua.spring_security_jwt.security_01.model;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private String role; //ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    private Timestamp createDate;
}
