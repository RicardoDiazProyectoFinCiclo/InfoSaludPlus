package es.albarregas.daofactory;

import es.albarregas.dao.IGenericoDAO;
import java.io.NotSerializableException;

public abstract class DAOFactory {

    public abstract IGenericoDAO getGenericoDAO();

    public static DAOFactory getDAOFactory(){
        DAOFactory daof = null;

        daof = new MySQLDAOFactory();

        return daof;
    }

}
