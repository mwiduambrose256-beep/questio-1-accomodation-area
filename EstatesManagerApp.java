import java.util.Scanner;
import java.util.InputMismatchException; 

abstract class AccommodationArea {
   
    private String name;
    private int occupants;

    // Array to store the state of the 3 lights (true = ON, false = OFF)
    private boolean[] lightStates = new boolean[3];

    // Constructor
    public AccommodationArea(String name) {
        this.name = name;
        this.occupants = 0; // Start with zero occupants
        // Lights are initialised to OFF (false) by default
    }

    // W - Add occupants
    public void addOccupants(int n) {
        this.occupants += n;
        System.out.println(n + " occupants added to " + this.name + ".");
    }

    // X - Remove occupants (count must not go below zero)
    public void removeOccupants(int n) {
        int actualRemoved = 0;
        if (n <= this.occupants) {
            this.occupants -= n;
            actualRemoved = n;
        } else {
            // Only remove the current number of occupants to prevent negative count
            actualRemoved = this.occupants;
            this.occupants = 0; 
            System.out.println("Warning: Cannot remove " + n + ". Removing all " + actualRemoved + " occupants instead.");
        }
        System.out.println(actualRemoved + " occupants removed from " + this.name + ".");
    }

    // Y - Switch ON a light (light number is 1-3, array index is 0-2)
    public void switchLightOn(int lightNum) {
        this.lightStates[lightNum - 1] = true;
        System.out.println("Light " + lightNum + " switched ON in " + this.name + ".");
    }

    // Z - Switch OFF a light
    public void switchLightOff(int lightNum) {
        this.lightStates[lightNum - 1] = false;
        System.out.println("Light " + lightNum + " switched OFF in " + this.name + ".");
    }

    // R - Report status
    public String getStatusReport() {
        String lightStatus = "";
        for (int i = 0; i < lightStates.length; i++) {
            // Append the status of each light (1, 2, 3)
            lightStatus += "Light " + (i + 1) + ": " + (lightStates[i] ? "ON" : "OFF") + (i < 2 ? ", " : "");
        }
        return "Area Name: " + this.name + 
               "\nOccupants: " + this.occupants +
               "\nLight States: " + lightStatus;
    }
    
    // Getter for Name
    public String getName() {
        return name;
    }
}


class GymArea extends AccommodationArea {
    public GymArea() {
        super("Gym");
    }
}


class SwimmingArea extends AccommodationArea {
    public SwimmingArea() {
        super("Swimming Pool");
    }
}



public class EstatesManagerApp {

    private AccommodationArea activeArea;
    private GymArea gymArea = new GymArea();
    private SwimmingArea swimmingArea = new SwimmingArea();
    private Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        EstatesManagerApp app = new EstatesManagerApp();
        app.run();
    }

    private void run() {
        // Initial active area is set to Gym for a starting state
        activeArea = gymArea; 
        System.out.println("Speke Apartments Estates Manager Application started.");
        System.out.println("Default active area: " + activeArea.getName());

        String command;
        // Keep the menu running until Q is entered.
        do {
            displayMenu();
            System.out.print("\nEnter Command (S, W, X, Y, Z, R, Q): ");
            command = scanner.nextLine().trim().toUpperCase();

            switch (command) {
                case "S":
                    selectArea();
                    break;
                case "W":
                    addOccupants();
                    break;
                case "X":
                    removeOccupants();
                    break;
                case "Y":
                    switchLight(true); // true for ON
                    break;
                case "Z":
                    switchLight(false); // false for OFF
                    break;
                case "R":
                    reportStatus();
                    break;
                case "Q":
                    System.out.println("Program terminated successfully. Goodbye! 👋");
                    break;
                default:
                    System.out.println("🛑 Invalid command. Please try again.");
            }
            System.out.println("\n-----------------------------------------");

        } while (!command.equals("Q"));

        scanner.close(); // Clean termination
    }

    private void displayMenu() {
        System.out.println("\n--- MAIN MENU (Active Area: " + activeArea.getName() + ") ---");
        System.out.println("S - Select active area (G=Gym, P=Swimming)");
        System.out.println("W - Add occupants (n)");
        System.out.println("X - Remove occupants (n)");
        System.out.println("Y - Switch ON light (1-3)");
        System.out.println("Z - Switch OFF light (1-3)");
        System.out.println("R - Report status");
        System.out.println("Q - Quit the program");
    }

    // S- Select active area
    private void selectArea() {
        System.out.print("Select (G)ym or (P)ool: ");
        String selection = scanner.nextLine().trim().toUpperCase();

        if (selection.equals("G")) {
            activeArea = gymArea;
            System.out.println("Active area is now: " + activeArea.getName());
        } else if (selection.equals("P")) {
            activeArea = swimmingArea;
            System.out.println("Active area is now: " + activeArea.getName());
        } else {
            System.out.println("🛑 Invalid selection. Active area remains: " + activeArea.getName());
        }
    }

    // W - Add occupants
    private void addOccupants() {
        int n = getIntegerInput("Enter number of occupants to ADD: ");
        if (n > 0) {
            activeArea.addOccupants(n);
        } else if (n < 0) {
            System.out.println("🛑 Cannot add a negative number of occupants.");
        }
    }

    // X - Remove occupants
    private void removeOccupants() {
        int n = getIntegerInput("Enter number of occupants to REMOVE: ");
        if (n > 0) {
            activeArea.removeOccupants(n);
        } else if (n < 0) {
            System.out.println("🛑 Cannot remove a negative number of occupants.");
        }
    }

    // Y/Z - Switch light ON/OFF
    private void switchLight(boolean on) {
        int lightNum = getLightNumberInput(on ? "ON" : "OFF");
        if (lightNum != -1) { 
            if (on) {
                activeArea.switchLightOn(lightNum);
            } else {
                activeArea.switchLightOff(lightNum);
            }
        }
    }

    // R - Report status
    private void reportStatus() {
        System.out.println("\n--- STATUS REPORT FOR " + activeArea.getName() + " ---");
        System.out.println(activeArea.getStatusReport());
    }

    // --- Defensive Input Handling (Re-prompting Validation) ---

    // Validate all inputs (integers only)
    private int getIntegerInput(String prompt) {
        int value = -1; 
        boolean valid = false;
        
        // Use a loop to re-prompt on invalid input
        while (!valid) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                value = Integer.parseInt(input);
                valid = true; 
            } catch (NumberFormatException e) {
                System.out.println("🛑 Invalid input. Please enter an INTEGER only.");
                // loop continues
            }
        }
        return value;
    }

    // Validate all inputs (light {1,2,3})
    private int getLightNumberInput(String action) {
        int lightNum = -1;
        boolean valid = false;

        while (!valid) {
            lightNum = getIntegerInput("Enter light number (1, 2, or 3) to switch " + action + ": ");
            
            if (lightNum >= 1 && lightNum <= 3) {
                valid = true;
            } else {
                System.out.println("🛑 Invalid light number. Must be 1, 2, or 3.");
            }
        }
        return lightNum;
    }
}