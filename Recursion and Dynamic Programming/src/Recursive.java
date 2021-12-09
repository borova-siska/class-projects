package assignment2;

import java.util.*;

public class Recursive {

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
     *         This method must be implemented using a naive recursive programming
     *         solution that identifies the optimal substructure of the problem. It is
     *         expected to have an exponential running time.
     * 
     *         (It is anticipated that you will need to introduce a new private helper
     *         method in the implementation of this solution.)
     */
    public static int optimalCostRecursive(ArrayList<Integer> ferries, ArrayList<Car> cars) {
        return optimalCostRecursiveHelper(ferries, cars, 0, 0, 0);
    }

    private static int optimalCostRecursiveHelper(ArrayList<Integer> ferries, ArrayList<Car> cars,
                                                  int c_l, int now_mod, int now_plus_mod) {
        int keep=Integer.MAX_VALUE, delay=Integer.MAX_VALUE, cancel;
        if (c_l == cars.size()) return 0;
        Car me = cars.get(c_l);

        // figure out if we need to move the modifiers for the next ferry
        int modMove;
        if (c_l == cars.size() - 1) modMove = 0;
        else if (me.ferry() == cars.get(c_l + 1).ferry()) modMove = 0;
        else if (me.ferry() + 1 == cars.get(c_l + 1).ferry()) modMove = 1;
        else modMove = 2;

        // try booking the car in for its ferry
        if (me.length() <= ferries.get(me.ferry()) - now_mod) {
            if (modMove == 0) {
                keep = optimalCostRecursiveHelper(ferries, cars, c_l + 1, now_mod + me.length(),
                        now_plus_mod);
            } else if (modMove == 1) {
                keep = optimalCostRecursiveHelper(ferries, cars, c_l + 1, now_plus_mod, 0);
            } else {
                keep = optimalCostRecursiveHelper(ferries, cars, c_l + 1, 0, 0);
            }
        }

        // try delaying the booking to the next ferry
        if (me.length() <= ferries.get(me.ferry() + 1) - now_plus_mod) {
            if (modMove == 0) {
                delay = optimalCostRecursiveHelper(ferries, cars, c_l + 1, now_mod,
                        now_plus_mod + me.length());
            } else if (modMove == 1) {
                delay = optimalCostRecursiveHelper(ferries, cars, c_l + 1,
                        now_plus_mod + me.length(), 0);
            } else {
                delay = optimalCostRecursiveHelper(ferries, cars, c_l + 1, 0, 0);
            }
            delay += me.delayCost();
        }

        // calculate cancel outcome
        if (modMove == 0) {
            cancel = optimalCostRecursiveHelper(ferries, cars, c_l + 1, now_mod, now_plus_mod);
        } else if (modMove == 1) {
            cancel = optimalCostRecursiveHelper(ferries, cars, c_l + 1, now_plus_mod, 0);
        } else {
            cancel = optimalCostRecursiveHelper(ferries, cars, c_l + 1, 0, 0);
        }
        cancel += me.cancelCost();

        // return max of the three possibilities
        return Math.min(keep, Math.min(delay, cancel));
    }
}
