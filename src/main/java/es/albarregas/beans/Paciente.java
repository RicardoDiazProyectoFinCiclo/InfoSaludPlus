/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

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
@ManagedBean(name = "paciente")
public class Paciente extends Usuario {

    @Column(nullable = false)
    private String numSegSoc;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "idUsuario")
    private List<Cita> citas;

    public String getNumSegSoc() {
        return numSegSoc;
    }

    public void setNumSegSoc(String numSegSoc) {
        this.numSegSoc = numSegSoc;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public String registro() {

        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        System.out.println("Registrando paciente ");
        if (!igd.get("Usuario as u WHERE u.email = '" + this.getEmail() + "'").isEmpty()) {
            FacesUtils.addMessage(null, "error", mensaje);
            //El email ya existe
        } else {
            setFechaAlta(new Date());
            igd.add(this);
            this.login();
            System.out.println("Email no encontrado...");
        }
        return "";
    }

    @Override
    public boolean isPasswordsEquals() {
        return super.isPasswordsEquals(); //To change body of generated methods, choose Tools | Templates.
    }

}
