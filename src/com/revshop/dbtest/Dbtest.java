package com.revshop.dbtest;


import java.sql.Connection;
import com.revshop.dbutil.DB_Connection;

public class Dbtest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        Connection conn = null;
        try {
            conn = DB_Connection.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✔ Connected successfully to Oracle 10g!");
            } else {
                System.out.println("✘ Failed to make connection!");
            }

        } catch (Exception e) {
            System.out.println("Exception occurred while testing connection:");
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Connection closed!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
