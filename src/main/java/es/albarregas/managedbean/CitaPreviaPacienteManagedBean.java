/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Cita;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.Constantes;
import es.albarregas.util.FacesUtils;
import es.albarregas.util.UtilidadesFechas;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "citaPreviaPacienteBean")
public class CitaPreviaPacienteManagedBean implements Serializable, Cloneable {

    private Integer[] horas;
    private Integer[] minutos;
    private int horaBusqueda;
    private int minutoBusqueda;
    private Date fechaBusqueda;
    private String tipoCita = "administrativa";
    private int siguienteHora;
    private int siguienteMinuto;
    private Usuario usuario;
    private Medico medico;
    private Paciente paciente;
    private Centro centro;
    private List<Cita> listHuecos;
    private UIDataTable tablaHuecos;
    private int indiceMinutos;
    private int indiceHoras;
    private boolean indImprimir;
    private Cita citaElegida;
    DAOFactory df;
    IGenericoDAO igd;

    public int getHoraBusqueda() {
        return horaBusqueda;
    }

    public void setHoraBusqueda(int horaBusqueda) {
        this.horaBusqueda = horaBusqueda;
    }

    public int getMinutoBusqueda() {
        return minutoBusqueda;
    }

    public void setMinutoBusqueda(int minutoBusqueda) {
        this.minutoBusqueda = minutoBusqueda;
    }

    public Integer[] getHoras() {
        return horas;
    }

    public void setHoras(Integer[] horas) {
        this.horas = horas;
    }

    public Integer[] getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer[] minutos) {
        this.minutos = minutos;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public Date getFechaBusqueda() {
        return fechaBusqueda;
    }

    public void setFechaBusqueda(Date fechaBusqueda) {
        this.fechaBusqueda = fechaBusqueda;
    }

    public List<Cita> getListHuecos() {
        return listHuecos;
    }

    public void setListHuecos(List<Cita> listHuecos) {
        this.listHuecos = listHuecos;
    }

    public UIDataTable getTablaHuecos() {
        return tablaHuecos;
    }

    public void setTablaHuecos(UIDataTable tablaHuecos) {
        this.tablaHuecos = tablaHuecos;
    }

    public Cita getCitaElegida() {
        return citaElegida;
    }

    public void setCitaElegida(Cita citaElegida) {
        this.citaElegida = citaElegida;
    }

    public boolean isIndImprimir() {
        return indImprimir;
    }

    public void setIndImprimir(boolean indImprimir) {
        this.indImprimir = indImprimir;
    }
    
    

    @PostConstruct
    public void init() {
        horas = new Integer[]{8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        minutos = new Integer[]{0, 15, 30, 45};
        fechaBusqueda = new Date();
        this.listHuecos = new ArrayList<Cita>();
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        if (FacesUtils.getSession("usuario") != null) {
            usuario = (Usuario) FacesUtils.getSession("usuario");
            paciente = (Paciente) igd.getOne(usuario.getId(), Paciente.class);
        }
    }

    /**
     * Método para generar un List con las proximas 10 citas disponibles en base
     * a la fecha y hora de búsqueda
     *
     * @return
     */
    public String pedirCitaPrevia() {
        
        if (paciente.getMedico() != null) {
            medico = paciente.getMedico();
        } else {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Debe elegir un médico en Inicio Area Paciente");
            return "";
        }

        if (paciente.getCentro() != null) {
            centro = paciente.getCentro();
        } else {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Debe elegir un centro en Inicio Area Paciente");
            return "";
        }

        //Establecemos los datos de la fecha de busqueda 
        fechaBusqueda = UtilidadesFechas.getDateConFechaYHora(fechaBusqueda, horaBusqueda, minutoBusqueda);

        this.setListHuecos(new ArrayList<Cita>());
        //Mostramos las 10 siguientes posibles citas a elegir
        this.setListHuecos(getObtenerListHuecos(fechaBusqueda, Constantes.NUM_MAX_POSIBLES_CITAS));

        return "";
    }

    /**
     * Método para obtener el índice de la hora seleccionada en el array
     *
     * @param hora
     * @return
     */
    public int getIndiceHoraSeleccionada(int hora) {

        for (int i = 0; i < horas.length; i++) {
            if (hora == horas[i]) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Método para obtener el indice del minuto seleccionado en el array
     *
     * @param minuto
     * @return
     */
    public int getIndiceMinutoSeleccionado(int minuto) {

        for (int i = 0; i < minutos.length; i++) {
            if (minuto == minutos[i]) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Para obtener el listado de posibles citas a elegir por el paciente,
     * pasandole la fecha y el número de posibles citas que le vayamos a dar a
     * elegir al paciente
     *
     * @param fecha
     * @param numPosiblesCitas
     * @return
     */
    private List getObtenerListHuecos(Date fecha, Integer numPosiblesCitas) {

        List<Cita> lista = new ArrayList();
        indiceMinutos = getIndiceMinutoSeleccionado(minutoBusqueda);
        indiceHoras = getIndiceHoraSeleccionada(horaBusqueda);
        int contador = numPosiblesCitas;
        Date huecoOk = fechaBusqueda;
        Cita cita = null;
        huecoOk = UtilidadesFechas.getDateConFechaYHora(huecoOk, horaBusqueda, minutoBusqueda);
        while (contador > 0) {

            SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Cita> citasObtenidas = igd.get("Cita Where fecha = '" + smd.format(huecoOk) + "'"); //añadir medico

            if (citasObtenidas.isEmpty()) {
                cita = new Cita();
                cita.setFecha(huecoOk);
                cita.setPaciente(paciente);
                cita.setMedico(medico);
                cita.setCentro(centro);
                cita.setTipo(tipoCita);
                lista.add(cita);
                contador--;
            }
            //adelantar +1 la cita
            huecoOk = getSiguienteHueco(huecoOk);
        }

        return lista;
    }

    /**
     * Método para obtener la siguiente hora de cita a la que le pasemos.
     * Tenemos que tener en cuenta que si los minutos no cumplen un ciclo, la
     * hora pasada será la misma a la retornada
     *
     * @param fecha
     * @param indiceHoras
     * @param indiceMinutos
     * @return
     */
    private Date getSiguienteHueco(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);


        if (indiceMinutos < (minutos.length - 1)) {
            indiceMinutos++;
        } else if (indiceHoras < (horas.length - 1)) {
            indiceMinutos = 0;
            indiceHoras++;
        } else {
            //Añadimos un día
            if (cal.get(Calendar.DAY_OF_WEEK) == 6) {
                //Si cae en viernes tendremos que añadir 3 para pasar a Lunes
                cal.add(Calendar.DATE, 3);
            } else {
                cal.add(Calendar.DATE, 1);
            }
            indiceMinutos = 0;
            indiceHoras = 0;
        }
        
        
        cal.set(Calendar.HOUR_OF_DAY, horas[indiceHoras]);
        cal.set(Calendar.MINUTE, minutos[indiceMinutos]);

        return cal.getTime();
    }

    public String seleccionarHueco() throws Exception {
        indImprimir = false;
        citaElegida = (Cita) tablaHuecos.getRowData();
        return "";
    }

    public String confirmarCita() {
        try{
            igd.add(citaElegida);
            FacesUtils.addMessage("formDatosCita", "info", "La cita ha sido reservada correctamente.\nPuede imprimirla en PDF.");
            indImprimir = true;
        }catch(Exception e){
            FacesUtils.addMessage("formDatosCita", "error", "Error al reservar la cita. Cierre y vuelva a intentarlo.");
            indImprimir = false;
            e.printStackTrace();
        }
        return "";
    }

}
