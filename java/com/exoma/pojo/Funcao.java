package com.exoma.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcao")
public class Funcao {

    @Id
    @Column(name = "idusu")
    private Integer idusu;

    @Id
    @Column(name = "idper")
    private Integer idper;

}