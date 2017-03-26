/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@Table (name = "Pacientes")
@ManagedBean (name = "paciente")
public class Paciente extends Usuario{
    @Column(name = "idPaciente")
    private int id;
    private String numSegSoc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumSegSoc() {
        return numSegSoc;
    }

    public void setNumSegSoc(String numSegSoc) {
        this.numSegSoc = numSegSoc;
    }
    
    
}
