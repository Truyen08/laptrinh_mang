import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPClient {
    private static JTextArea textArea;
    private static String[] cities = {"Tokyo", "Paris", "Seoul"};
    private static int[] marketScores = new int[cities.length];

    public static void main(String[] args) {
        JFrame frame = new JFrame("UDP Client");
        textArea = new JTextArea(10, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // tránh lặp tiêu đề
        textArea.append("Time\t\tTokyo\tParis\tSeoul\n");

        new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(9877);
                byte[] receiveData = new byte[1024];
                
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    updateData(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }).start();
    }

    private static void updateData(String message) {
        String[] parts = message.split(", ");
        String time = parts[0];
        
        for (int i = 0; i < cities.length; i++) {
            marketScores[i] = Integer.parseInt(parts[i + 1].split(": ")[1]); // Lấy tỉ số
        }

        // Cập nhật dữ liệu mới
        StringBuilder dataLine = new StringBuilder(time + "\t");
        for (int i = 0; i < cities.length; i++) {
            dataLine.append(marketScores[i]).append("\t");
        }

        // Thêm dòng mới
        SwingUtilities.invokeLater(() -> textArea.append(dataLine.toString() + "\n"));
    }
}
