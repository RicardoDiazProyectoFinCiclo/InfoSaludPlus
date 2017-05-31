/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Servicio;
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
@ManagedBean(name = "servicioBean")
public class ServicioManagedBean implements Serializable {

    private int aniadirModificar;
    private int contadorListServicios;
    private List<Servicio> listServicios;
    private Servicio servicio;
    private UIDataTable panelServicio;
    private DAOFactory df;
    private IGenericoDAO igd;

    public int getAniadirModificar() {
        return aniadirModificar;
    }

    public void setAniadirModificar(int aniadirModificar) {
        this.aniadirModificar = aniadirModificar;
    }

    public int getContadorListServicios() {
        return contadorListServicios;
    }

    public void setContadorListServicios(int contadorListServicios) {
        this.contadorListServicios = contadorListServicios;
    }

    public List<Servicio> getListServicios() {
        return listServicios;
    }

    public void setListServicios(List<Servicio> listServicios) {
        this.listServicios = listServicios;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public UIDataTable getPanelServicio() {
        return panelServicio;
    }

    public void setPanelServicio(UIDataTable panelServicio) {
        this.panelServicio = panelServicio;
    }
    

    @PostConstruct
    public void init() {
        try {
            aniadirModificar = 0;

            df = DAOFactory.getDAOFactory();
            igd = df.getGenericoDAO();

            servicio = new Servicio();
            listServicios = igd.get("Servicio");
            contadorListServicios = listServicios.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String levantarModalAniadir() {
        init();

        return "";
    }

    public String levantarModalModificar() {
        init();
        System.out.println("Levantando modal servicios");
        try {
            aniadirModificar = 1;
            servicio = (Servicio) panelServicio.getRowData();

            FacesUtils.addSession("servicioSeleccionado", servicio);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String aniadirServicio() {

        try {
            igd.add(servicio);
            listServicios.add(servicio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public String modificarServicio() {

        try {
            igd.update(servicio);
            //Es más rápido sacarlos de la base de datos que modificar el actualizado.
            listServicios = igd.get("Servicio");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
