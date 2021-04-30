package main.java.com.example.simulationlab17;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class Listener {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final double[] intensities = new double[3];
    private static final int[] combinedProbabilities = new int[] {1, 1};
    private static final int[] sumProbabilities = new int[] {1, 1};
    private static final double[] eventTimes = new double[3];
    private static final int[] eventNumber = new int[] { 0, 0, 0, 0 };
    private static final double[] eventLastTime = new double[] {getTime(), getTime()};

    private static double exponentialRV(double intensity) {
        return -Math.log(random.nextDouble()) / intensity;
    }

    private static double getTime() {
        return (double) System.currentTimeMillis() / 1000.0;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter intensity of first process:");
        intensities[0] = input.nextDouble();
        System.out.println("Enter intensity of second process:");
        intensities[1] = input.nextDouble();
        intensities[2] = intensities[0] + intensities[1];
        eventTimes[0] = exponentialRV(intensities[0]) + getTime();
        eventNumber[0] = 0;
        eventTimes[1] = exponentialRV(intensities[1]) + getTime();
        eventNumber[1] = 0;
        eventTimes[2] = exponentialRV(intensities[2]) + getTime();
        eventNumber[2] = 0;
        double startTime = getTime();
        while (true) {
            for (int i = 0; i < 3; i++) {
                if (eventTimes[i] < getTime()) {
                    eventNumber[i]++;
                    eventTimes[i] = exponentialRV(intensities[i]) + getTime();
                    int interval = 0;
                    if (i < 2) {
                        if ((getTime() - eventLastTime[0]) > intensities[2]) {
                            interval = 1;
                        }
                        eventLastTime[0] = getTime();
                        combinedProbabilities[interval]++;
                    } else {
                        if ((getTime() - eventLastTime[1]) > intensities[2]) {
                            interval = 1;
                        }
                        eventLastTime[1] = getTime();
                        sumProbabilities[interval]++;
                    }
                }
            }
            double experimentTime = getTime() - startTime;
            eventNumber[3] = eventNumber[0] + eventNumber[1];
            printData(experimentTime);
            sleep(3000);
        }
    }

    private static void printData(double experimentTime) {
        System.out.println("First event happened " + eventNumber[0] + " times. This is " + eventNumber[0] / experimentTime + " times per second on average");
        System.out.println("Second event happened " + eventNumber[1] + " times. This is " + eventNumber[1] / experimentTime + " times per second on average");
        System.out.println("First OR Second event happened " + eventNumber[3] + " times. This is " + eventNumber[3] / experimentTime + " times per second on average");
        System.out.println("Event with combined intensity happened " + eventNumber[2] + " times. This is " + eventNumber[2] / experimentTime + " times per second on average\n");
        System.out.println("For 'OR' event thread");
        double average = combinedProbabilities[1] * 1.0 / (combinedProbabilities[0] + combinedProbabilities[1]);
        System.out.println("Average: " + average);
        System.out.println("Dispersion: " + average * (1 - average) + '\n');
        System.out.println("For Sum event thread");
        average = sumProbabilities[1] * 1.0 / (sumProbabilities[0] + sumProbabilities[1]);
        System.out.println("Average: " + average);
        System.out.println("Dispersion: " + average * (1 - average));
    }
}