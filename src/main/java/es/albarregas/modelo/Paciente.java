/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *  Modelo relacionado con la tabla pacientes. Hereda de usuarios
 * @author Ricardo
 */
@Entity
@Table(name = "Pacientes")
public class Paciente extends Usuario {

    @Column(nullable = false)
    private String numSegSoc;
    @OneToOne
    @JoinColumn(name = "idMedico")
    private Medico medico;

    public String getNumSegSoc() {
        return numSegSoc;
    }

    public void setNumSegSoc(String numSegSoc) {
        this.numSegSoc = numSegSoc;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

}
