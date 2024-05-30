package elektronik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Mainscreen extends JFrame{

    private static final String URL = "jdbc:mysql://localhost:3306/barang_elektronik";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JPanel panelMain;
    private JTable jTableBarang;
    private JTextField txtStok;
    private JTextField txtHarga;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtFilter;
    private JButton btnFilter;
    private JTextField txtNama;

    private DefaultTableModel defaultTableModel = new DefaultTableModel();

    public Mainscreen(){
        super("Data Barang Elektronik");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();

        refreshTable(getBarang());
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Nama = txtNama.getText();
                String Stok = txtStok.getText();
                Integer Harga = Integer.parseInt(txtHarga.getText());

                Barang barang = new Barang();
                barang.setNama(Nama);
                barang.setStok(Stok);
                barang.setHarga(Harga);

                clearForm();
                insertBarang(barang);
                refreshTable(getBarang());
            }
        });

        jTableBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int row = jTableBarang.getSelectedRow();
                System.out.println(row);

                String Nama = jTableBarang.getValueAt(row,0).toString();
                String Stok = jTableBarang.getValueAt(row,1).toString();
                String Harga = jTableBarang.getValueAt(row,2).toString();

                txtNama.setText(Nama);
                txtStok.setText(Stok);
                txtHarga.setText(Harga);
            }
        });


        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Nama = txtNama.getText();
                String Stok = txtStok.getText();
                Integer Harga = Integer.parseInt(txtHarga.getText());

                Barang barang = new Barang();
                barang.setNama(Nama);
                barang.setStok(Stok);
                barang.setHarga(Harga);

                clearForm();
                updateBarang(barang);
                refreshTable(getBarang());
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Nama = txtNama.getText();

                clearForm();
                deleteBarang(Nama);
                refreshTable(getBarang());
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Nama = txtNama.getText();
                refreshTable(filterBarang(Nama));
            }
        });
    }

    private static List<Barang> filterBarang(String filterNama){
        List<Barang> arrayListbarang = new ArrayList<>();

        ResultSet resultSet = executeQuery("select * from barang where Nama like '%" + filterNama + "%' ");

        try{
            while (resultSet.next()){
                String Stok = resultSet.getString("stok");
                String Nama = resultSet.getString("nama");
                Integer Harga = Integer.parseInt(resultSet.getString("harga"));

                Barang barang = new Barang();
                barang.setStok(Stok);
                barang.setNama(Nama);
                barang.setHarga(Harga);

                arrayListbarang.add(barang);
            }
        }

        catch (Exception e){
            return null;
        }
        return arrayListbarang;
    }

    public void refreshTable(List<Barang> arrayListbarang){
        Object [][] data = new Object [arrayListbarang.size()][3];

        for (int i = 0; i < arrayListbarang.size(); i++){
            data[i] = new Object[]{
                    arrayListbarang.get(i).getNama(),
                    arrayListbarang.get(i).getStok(),
                    arrayListbarang.get(i).getHarga()
            };
        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[] {"Nama", "Stok", "Harga"}
        );

        jTableBarang.setModel(defaultTableModel);
    }

    private static ResultSet executeQuery(String query){
        try {
            Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }

        catch (Exception e){
            return null;
        }
    }

    public static List<Barang> getBarang(){
        List<Barang> arrayListBarang = new ArrayList<>();
        ResultSet resultSet = executeQuery("select * from barang");

        try {
            while (resultSet.next()){
                String nama = resultSet.getString("nama");
                String stok = resultSet.getString("stok");
                Integer harga = Integer.parseInt(resultSet.getString("harga"));

                System.out.println(nama);
                System.out.println(stok);
                System.out.println(harga);
                System.out.println();

                Barang barang = new Barang();
                barang.setHarga(harga);
                barang.setStok(stok);
                barang.setNama(nama);
                arrayListBarang.add(barang);
            }
        }

        catch (Exception e){

        }
        return arrayListBarang;
    }
    public static void executeSql(String sql){
        try{
            Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
        }

        catch (Exception e){

        }
    }


    private static void insertBarang(Barang barang){
        String sql = "insert into barang values (" +
                "'" + barang.getNama() + "'," +
                "'" + barang.getStok() + "'," +
                "'" + barang.getHarga() + "')";
        executeSql(sql);
    }

    private static void updateBarang(Barang barang){
        String sql = "update barang set " +
                "Nama = '"+ barang.getNama() +"', " +
                "Stok = '"+ barang.getStok() +"' " +
                "where Harga = '"+ barang.getHarga() +"'";
        executeSql(sql);
    }
    private static void deleteBarang(String Nama) {
        String sql = "DELETE FROM barang WHERE Nama = '" + Nama + "'";
        executeSql(sql);
    }

    private void clearForm(){
        txtNama.setText("");
        txtStok.setText("");
        txtHarga.setText("");
    }

    public static void main(String[] args) {
        Mainscreen mainscreen = new Mainscreen();
        mainscreen.setVisible(true);
    }
}
