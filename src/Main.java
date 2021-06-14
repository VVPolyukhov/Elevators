public class Main {
    public static void main(String[] args) {
        ElevatorsSystem elevatorsSystem = new ElevatorsSystem(5); // 20
        elevatorsSystem.createElevators(2, 4);
        elevatorsSystem.generateCalls();
    }
}