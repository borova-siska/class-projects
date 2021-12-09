package assignment2;

/**
 * An immutable representation of car.
 * 
 * A car has a length (in whole meters), the number of the ferry that they have a ticket
 * booked for, as well as a cost associated with being rescheduled to the next ferry, and
 * a cost associated with having their ferry ticket cancelled.
 * 
 * The length of the car must be greater than zero, and the number of the ferry that they
 * are booked for must be greater than or equal to zero. The rescheduling cost, as well as
 * the cancellation cost must be greater than zero, and the cancel cost must be greater
 * than the cost of being rescheduled to the next ferry.
 *
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */

public final class Car {

    /* The length (in whole meters) of the car */
    private final int length;
    /* The number of the ferry that the car has a ticket booked for. */
    private final int ferry;
    /* The cost associated with rescheduling the car to the next ferry. */
    private final int delayCost;
    /* The cost associated with having their ferry ticket cancelled. */
    private final int cancelCost;

    /**
     * 
     * @param length
     *            The length (in whole meters) of the car, where length is greater than 0.
     * @param ferry
     *            The number of the ferry that the car has a ticket booked for, where
     *            ferry is greater than or equal to 0.
     * @param delayCost
     *            The cost associated with rescheduling the car to the next ferry, where 0
     *            < delayCost < cancelCost.
     * @param cancelCost
     *            The cost associated with having their ferry ticket cancelled, where 0 <
     *            delayCost < cancelCost.
     * 
     * @return Creates a new car with the given length, ferry, delay cost and cancel cost.
     * 
     */
    public Car(int length, int ferry, int delayCost, int cancelCost) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length " + length + " must be greater than zero.");
        }
        if (ferry < 0) {
            throw new IllegalArgumentException(
                    "Ferry number" + ferry + " must be greater than or equal to zero.");
        }
        if (delayCost <= 0 || cancelCost <= 0 || cancelCost <= delayCost) {
            throw new IllegalArgumentException("Delay and cancel costs must be greater than zero "
                    + "and the delay cost must be less the cancel cost.");
        }
        this.length = length;
        this.ferry = ferry;
        this.delayCost = delayCost;
        this.cancelCost = cancelCost;
    }

    /**
     * @return Returns the length (in whole meters) of the car.
     */
    public int length() {
        return length;
    }

    /**
     * @return Returns number of the ferry that the car has a ticket booked for.
     */
    public int ferry() {
        return ferry;
    }

    /**
     * @return Returns the cost associated with rescheduling the car to the next ferry.
     */
    public int delayCost() {
        return delayCost;
    }

    /**
     * @return Returns the cost associated with having their ferry ticket cancelled.
     */
    public int cancelCost() {
        return cancelCost;
    }

    @Override
    public String toString() {
        return "(" + length + ", " + ferry + ", " + delayCost + ", " + cancelCost + ")";
    }

}
