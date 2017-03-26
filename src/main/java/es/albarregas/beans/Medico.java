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
@ManagedBean(name = "medico")
@Table(name = "Medicos")
public class Medico extends Usuario {

    @Column(name = "idMedico")
    private int id;
    private String numColegiado;
    private int idServicio;
    private String jefe;
    private String titulos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getTitulos() {
        return titulos;
    }

    public void setTitulos(String titulos) {
        this.titulos = titulos;
    }
    
    
}
