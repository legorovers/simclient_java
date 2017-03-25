package com.liverpool.university.pirover_examples;

import com.liverpool.university.pirover_simulator.SimRobot;

import java.io.IOException;

public class IRAvoider {

    public static void main(String[] args) {
        int speed = 60;
        int turn_speed = 50;
        try {
            SimRobot robot = new SimRobot();
            while (true) {
                System.out.println(robot.irLeft() + " " + robot.irRight());
                if (robot.irAll()) {
                    if (robot.irLeft()) {
                        robot.spinRight(turn_speed);
                    } else if (robot.irRight()) {
                        robot.spinLeft(turn_speed);
                    }
                    while (robot.irAll()) {
                        Thread.sleep(500);
                    }

                } else {
                    robot.forward(speed);
                }

                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }
}
