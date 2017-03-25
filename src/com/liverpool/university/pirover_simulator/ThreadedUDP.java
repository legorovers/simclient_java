package com.liverpool.university.pirover_simulator;
import java.io.*;
import java.net.*;

/**
 * Creates a daemon thread to handle communication between the simulator and client. Communication
 * is done using text strings passed via UDP socket.
 *
 */
public class ThreadedUDP extends Thread {
    private DatagramSocket data_sock;
    private DatagramSocket command_sock;
    private SimRobot robot;
    private static final int DATA_PORT = 5000;
    private static final int COMMAND_PORT = 5001;
    private static final String HOSTNAME = "127.0.0.1";

    public ThreadedUDP(SimRobot robot) throws IOException {
        this.robot = robot;
        data_sock = new DatagramSocket(new InetSocketAddress(HOSTNAME, DATA_PORT));
        command_sock = new DatagramSocket();
        setDaemon(true);
    }

    public void run(){
        byte[] buffer = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        while (!this.isInterrupted()){
            try{
                data_sock.receive(incoming);
                byte[] data = incoming.getData();
                String msg = new String(data, 0, incoming.getLength());
                this.updateRobotFromMessage(msg);
                String command = getCommandString();
                DatagramPacket dp = new DatagramPacket(command.getBytes() , command.getBytes().length,
                        new InetSocketAddress("127.0.0.1", COMMAND_PORT));
                command_sock.send(dp);

            } catch (IOException e){
                System.out.println("Error communicating with simulator: " + e.getMessage());
            }
        }
    }

    /**
     * Create a command string to send to the robot.
     *
     * Commands for the initio robot take the form:
     *
     *      <<LINEAR_VELOCITY;ANGULAR_VELOCITY;SONAR_SERVO_ANGLE>>
     *
     * Commands for the pi2go robot take the form:
     *
     *      <<LINEAR_VELOCITY;ANGULAR_VELOCITY>>
     *
     * @return The relevant command string with values.
     */
    private String getCommandString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<<");
        if (robot.robot_name.equals("INITIO")){
            sb.append(robot.vx);
            sb.append(";");
            sb.append(robot.vth);
            sb.append(";");
            sb.append(robot.sonar_angle);
        } else if (robot.robot_name.equals("PI2GO")){
            sb.append(robot.vx);
            sb.append(";");
            sb.append(robot.vth);
        }
        sb.append(">>");
        return sb.toString();
    }

    /**
     * Takes a robot state message received from the simulator and updates the SimRobot object as necessary.
     *
     * State strings for the initio take the form:
     *
     *      <<ROBOT_NAME;SONAR_RANGE;LEFT_LINE;RIGHT_LINE;LEFT_IR;RIGHT_IR>>
     *
     * State strings for the pi2go take the form:
     *
     *      <<ROBOT_NAME;SONAR_RANGE;LEFT_LINE;RIGHT_LINE;LEFT_IR;MIDDLE_IR;RIGHT_IR>>
     *
     *
     * @param msg The state string from the simulator.
     */
    private void updateRobotFromMessage(String msg){
        if (msg.startsWith("<<") && msg.endsWith(">>")){
            msg = msg.replace("<<", "");
            msg = msg.replace(">>", "");
            //System.out.println(msg);
            String[] values_arr = msg.split(";");
            //System.out.println(values_arr.length);
            if (values_arr.length == 6) {
                robot.robot_name = values_arr[0];
                robot.sonar_range = Float.valueOf(values_arr[1]);
                robot.left_line_sensor_triggered = Integer.parseInt(values_arr[2]) == 1;
                robot.right_line_sensor_triggered = Integer.parseInt(values_arr[3]) == 1;
                robot.ir_left_triggered = Integer.parseInt(values_arr[4]) == 1;
                robot.ir_right_triggered = Integer.parseInt(values_arr[5]) == 1;
            } else if (values_arr.length == 7){
                robot.robot_name = values_arr[0];
                robot.sonar_range = Float.valueOf(values_arr[1]);
                robot.left_line_sensor_triggered = Integer.parseInt(values_arr[2]) == 1;
                robot.right_line_sensor_triggered = Integer.parseInt(values_arr[3]) == 1;
                robot.ir_left_triggered = Integer.parseInt(values_arr[4]) == 1;
                robot.ir_centre_triggered = Integer.parseInt(values_arr[5]) == 1;
                robot.ir_right_triggered = Integer.parseInt(values_arr[6]) == 1;
            }
        }
    }
}
