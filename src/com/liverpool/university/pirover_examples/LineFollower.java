package com.liverpool.university.pirover_examples;
import com.liverpool.university.pirover_simulator.SimRobot;

import java.io.IOException;

public class LineFollower {
    public static void main(String [] args)
    {
        int speed = 20;
        int turn_speed = 10;
        try{
            SimRobot robot = new SimRobot();
            while (true){
                if (robot.irLeftLine()){
                    robot.turnForward(speed-turn_speed, speed+turn_speed);
                    Thread.sleep(1000);
                } else if (robot.irRightLine()){
                    robot.turnForward(speed+turn_speed, speed-turn_speed);
                    Thread.sleep(100);
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
