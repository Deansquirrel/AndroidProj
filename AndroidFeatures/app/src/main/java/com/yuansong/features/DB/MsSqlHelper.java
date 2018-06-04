package com.yuansong.features.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class MsSqlHelper {

    private final String DIRVER = "net.sourceforge.jtds.jdbc.Driver";

    private String mConnStr;
    private String mServer;
    private String mPort;
    private String mDbName;
    private String mUser;
    private String mPwd;

    private Connection mConn;
    private PreparedStatement mPs;

    public MsSqlHelper(String server,String port, String dbName,String user,String pwd){
        this.mServer = server;
        this.mPort = port;
        this.mDbName = dbName;
        this.mConnStr = "jdbc:jtds:sqlserver://" + this.mServer + ":" + this.mPort + "/" + this.mDbName;
        this.mUser = user;
        this.mPwd = pwd;

        try{
            Class.forName(DIRVER);
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public int ExecuteNonQuery(String sql, List<Object> params) {
        try{
            mConn = DriverManager.getConnection(mConnStr,mUser,mPwd);
            mPs = mConn.prepareStatement(sql);
            if(params != null && !params.equals("")){
                for(int i=0;i<params.size();i++){
                    mPs.setObject(i + 1,params.get(i));
                }
            }
            return mPs.executeUpdate();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
        finally {
            try{
                mPs.close();
                mConn.close();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

//         39     public int ExecuteNonQuery(String sql, List<Object> params) {
//        40         try {
//            41             con = DriverManager.getConnection(this.connStr, this.userName, this.userPwd);
//            42             pstm = con.prepareStatement(sql);
//            43             if (params != null && !params.equals("")) {
//                44                 for (int i = 0; i < params.size(); i++) {
//                    45                     pstm.setObject(i + 1, params.get(i));
//                    46                 }
//                47             }
//            48             return pstm.executeUpdate();
//            49         } catch (Exception e) {
//            50             // TODO: handle exception
//            51             e.printStackTrace();
//            52             return -1;
//            53         } finally {
//            54             try {
//                55                 pstm.close();
//                56                 con.close();
//                57             } catch (SQLException e) {
//                58                 // TODO Auto-generated catch block
//                59                 e.printStackTrace();
//                60             }
//            61         }
//        62     }
// 63
//         64     public String ExecuteQuery(String sql, List<Object> params) {
//        65         // TODO Auto-generated method stub
//        66         JSONArray jsonArray = new JSONArray();
//        67         try {
//            68             con = DriverManager.getConnection(this.connStr, this.userName, this.userPwd);
//            69             pstm = con.prepareStatement(sql);
//            70             if (params != null && !params.equals("")) {
//                71                 for (int i = 0; i < params.size(); i++) {
//                    72                     pstm.setObject(i + 1, params.get(i));
//                    73                 }
//                74             }
//            75             ResultSet rs = pstm.executeQuery();
//            76             ResultSetMetaData rsMetaData = rs.getMetaData();
//            77             while (rs.next()) {
//                78                 JSONObject jsonObject = new JSONObject();
//                79                 for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
//                    80                     String columnName = rsMetaData.getColumnLabel(i + 1);
//                    81                     String value = rs.getString(columnName);
//                    82                     jsonObject.put(columnName, value);
//                    83                 }
//                84                 jsonArray.put(jsonObject);
//                85             }
//            86             return jsonArray.toString();
//            87         } catch (Exception e) {
//            88             // TODO: handle exception
//            89             return null;
//            90         } finally {
//            91             try {
//                92                 pstm.close();
//                93                 con.close();
//                94             } catch (SQLException e) {
//                95                 // TODO Auto-generated catch block
//                96                 e.printStackTrace();
//                97             }
//            98         }
//        99     }
//100 }
//}
