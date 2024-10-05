import java.net.*;
import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);
	    System.out.println("Server is running...");
            Random random = new Random();
            String[] cities = {"Tokyo", "Paris", "Seoul"};
            while (true) {
                // Tạo thời gian
                String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                
                // tỉ số thị trường ngẫu nhiên
                StringBuilder message = new StringBuilder("Thời gian: " + time);
                for (String city : cities) {
                    int marketScore = random.nextInt(1000) + 1; // Tỉ số từ 1 đến 1000
                    message.append(", ").append(city).append(": ").append(marketScore);
                }

                // Gửi dữ liệu đến client
                byte[] sendData = message.toString().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 9877);
                socket.send(sendPacket);
                
                // delay 1s
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
