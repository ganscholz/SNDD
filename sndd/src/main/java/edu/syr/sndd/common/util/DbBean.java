/**
 * Class Name:   DbBean
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/08
 * Company:      exportSolution, Inc
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  This class is to handle the DB operation.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.util;

import edu.syr.sndd.common.framework.SysException;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DbBean {
    private Connection conn;
    
    /**
     * Get a datasource from the container then get a connection from the
     * datasource
     *
     * @throws SysException
     */
    public DbBean() throws Exception {
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/" + Env.DATASOURCE_DEFAULT);
            conn = ds.getConnection();
        } catch (Exception e) {
            throw new SysException("Problems trying to connect to database", e);
        }
    }
    
    
    /**
     * Get a resultset.
     *
     * @param argStrSQL
     *            the query string of a selection statement.
     *
     * @return rs - a resultset.
     *
     * @throws SysException:
     *             if an error occurs executing the SQL query.
     */
    public ResultSet doQuery(String argStrSQL) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(argStrSQL);
            return rs;
        } catch (Exception e) {
            throw new SysException("Error occurred in doSelect", e);
        }
    }
    
    /**
     * get the number of records after running query of input query string.
     *
     * @param argStrSQL
     *            is the query string to run
     *
     * @return the number of records, return 0 if any exception.
     */
    public int getNumberOfRecords(String argStrSQL) {
        try {
            ResultSet rs1 = doQuery(argStrSQL);
            rs1.last();
            return (rs1.getRow());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * get the number of records in the result set
     *
     * @param argRs1 is a result set
     *
     * @return the number of records, return 0 if any exception.
     */
    public int getNumberOfRecords(ResultSet argRs1) {
        try {
            argRs1.last();
            return (argRs1.getRow());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Updates the database with the query string.
     *
     * @param argStrSQL
     *            the string value of an update statement.
     *
     * @return intRowCount - the number of rows updated in the database.
     *
     * @throws SysException:
     *             if an error occurs updating the database.
     */
    public int doUpdate(String argStrSQL) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            int IntRowCount = stmt.executeUpdate(argStrSQL);
            return IntRowCount;
        } catch (Exception e) {
            throw new SysException("Error occurred in doUpdate", e);
        }
    }
    
    /**
     * Gets the status of the database connection.
     *
     * @return DBConnection - the current status of the database connection.
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * Get the next val of a sequence
     *
     * @param argStrSequenceName
     *            is a string of sequence to be queried.
     *
     * @return the next value of the sequence.
     *
     * @throws DBR-020:
     *             Error in get next value of a sequence.
     */
    public long getSequenceNextVal(String argStrSequenceName) throws Exception {
        try {
            ResultSet rs = doQuery("SELECT " + argStrSequenceName.toUpperCase()
            + ".nextVal FROM DUAL");
            rs.next();
            return (rs.getLong(1));
        } catch (Exception e) {
            throw new SysException("Error occurred in getSequenceNextVal", e);
        }
    }
    
    /**
     * Clearn the statement
     */
    public void finalize() throws Exception {
        try {
            conn.close();
        } catch (Exception e) {
        }
    }
}
