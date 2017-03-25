package com.liverpool.university.pirover_examples;
import com.liverpool.university.pirover_simulator.SimRobot;

import java.io.IOException;


public class MotorTest
{
    public static void main(String [] args)
    {
        int speed = 100;
        try{
            SimRobot robot = new SimRobot();
            while (true){
                robot.forward(speed);
                Thread.sleep(3000);
                robot.reverse(speed);
                Thread.sleep(3000);
                robot.spinRight(speed);
                Thread.sleep(3000);
                robot.spinLeft(speed);
                Thread.sleep(3000);
                robot.stop();
                Thread.sleep(3000);
            }
        } catch (IOException|InterruptedException e){
            System.out.println(e.getMessage());
        }

    }

}