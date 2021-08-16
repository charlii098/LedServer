import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Carlos
 */
public class LEDservidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
        final int Puerto = 8000;
        try {
            servidor = new ServerSocket(Puerto);
            System.out.println("servidor iniciado");
            while (true) {
                sc = servidor.accept();//se queda a la espera de respuestas
                System.out.println("cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                String mensaje = in.readUTF();//espera mensaje
                String mensaje1 = in.readUTF();
                System.out.println(mensaje);
                System.out.println(mensaje1);

                out.writeUTF("hola mundo desde el servidor");
                sc.close();
                QuerimetodTurnLed(mensaje1,mensaje);
                System.out.println("cliente desconectado");
            }
        } catch (IOException ex) {
            Logger.getLogger(LEDservidor.class.getName()).log(Level.SEVERE, null, ex);
        }
//         QuerimetodTurnLed("ecendido","led2");
    }


    private static void  QuerimetodTurnLed(String modoR,String LedR) {
        //CONECTAR A LA BASE DE DATOS
        String url = "jdbc:mysql://192.168.1.107:3306/led";
        String user = "usuario";
        String pass = "12345";

        String modo = modoR, led = LedR;
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn != null) {
                System.out.println("Conectado a la base de datos...");
                try (Statement st = conn.createStatement()) {

                    String QuerySeletc = "Select * from led.leds;";
                    String QueryUpdate = "update led.leds set modo='" + modo + "' where nombre='" + led + "';";
                    String dia = fecha();

                    //tabla 2 Registro
                    String QueryRegistroI = "insert into Registro (nombre,dia,hora,accion) values ('"+led+"','"+dia+"',curtime(),'"+modo+"');";

                    PreparedStatement preparedStmt = conn.prepareStatement(QueryUpdate);
                    PreparedStatement preparedStmt1 = conn.prepareStatement(QueryRegistroI);

                    //ejecutar consulta
                    preparedStmt.executeUpdate();
                    preparedStmt1.execute();
                    //ResultSet Reg = st.executeQuery(QueryRegistroI);

                    ResultSet rs = st.executeQuery(QuerySeletc);
                    while (rs.next()) {

                        String nombre = rs.getString("nombre");
                        String modo1 = rs.getString("modo");

                        //imprmir datos
                        System.out.format("%s %s\n",nombre,modo1);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {

                System.out.println("no me pude conectar");

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static String fecha(){
        LocalDateTime fecha = LocalDateTime.now();
       String dia = fecha.getDayOfWeek().toString();

       String fecha1 = "";

        if (dia == "MONDAY") {
            String Lunes = "Lunes";
            fecha1 = Lunes;
            System.out.println(Lunes);
        }
        if (dia == "THUESDAY") {
            String Martes = "Martes";
            fecha1 = Martes;
            System.out.println(Martes);
        }
        if (dia == "WEDNESDAY") {
            String Miercoles = "Miercoles";
            fecha1 = Miercoles;
            System.out.println(Miercoles);
        }
        if (dia == "THURSDAY") {
            String Jueves = "Jueves";
            fecha1 = Jueves;
            System.out.println(Jueves);
        }
        if (dia == "FRIDAY") {
            String Viernes = "Viernes";
            fecha1 = Viernes;
            System.out.println("Viernes");
        }
        if (dia == "SATURDAY") {
            String Sabado = "Sabado";
            fecha1 = Sabado;
            System.out.println("Sabado");
        }
        if (dia == "SUNDAY") {
            String Domingo = "Domingo";
            fecha1 = Domingo;
            System.out.println("Domingo");

        }

        return fecha1;
    }

}


