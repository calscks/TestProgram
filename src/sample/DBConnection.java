package sample;

import java.sql.*;

public class DBConnection {
    private static DBConnection singleInstance;
    private Connection connection;
    private PreparedStatement statement;

    // prevent more than one instance caused by concurrency
    public static DBConnection getInstance() {
        if (singleInstance == null)
            synchronized (DBConnection.class) {
                if (singleInstance == null)
                    singleInstance = new DBConnection();
            }
        return singleInstance;
    }

    // prevent more than one instance caused by reflection API
    private DBConnection() {
        if (singleInstance != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");

        try {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection("jdbc:sqlserver://maersk-cms-server.database.windows.net:1433;database=maersk-cmsdb;user=serveradmin@maersk-cms-server;password=Ddac2018;");
            System.out.println("connection established.");
        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type {@link EType} enumeration
     * @param sql SQL Statement to be executed
     * @return returns ResultSet whenever possible. Note that nothing will be returned if {@code EType.UPDATE} is used.
     */
    public ResultSet execute(EType type , String sql){
        try {
            statement = connection.prepareStatement(sql);
            if (type ==EType.QUERY)
                return statement.executeQuery();
            else if (type == EType.UPDATE){
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeStatement(){
        if (statement != null) {
            try {
                statement.close();
                System.out.println("Statement is closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeAll(){
        closeStatement();
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
