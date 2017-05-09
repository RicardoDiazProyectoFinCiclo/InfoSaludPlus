/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.modelo;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.persistencia.FacesUtils;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@Table(name = "Pacientes")
public class Paciente extends Usuario {

    @Column(nullable = false)
    private String numSegSoc;

    public String getNumSegSoc() {
        return numSegSoc;
    }

    public void setNumSegSoc(String numSegSoc) {
        this.numSegSoc = numSegSoc;
    }

}
