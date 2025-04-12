import java.util.Scanner;

public class ParkingLot {

    static class ParkingSpot {
        String spotID;
        boolean isOccupied;
        String vehicleDetails;

        public ParkingSpot(String spotID) {
            this.spotID = spotID;
            this.isOccupied = false;
            this.vehicleDetails = null;
        }

        public void parkVehicle(String vehicleDetails) {
            this.isOccupied = true;
            this.vehicleDetails = vehicleDetails;
        }

        public void removeVehicle() {
            this.isOccupied = false;
            this.vehicleDetails = null;
        }

        public void displayInfo() {
            if (isOccupied) {
                System.out.println(spotID + " is occupied by: " + vehicleDetails);
            } else {
                System.out.println(spotID + " is available.");
            }
        }
    }

    static class ParkingLotManager {
        ParkingSpot[] spots;

        public ParkingLotManager(int totalSpots) {
            spots = new ParkingSpot[totalSpots];
            for (int i = 0; i < totalSpots; i++) {
                spots[i] = new ParkingSpot("Spot" + (i + 1));
            }
        }

        public void parkVehicle(String vehicleDetails) {
            for (ParkingSpot spot : spots) {
                if (!spot.isOccupied) {
                    spot.parkVehicle(vehicleDetails);
                    System.out.println("Vehicle parked in " + spot.spotID);
                    return;
                }
            }
            System.out.println("No available parking spots.");
        }

        public void removeVehicle(String spotID) {
            for (ParkingSpot spot : spots) {
                if (spot.spotID.equals(spotID)) {
                    if (spot.isOccupied) {
                        spot.removeVehicle();
                        System.out.println("Vehicle removed from " + spot.spotID);
                    } else {
                        System.out.println("No vehicle to remove from " + spot.spotID);
                    }
                    return;
                }
            }
            System.out.println("Invalid spot ID.");
        }

        public void viewParkedVehicles() {
            for (ParkingSpot spot : spots) {
                spot.displayInfo();
            }
        }

        public void checkAvailability() {
            for (ParkingSpot spot : spots) {
                if (!spot.isOccupied) {
                    System.out.println("There are available parking spots.");
                    return;
                }
            }
            System.out.println("No available parking spots.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingLotManager parkingLotManager = new ParkingLotManager(5); // 5 spots

        while (true) {
            System.out.println("\nParking Lot Management System");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. View Parked Vehicles");
            System.out.println("4. Check Availability");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle details: ");
                    String vehicleDetails = scanner.nextLine();
                    parkingLotManager.parkVehicle(vehicleDetails);
                    break;
                case 2:
                    System.out.print("Enter spot ID to remove vehicle: ");
                    String spotID = scanner.nextLine();
                    parkingLotManager.removeVehicle(spotID);
                    break;
                case 3:
                    parkingLotManager.viewParkedVehicles();
                    break;
                case 4:
                    parkingLotManager.checkAvailability();
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

