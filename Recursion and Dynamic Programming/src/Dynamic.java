package assignment2;

import java.util.*;

public class Dynamic {

    /**
     * @param ferries
     *            A list describing the space available (in whole meters) on each of the
     *            ferries. E.g. there are ferries.size() ferries, and the jth ferry is
     *            ferries.get(j) meters in length. The length of each ferry is
     *            non-negative (i.e. it is greater than or equal to zero).
     * 
     * @param cars
     *            A list describing each of the cars that has a ticket for a ferry. E.g.
     *            there are cars.size() cars with tickets to a ferry, and the ith car is
     *            cars.get(i). (The first car is the 0th car, cars.get(0).) The list is in
     *            non-decreasing order of the ferry that each car has a ticket for. E.g.
     *            for any i in 0, 1, ... , cars.size()-2, cars.get(i).ferry() <=
     *            cars.get(i+1).ferry(). There is at least one car that has a ticket for a
     *            ferry, i.e. cars.size() > 0.
     * 
     *            For each car index 0, 1, ... , cars.size(), 0 <= cars.get(i).ferry() <
     *            ferries.size() - 1. That is, each car has a ticket for one of the first
     *            ferries.size() - 1 ferries. (This means that there is always a ferry
     *            that departs after the ferry that the car has a ticket booked for.)
     * 
     * @return Returns the minimum total allocation cost of any valid allocation of the
     *         given cars to the given ferries. (See the assignment handout for details.)
     * 
     *         This method must be implemented using an efficient bottom-up dynamic
     *         programming solution to the problem.
     */
    public static int optimalCostDynamic(ArrayList<Integer> ferries, ArrayList<Car> cars) {
        int[][][] T = new int[cars.size() + 1][Collections.max(ferries) + 1]
                [Collections.max(ferries) + 1];
        for (int i = cars.size(); i >= 0; i--) {
            for (int j = 0; j < Collections.max(ferries) + 1; j++) {
                for (int k = 0; k < Collections.max(ferries) + 1; k++) {
                    if (i == cars.size()) T[i][j][k] = 0;
                    else {
                        Car me = cars.get(i);

                        // figure out if we need to move the modifiers for the next ferry
                        int modMove;
                        if (i == cars.size() - 1) modMove = 0;
                        else if (me.ferry() == cars.get(i + 1).ferry()) modMove = 0;
                        else if (me.ferry() + 1 == cars.get(i + 1).ferry()) modMove = 1;
                        else modMove = 2;

                        int keep=Integer.MAX_VALUE, delay=Integer.MAX_VALUE, cancel;
                        if (modMove == 0) {
                            if (me.length() <= ferries.get(me.ferry()) - j) keep =
                                    T[i + 1][j + me.length()][k];
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) delay =
                                    T[i + 1][j][k + me.length()] + me.delayCost();
                            cancel = me.cancelCost() + T[i + 1][j][k];
                        } else if (modMove == 1) {
                            if (me.length() <= ferries.get(me.ferry()) - j) keep =
                                    T[i + 1][k][0];
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) delay =
                                    T[i + 1][k + me.length()][0] + me.delayCost();
                            cancel = me.cancelCost() + T[i + 1][k][0];
                        }
                        else {
                            if (me.length() <= ferries.get(me.ferry()) - j) keep =
                                    T[i + 1][0][0];
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) delay =
                                    T[i + 1][0][0] + me.delayCost();
                            cancel = me.cancelCost() + T[i + 1][0][0];
                        }

                        T[i][j][k] = Math.min(keep, Math.min(delay, cancel));
                    }
                }
            }
        }

        return T[0][0][0];
    }

    /**
     * @param ferries
     *            A list describing the space available (in whole meters) on each of the
     *            ferries. E.g. there are ferries.size() ferries, and the jth ferry is
     *            ferries.get(j) meters in length. The length of each ferry is
     *            non-negative (i.e. it is greater than or equal to zero).
     * 
     * @param cars
     *            A list describing each of the cars that has a ticket for a ferry. E.g.
     *            there are cars.size() cars with tickets to a ferry, and the ith car is
     *            cars.get(i). (The first car is the 0th car, cars.get(0).) The list is in
     *            non-decreasing order of the ferry that each car has a ticket for. E.g.
     *            for any i in 0, 1, ... , cars.size()-2, cars.get(i).ferry() <=
     *            cars.get(i+1).ferry(). There is at least one car that has a ticket for a
     *            ferry, i.e. cars.size() > 0.
     * 
     *            For each car index 0, 1, ... , cars.size(), 0 <= cars.get(i).ferry() <
     *            ferries.size() - 1. That is, each car has a ticket for one of the first
     *            ferries.size() - 1 ferries. (This means that there is always a ferry
     *            that departs after the ferry that the car has a ticket booked for.)
     * 
     * 
     * @return Calculates the minimum total allocation cost of any valid allocation of the
     *         given cars to the given ferries, and returns a valid allocation of cars to
     *         ferries that has that minimum cost. (See the assignment handout for
     *         details.)
     * 
     *         The length of the returned allocation list should be equal to the number of
     *         cars, and the ith position in the list should contain an integer denoting
     *         the ferry (either ferry cars.get(i).ferry() or ferry cars.get(i).ferry()
     *         +1) that the ith car has been allocated to, or the distinguished value "-1"
     *         if the ith car is not allocated to any ferry, i.e. the ticket held by the
     *         ith car is cancelled.
     * 
     *         This method must be implemented using an efficient bottom-up dynamic
     *         programming solution to the problem.
     */
    public static ArrayList<Integer> optimalSolutionDynamic(ArrayList<Integer> ferries,
                                                            ArrayList<Car> cars) {
        int ferryBound = Collections.max(ferries) + 1;
        int[][][] weights = new int[cars.size() + 1][ferryBound][ferryBound];
        int[][][][] path = new int[cars.size() + 1][ferryBound][ferryBound][3];
        int[][][] alloc = new int[cars.size() + 1][ferryBound][ferryBound];
        for (int i = cars.size(); i >= 0; i--) {
            for (int j = 0; j < ferryBound; j++) {
                for (int k = 0; k < ferryBound; k++) {
                    if (i == cars.size()) {
                        weights[i][j][k] = 0;
                        path[i][j][k] = null;
                        alloc[i][j][k] = -2;
                    }
                    else {
                        Car me = cars.get(i);

                        // figure out if we need to move the modifiers for the next ferry
                        int modMove;
                        if (i == cars.size() - 1) modMove = 0;
                        else if (me.ferry() == cars.get(i + 1).ferry()) modMove = 0;
                        else if (me.ferry() + 1 == cars.get(i + 1).ferry()) modMove = 1;
                        else modMove = 2;

                        int keep=Integer.MAX_VALUE, delay=Integer.MAX_VALUE, cancel;
                        int[] pathKeep = new int[3], pathDelay = new int[3], pathCancel;
                        if (modMove == 0) {
                            if (me.length() <= ferries.get(me.ferry()) - j) {
                                keep = weights[i + 1][j + me.length()][k];
                                pathKeep = new int[]{i + 1, j + me.length(), k};
                            }
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) {
                                delay = weights[i + 1][j][k + me.length()] + me.delayCost();
                                pathDelay = new int[]{i + 1, j, k + me.length()};
                            }
                            cancel = weights[i + 1][j][k] + me.cancelCost();
                            pathCancel = new int[]{i + 1, j, k};
                        } else if (modMove == 1) {
                            if (me.length() <= ferries.get(me.ferry()) - j) {
                                keep = weights[i + 1][k][0];
                                pathKeep = new int[]{i + 1, k, 0};
                            }
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) {
                                delay = weights[i + 1][k + me.length()][0] + me.delayCost();
                                pathDelay = new int[]{i + 1, k + me.length(), 0};
                            }
                            cancel = weights[i + 1][k][0] + me.cancelCost();
                            pathCancel = new int[]{i + 1, k, 0};
                        } else {
                            if (me.length() <= ferries.get(me.ferry()) - j) {
                                keep = weights[i + 1][0][0];
                                pathKeep = new int[]{i + 1, 0, 0};
                            }
                            if (me.length() <= ferries.get(me.ferry() + 1) - k) {
                                delay = weights[i + 1][0][0] + me.delayCost();
                                pathDelay = new int[]{i + 1, 0, 0};
                            }
                            cancel = weights[i + 1][0][0] + me.cancelCost();
                            pathCancel = new int[]{i + 1, 0, 0};
                        }

                        if (keep <= delay) {
                            if (keep <= cancel) {
                                weights[i][j][k] = keep;
                                path[i][j][k] = pathKeep;
                                alloc[i][j][k] = me.ferry();
                            } else /*cancel < keep <= delay*/ {
                                weights[i][j][k] = cancel;
                                path[i][j][k] = pathCancel;
                                alloc[i][j][k] = -1;
                            }
                        } else /*delay < keep*/ {
                            if (delay <= cancel) {
                                weights[i][j][k] = delay;
                                path[i][j][k] = pathDelay;
                                alloc[i][j][k] = me.ferry() + 1;
                            } else /*cancel < delay < keep*/ {
                                weights[i][j][k] = cancel;
                                path[i][j][k] = pathCancel;
                                alloc[i][j][k] = -1;
                            }
                        }
                    }
                }
            }
        }

        int[] minInd = new int[]{0, 0, 0};

        ArrayList<Integer> retPath = new ArrayList<>();
        int[] pathPos = minInd;
        int i, j, k;
        do {
            i = pathPos[0];
            j = pathPos[1];
            k = pathPos[2];
            retPath.add(alloc[i][j][k]);
            pathPos = path[i][j][k];
        } while (alloc[pathPos[0]][pathPos[1]][pathPos[2]] != -2);

        return retPath;
    }
}
