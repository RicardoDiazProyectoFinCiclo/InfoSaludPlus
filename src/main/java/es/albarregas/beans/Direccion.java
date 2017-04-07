/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@ManagedBean(name = "direccion")
@Table(name = "Direcciones")
public class Direccion implements Serializable {

    @Id
    @Column(name = "idDireccion")
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCentro")
    private Centro centro;
    private String nombreDireccion;
    private String direccion;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idPueblo")
    private Pueblo pueblo;
    private String telefono;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreDireccion() {
        return nombreDireccion;
    }

    public void setNombreDireccion(String nombreDireccion) {
        this.nombreDireccion = nombreDireccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Pueblo getPueblo() {
        return pueblo;
    }

    public void setPueblo(Pueblo pueblo) {
        this.pueblo = pueblo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
