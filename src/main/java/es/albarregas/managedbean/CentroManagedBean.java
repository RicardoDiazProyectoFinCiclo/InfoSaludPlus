/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Direccion;
import es.albarregas.modelo.Provincia;
import es.albarregas.modelo.Pueblo;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "centroBean")
public class CentroManagedBean implements Serializable, Cloneable {

    private Centro centro;
    private List<Centro> listCentros;
    private int contadorListCentros;
    private DAOFactory df;
    private IGenericoDAO igd;
    private UIDataTable panelCentro;
    private Integer aniadirModificar;
    private Direccion direccion;
    private Pueblo pueblo;
    private Provincia provincia;
    private List<Pueblo> listPueblos;
    private int pagina;

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public List<Centro> getListCentros() {
        return listCentros;
    }

    public void setListCentros(List<Centro> listCentros) {
        this.listCentros = listCentros;
    }

    public int getContadorListCentros() {
        return contadorListCentros;
    }

    public void setContadorListCentros(int contadorListCentros) {
        this.contadorListCentros = contadorListCentros;
    }

    public UIDataTable getPanelCentro() {
        return panelCentro;
    }

    public void setPanelCentro(UIDataTable panelCentro) {
        this.panelCentro = panelCentro;
    }

    public Integer getAniadirModificar() {
        return aniadirModificar;
    }

    public void setAniadirModificar(Integer aniadirModificar) {
        this.aniadirModificar = aniadirModificar;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Pueblo getPueblo() {
        return pueblo;
    }

    public void setPueblo(Pueblo pueblo) {
        this.pueblo = pueblo;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Pueblo> getListPueblos() {
        return listPueblos;
    }

    public void setListPueblos(List<Pueblo> listPueblos) {
        this.listPueblos = listPueblos;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    
    
    @PostConstruct
    public void init() {
        try {
            aniadirModificar = 0;

            df = DAOFactory.getDAOFactory();
            igd = df.getGenericoDAO();

            centro = new Centro();
            listCentros = igd.get("Centro");
            contadorListCentros = listCentros.size();

            direccion = new Direccion();
            pueblo = new Pueblo();
            provincia = new Provincia();
            this.setListPueblos(igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String levantarModalAniadir() {
        //Hay que resetear todo al levantar el aniadir, por tanto el init() es el método indicado
        init();

        return null;
    }

    public String levantarModalModificar() {
        init();
        System.out.println("Levantando modal centros");
        try {
            aniadirModificar = 1;
            centro = (Centro) panelCentro.getRowData();
            if (centro.getDireccion() != null) {
                this.setDireccion(centro.getDireccion());
            } else {
                this.setDireccion(new Direccion());
            }

            if (direccion.getPueblo() != null) {
                this.setPueblo(this.getDireccion().getPueblo());
                listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
            } else {
                this.setPueblo(new Pueblo());
                listPueblos = null;
            }

            if (pueblo.getProvincia() != null) {
                this.setProvincia(this.getPueblo().getProvincia());
            } else {
                this.setProvincia(new Provincia());
            }
            
            FacesUtils.addSession("centroSeleccionado", centro);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String aniadirCentro() {
        try {
            pueblo.setProvincia(provincia);
            direccion.setPueblo(pueblo);
            centro.setDireccion(direccion);
            igd.add(centro);
            listCentros.add(centro);
            contadorListCentros++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public String modificarCentro() {
        try {
            pueblo.setProvincia(provincia);
            direccion.setPueblo(pueblo);
            centro.setDireccion(direccion);
            igd.update(centro);
            //Es más rápido sacarlos de la base de datos que modificar el actualizado.
            listCentros = igd.get("Centro");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Función escuchador del codigo postal, para cambiar provincia y pueblo en
     * función del código postal introducido
     */
    public void escuchadorCP() {
        try {
            if (this.getPueblo().getCodigoPostal().trim().length() == 5) {
                listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
                if (!listPueblos.isEmpty()) {
                    this.provincia = (Provincia) igd.getOne(listPueblos.get(0).getProvincia().getId(), provincia.getClass());
                } else {
                    this.provincia.setNombre("");
                }
            } else {
                listPueblos = null;
                provincia.setNombre("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
