package miner.store;

import miner.spider.utils.MysqlUtil;
import miner.utils.MySysLogger;
import miner.utils.PlatformParas;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    private static MySysLogger logger = new MySysLogger(CreateTable.class);

    private static Configuration configuration = null;
    static{
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", PlatformParas.hbase_zookeeper_host);
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.rootdir","hdfs://master:8020/hbase");
        configuration.set("hbase.master", "hdfs://master:60000");
    }
    public static void main(String args[]) throws SQLException{

        //new CreateTable().mysqlCheck("1","1");
        new CreateTable().createTable(configuration, "cutoutsy", true);

    }

    public static void mysqlCheck(String tableWid,String tablePid) throws SQLException{
        boolean flag = true;
        Connection con = MysqlUtil.getConnection();
        Statement sta = con.createStatement();
        String sql = "select * from data order by id desc";
        ResultSet rs = sta.executeQuery(sql);
        while(rs.next()){
        String wid = rs.getString("wid");
        String pid = rs.getString("pid");
            if(wid.equals(tableWid)&&pid.equals(tablePid)){
                String tid = rs.getString("tid");
                String dataid = rs.getString("dataid");
                String processWay = rs.getString("processWay");
                String foreignkey = rs.getString("foreignkey");
                 if(foreignkey.equals("none")){
                     flag = false;
                    }
                // 在hbase创建data处理方式为s的相应的表
                if(processWay.equals("s") || processWay.equals("S")) {
                    String tablename = wid + pid + tid + dataid;
                    createTable(configuration, tablename, flag);
                }
            }else{
                break;
            }
    }
    }

    public static void createTable(Configuration conf,String tableName,boolean flag){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(conf);
            if(admin.tableExists(tableName)){
                System.err.println(tableName+"is exist and please check it");
            }else{
                HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                tableDescriptor.addFamily(new HColumnDescriptor("property"));
                tableDescriptor.addFamily(new HColumnDescriptor("link"));
                if(flag){
                    tableDescriptor.addFamily(new HColumnDescriptor("foreign"));
                }
                admin.createTable(tableDescriptor);
                System.out.println("end create table");
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
            logger.error("Master Not Running "+ MySysLogger.formatException(e));
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
            logger.error("Zookeeper Connect Exception " + MySysLogger.formatException(e));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception"+ MySysLogger.formatException(e));
        }
    }

    public static void createTable(String tableName,boolean flag){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(configuration);
            if(admin.tableExists(tableName)){
                System.err.println(tableName+"is exist and please check it");
            }else{
                HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                tableDescriptor.addFamily(new HColumnDescriptor("property"));
                tableDescriptor.addFamily(new HColumnDescriptor("link"));
                if(flag){
                    tableDescriptor.addFamily(new HColumnDescriptor("foreign"));
                }
                admin.createTable(tableDescriptor);
                System.out.println("end create table");
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
            logger.error("Master Not Running "+ MySysLogger.formatException(e));
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
            logger.error("Zookeeper Connect Exception " + MySysLogger.formatException(e));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception"+ MySysLogger.formatException(e));
        }
    }

    public static void initHbaseTable() throws SQLException{
        boolean flag = true;
        Connection con = MysqlUtil.getConnection();
        Statement sta = con.createStatement();
        String sql = "select * from data";
        ResultSet rs = sta.executeQuery(sql);
        while(rs.next()){
            String wid = rs.getString("wid");
            String pid = rs.getString("pid");
            String tid = rs.getString("tid");
            String dataid = rs.getString("dataid");
            String foreignkey = rs.getString("foreignkey");
            if(foreignkey.equals("none")){
                flag = false;
            }
            String tablename = wid+pid+tid+dataid;
            createTable(configuration, tablename, flag);
        }
    }

}

