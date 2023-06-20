package com.example.userservice.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Users implements Comparable<Users>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "`id`", nullable = false)

    @ApiModelProperty(hidden = true)
    private Long id;
    private String firstName;
    private String lastName;
    @Override
    public int compareTo(Users o) {
        return this.getLastName().compareToIgnoreCase(o.getLastName());
    }

}
