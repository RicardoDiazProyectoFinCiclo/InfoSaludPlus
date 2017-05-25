/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.modelo;

import java.io.Serializable;
import java.sql.Blob;
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
@Table(name = "Imagenes")
public class Imagen implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idImagen")
    private int id;
    @Column(nullable = false)
    private Blob fuenteImagen;
    @Column(nullable = false)
    private String tipoMIME = "";
    private String descripcion = "";
    private int idUsuario;
    private int idCentro;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getFuenteImagen() {
        return fuenteImagen;
    }

    public void setFuenteImagen(Blob fuenteImagen) {
        this.fuenteImagen = fuenteImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoMIME() {
        return tipoMIME;
    }

    public void setTipoMIME(String tipoMIME) {
        this.tipoMIME = tipoMIME;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

}
