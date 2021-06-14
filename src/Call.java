public class Call {
    int sourceFloor;
    int destinationFloor;

    Call(int source, int destination) {
        this.sourceFloor = source;
        this.destinationFloor = destination;
    }

    public int getDestinationFloor() {
        return this.destinationFloor;
    }
}

