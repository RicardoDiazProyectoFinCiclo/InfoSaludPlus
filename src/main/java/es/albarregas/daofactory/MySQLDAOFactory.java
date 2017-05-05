package es.albarregas.daofactory;

import es.albarregas.dao.GenericoDAO;
import es.albarregas.dao.IGenericoDAO;
import java.io.Serializable;

public class MySQLDAOFactory extends DAOFactory implements Serializable
{

    @Override
    public IGenericoDAO getGenericoDAO() {
        return new GenericoDAO();
    }

}
