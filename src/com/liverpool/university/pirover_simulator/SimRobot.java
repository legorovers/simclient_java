package com.liverpool.university.pirover_simulator;

import java.io.IOException;

/**
 * SimRobot provides a Java interface to the python-based 4tronix simulator. It provides
 * similar functionality to both the python interface and the real robots.
 */
public class SimRobot {
    // class members declared as package-private to allow access
    // in ThreadedUDP class.
    float sonar_range;
    int sonar_angle;
    boolean left_line_sensor_triggered;
    boolean right_line_sensor_triggered;
    boolean ir_left_triggered;
    boolean ir_centre_triggered;
    boolean ir_right_triggered;
    int vx;
    int vth;
    String robot_name;
    private static final int PAN_SERVO = 0;
    private ThreadedUDP sim_connet;

    public SimRobot() throws IOException {
        sonar_range = 1700.0f;
        sonar_angle = 0;
        left_line_sensor_triggered = false;
        right_line_sensor_triggered = false;
        ir_left_triggered = false;
        ir_centre_triggered = false;
        ir_right_triggered = false;
        vx = 0;
        vth = 0;
        robot_name = "Not Connected";
        sim_connet = new ThreadedUDP(this);
        sim_connet.start();
    }

    /**
     * Initializes the simulator client.
     */
    public void init(){
        sim_connet.start();
    }

    /**
     * Stops the simulator client.
     */
    public void cleanup(){
        sim_connet.interrupt();
    }

    /**
     * Sets the servo to position in degrees.
     * @param servo The servo being moved (0 = pan servo)
     * @param degrees The position in degrees in the range -90 to +90
     */
    public void setServo(int servo, int degrees) {
        if (servo == PAN_SERVO) {
            sonar_angle = degrees;
        }
    }

    /**
     * Returns the distance in cm to the nearest reflecting object
     * @return distance in centimetres
     */
    public float getDistance() {
        return sonar_range;
    }

    /**
     * Returns state of Left IR Obstacle sensor.
     * @return Returns true if an obstacle is detected
     */
    public boolean irLeft() {
        return ir_left_triggered;
    }

    /**
     * Returns state of Right IR Obstacle sensor.
     * @return Returns true if an obstacle is detected
     */
    public boolean irRight() {
        return ir_right_triggered;
    }

    /**
     * Returns state of the Centre IR Obstacle sensor.
     * @return Returns true if an obstacle is detected
     */
    public boolean irCentre() {
        return ir_centre_triggered;
    }

    /**
     * Returns state of the All IR Obstacle sensors.
     * @return Returns true if an obstacle is detected
     */
    public boolean irAll() {
        if (robot_name.equals("Initio")) {
            return ir_left_triggered || ir_right_triggered;
        } else {
            return ir_left_triggered || ir_centre_triggered || ir_right_triggered;
        }
    }

    /**
     * Returns state of the Left IR Line sensor.
     * @return Returns true if a line is detected
     */
    public boolean irLeftLine() {
        return left_line_sensor_triggered;
    }

    /**
     * Returns state of the Right IR Line sensor.
     * @return Returns true if a line is detected
     */
    public boolean irRightLine() {
        return right_line_sensor_triggered;
    }

    /**
     * Sets both motors to move forward at speed.
     * @param speed Speed value 0 <= speed <= 100
     */
    public void forward(int speed) {
        vx = speed;
        vth = 0;
    }

    /**
     * Sets both motors to move backwards at speed.
     * @param speed Speed value 0 <= speed <= 100
     */
    public void reverse(int speed) {
        vx = -speed;
        vth = 0;
    }

    /**
     * Sets motors to turn opposite directions at speed.
     * @param speed Speed value 0 <= speed <= 100
     */
    public void spinLeft(int speed) {
        vx = 0;
        vth = speed;
    }

    /**
     * Sets motors to turn opposite directions at speed.
     * @param speed Speed value 0 <= speed <= 100
     */
    public void spinRight(int speed) {
        vx = 0;
        vth = -speed;
    }

    /**
     * Moves forwards in an arc by setting different speeds.
      * @param left_speed Speed value 0 <= speed <= 100
     * @param right_speed Speed value 0 <= speed <= 100
     */
    public void turnForward(int left_speed, int right_speed) {
        vx = (int) ((left_speed + right_speed) / 2.0);
        vth = right_speed - left_speed;
    }

    /**
     * Moves forwards in an arc by setting different speeds.
     * @param left_speed Speed value 0 <= speed <= 100
     * @param right_speed Speed value 0 <= speed <= 100
     */
    public void turnReverse(int left_speed, int right_speed) {
        vx = -(int) ((left_speed + right_speed) / 2.0);
        vth = right_speed - left_speed;
    }

    /**
     * Stops both motors.
     */
    public void stop() {
        vx = 0;
        vth = 0;
    }
}
