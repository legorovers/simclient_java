package com.liverpool.university.pirover_examples;
import com.liverpool.university.pirover_simulator.SimRobot;

import java.io.IOException;

public class SonarAvoider {

    public static void main(String [] args)
    {
        int speed = 60;
        int turn_speed = 50;
        float stop_range = 100.0f;
        int sonar_wait = 1500;
        try{
            SimRobot robot = new SimRobot();
            while (true){
                if (robot.getDistance() < stop_range){
                    robot.stop();
                    System.out.println("Range less than " + stop_range);
                    float left, right = 0.f;

                    robot.setServo(0, -70);
                    Thread.sleep(sonar_wait);
                    right = robot.getDistance();

                    robot.setServo(0, 70);
                    Thread.sleep(sonar_wait);
                    left = robot.getDistance();

                    robot.setServo(0, 0);
                    Thread.sleep(sonar_wait);
                    if (left > right){
                        robot.spinLeft(turn_speed);
                    } else if (right > left) {
                        robot.spinRight(turn_speed);
                    } else {
                        robot.spinRight(turn_speed + 20);
                    }
                    while (true){
                        Thread.sleep(250);
                        if (robot.getDistance() > stop_range)
                            break;
                    }
                    robot.stop();
                } else {
                    robot.forward(speed);
                }
                Thread.sleep(100);
            }
        } catch (IOException|InterruptedException e){
            System.out.println(e.getMessage());
        }

    }
}
