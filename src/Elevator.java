import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Elevator implements Runnable {
    int id;
    int maximumPeopleLoad;
    int currentFloor;
    ArrayList<LinkedList<Call>> floorsInfo;
    LinkedList<Integer> floorsQueue;
    // 0  - лифт стоит
    // 1  - движение вверх
    // -1 - движение вниз
    int direction;

    ArrayList<Call> passengers;


    Elevator(int id,
             int maximumPeopleLoad,
             ArrayList<LinkedList<Call>> floorsInfo,
             LinkedList<Integer> floorsQueue) {
        this.id = id;
        this.maximumPeopleLoad = maximumPeopleLoad;
        this.floorsInfo = floorsInfo;
        this.floorsQueue = floorsQueue;
        this.passengers = new ArrayList<Call>();
        this.currentFloor = 0;
        this.direction = 0;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public ArrayList<Call> getPassengers() {
        return (ArrayList<Call>)passengers.clone();
    }

    public void setPassengers(ArrayList<Call> passengers) {
        this.passengers = passengers;
    }

    // TODO: Проблема, когда в первый раз генерится человек на 0 этаже
    public void run() {
        while (true) {
            // Поэтапно:
            try {
                Random randomNumber = new Random();
                Thread.sleep(randomNumber.nextInt(100));
                if (direction != 0) {
                    System.out.printf("Лифт №%d приехал на %d этаж\n", id, currentFloor);
                }
                this.exitTheElevator();
                this.pickUpNewPassengers();
                this.move();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized void exitTheElevator() throws InterruptedException {
        if (passengers.size() != 0) {
            ArrayList<Call> remainingPassengers = new ArrayList<Call>();
            for (Call passenger : passengers) {
                if (passenger.getDestinationFloor() != currentFloor) {
                    remainingPassengers.add(passenger);
                } else {
                    System.out.printf("Пассажир вышел из лифта №%d на %d этаже\n", id, currentFloor);
                    // Ожидание выхода пассажира
                    Thread.sleep(150);
                }
            }
            if (remainingPassengers.size() == 0) {
                setDirection(0);
            }
            setPassengers(remainingPassengers);
        }
    }

    public synchronized void pickUpNewPassengers() {
        if (passengers.size() < maximumPeopleLoad
                && floorsInfo.get(currentFloor).size() != 0) {
            ArrayList<Call> currentPassengers = this.getPassengers();
            ArrayList<Integer> deletedIndexes = new ArrayList<Integer>();
            int availableSeatsCount = maximumPeopleLoad - passengers.size();
            for (int j = 0; availableSeatsCount != 0 && j < floorsInfo.get(currentFloor).size(); j++) {
                if (direction == 0 || passengers.size() == 0
                        || direction == 1
                                && floorsInfo.get(currentFloor).get(j).getDestinationFloor() > currentFloor
                        || direction == -1
                                && floorsInfo.get(currentFloor).get(j).getDestinationFloor() < currentFloor) {
                    System.out.printf("Пассажир вошёл в лифт №%d на %d этаже\n", id, currentFloor);
                    Call passenger = floorsInfo.get(currentFloor).get(j);
                    deletedIndexes.add(j);
                    if (direction == 0 || currentPassengers.size() == 0) {
                        setDirection(passenger.getDestinationFloor() > currentFloor ? 1 : -1);
                    }
                    availableSeatsCount -= 1;
                    currentPassengers.add(passenger);
                }
            }
            for (int i = 0; i < deletedIndexes.size(); i++) {
                floorsInfo.get(currentFloor).remove(i);
            }
            setPassengers(currentPassengers);
        }
    }

    public synchronized void move() throws InterruptedException {
        // TODO: Удалить все этажи floorsQueue, которые мы проедем (и которые не триггерятся)
        if (direction == 0 && floorsQueue.size() == 0) {
            return;
        }
        if (direction == 0 && floorsQueue.size() != 0) {
            Integer goTo = floorsQueue.getFirst();
            floorsQueue.removeFirst();
            setDirection(goTo > currentFloor ? 1 : -1);
            System.out.printf(
                    "Лифт №%d принимает вызов на %d этаж\n",
                    id, goTo);
        }
        setCurrentFloor(currentFloor + direction);
        Thread.sleep(3000);
    }
}
