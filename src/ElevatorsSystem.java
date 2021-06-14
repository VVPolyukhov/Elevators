import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ElevatorsSystem {
    int floorsCount;
    ArrayList<LinkedList<Call>> floorsInfo;
    LinkedList<Integer> floorsQueue;
    ArrayList<Elevator> elevatorsList;

    ElevatorsSystem(int floorsCount) {
        this.floorsCount = floorsCount;
        this.floorsInfo = new ArrayList<>();
        for(int i = 0; i < floorsCount; i++){
            floorsInfo.add(new LinkedList<>());
        }
        this.floorsQueue = new LinkedList<Integer>();
        this.elevatorsList = new ArrayList<Elevator>();
    }

    public ArrayList<LinkedList<Call>> getFloorsInfo() {
        return floorsInfo;
    }

    public LinkedList<Integer> getFloorsQueue() {
        return floorsQueue;
    }

    public LinkedList<Call> addToFloorQueueList(int index, Call call) {
        LinkedList<Call> newCallsList = this.getFloorsInfo().get(index);
        newCallsList.addLast(call);
        return newCallsList;
    }

    public void addToFloorsInfo(int index, Call call) {
        this.floorsInfo.add(index, addToFloorQueueList(index, call));
    }

    public void addToFloorsQueue(int sourceFloor) {
        this.floorsQueue.addLast(sourceFloor);
    }

    public void setElevatorsList(ArrayList<Elevator> elevatorsList) {
        this.elevatorsList = elevatorsList;
    }

    public ArrayList<Elevator> getElevatorsList() {
        return elevatorsList;
    }

    public void createElevators(int elevatorsNumber, int maximumPeopleLoad) {
        for (int i = 1; i <= elevatorsNumber; i++) {
            Elevator elevator = new Elevator(i, maximumPeopleLoad, this.getFloorsInfo(), this.getFloorsQueue());
            ArrayList<Elevator> newElevatorsList = this.getElevatorsList();
            newElevatorsList.add(elevator);
            setElevatorsList(newElevatorsList);

            Thread thread = new Thread(elevator);
            thread.start();
        }
    }

    public void generateCalls() {
        Thread callsGeneratorThread = new Thread(new Runnable() {
            public void run() {
                Random randomNumber = new Random();
                while (true) {
                    int sourceFloor = randomNumber.nextInt(floorsCount);
                    int destinationFloor = randomNumber.nextInt(floorsCount);
                    if (destinationFloor != sourceFloor) {
                        System.out.printf("Поступил вызов с %d до %d\n", sourceFloor, destinationFloor);
                        Call call = new Call(
                                sourceFloor,
                                destinationFloor
                        );
                        addToFloorsInfo(sourceFloor, call);
                        addToFloorsQueue(sourceFloor);

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
        callsGeneratorThread.start();
    }
}
