import java.util.*;

public class Main
{
    // Function for booking ticket
    public static int bookTicket(Passenger p)
    {
        TicketBooker booker = new TicketBooker();
        // Check if no tickets are available
        if (TicketBooker.availableWaitingList == 0)
        {
            System.out.println("No Tickets Available");
            return -1; // Indicate failure
        }
        // Check if preferred berth is available
        if ((p.berthPreference.equals("L") && TicketBooker.availableLowerBerths > 0) ||
            (p.berthPreference.equals("M") && TicketBooker.availableMiddleBerths > 0) ||
            (p.berthPreference.equals("U") && TicketBooker.availableUpperBerths > 0))
        {
            // Preferred berth available logic here
            if (p.berthPreference.equals("L"))
            {
                System.out.println("Lower Berth Given");
                booker.bookTicket(p, TicketBooker.lowerBerthsPositions.get(0), "L");
                TicketBooker.lowerBerthsPositions.remove(0);
                TicketBooker.availableLowerBerths--;
            }
            else if (p.berthPreference.equals("M"))
            {
                System.out.println("Middle Berth Given");
                booker.bookTicket(p, TicketBooker.middleBerthsPositions.get(0), "M");
                TicketBooker.middleBerthsPositions.remove(0);
                TicketBooker.availableMiddleBerths--;
            }
            else if (p.berthPreference.equals("U"))
            {
                System.out.println("Upper Berth Given");
                booker.bookTicket(p, TicketBooker.upperBerthsPositions.get(0), "U");
                TicketBooker.upperBerthsPositions.remove(0);
                TicketBooker.availableUpperBerths--;
            }
        }
        else if (TicketBooker.availableLowerBerths > 0)
        {
            System.out.println("Lower Berth Given");
            booker.bookTicket(p, TicketBooker.lowerBerthsPositions.get(0), "L");
            TicketBooker.lowerBerthsPositions.remove(0);
            TicketBooker.availableLowerBerths--;
        }
        else if (TicketBooker.availableMiddleBerths > 0)
        {
            System.out.println("Middle Berth Given");
            booker.bookTicket(p, TicketBooker.middleBerthsPositions.get(0), "M");
            TicketBooker.middleBerthsPositions.remove(0);
            TicketBooker.availableMiddleBerths--;
        }
        else if (TicketBooker.availableUpperBerths > 0)
        {
            System.out.println("Upper Berth Given");
            booker.bookTicket(p, TicketBooker.upperBerthsPositions.get(0), "U");
            TicketBooker.upperBerthsPositions.remove(0);
            TicketBooker.availableUpperBerths--;
        }
        else if (TicketBooker.availableRacTickets > 0)
        {
            System.out.println("RAC available");
            booker.addToRAC(p, TicketBooker.racPositions.get(0), "RAC");
        }
        else if (TicketBooker.availableWaitingList > 0)
        {
            System.out.println("Added to Waiting List");
            booker.addToWaitingList(p, TicketBooker.waitingListPositions.get(0), "WL");
        }
        // Return passenger ID if booking is successful
        return p.passengerId;
    }

    // Cancel ticket function
    public static void cancelTicket(int id)
    {
        TicketBooker booker = new TicketBooker();
        // Check if passenger ID is valid
        if (!booker.passengers.containsKey(id))
        {
            System.out.println("Passenger detail Unknown");
        }
        else
            booker.cancelTicket(id);
    }

    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        boolean loop = true;
        // Loop to get choices from user until they stop
        while (loop)
        {
            System.out.println("1. Book Ticket \n2. Cancel Ticket \n3. Available Tickets \n4. Booked Tickets \n5. Exit");
            int choice = s.nextInt();
            switch (choice)
            {
                // Book ticket
                case 1:
                {
                    // Get details from Passenger
                    System.out.println("Enter Passenger name, age and berth preference (L, M or U)");
                    String name = s.next();
                    int age = s.nextInt();
                    // Get berth preference (L, U, M)
                    String berthPreference = s.next();
                    // Create a passenger object
                    Passenger p = new Passenger(name, age, berthPreference);
                    // Book ticket and get passenger ID
                    int passengerId = bookTicket(p);
                    if (passengerId != -1)
                    {
                        System.out.println("Booking successful! Your Passenger ID is: " + passengerId);
                    }
                    else
                    {
                        System.out.println("Booking failed.");
                    }
                }
                break;

                // Cancel ticket
                case 2:
                {
                    // Get passenger ID to cancel
                    System.out.println("Enter passenger ID to cancel");
                    int id = s.nextInt();
                    cancelTicket(id);
                }
                break;

                // Available tickets print
                case 3:
                {
                    TicketBooker booker = new TicketBooker();
                    booker.printAvailable();
                }
                break;

                // Occupied tickets print
                case 4:
                {
                    TicketBooker booker = new TicketBooker();
                    booker.printPassengers();
                }
                break;

                // Exit
                case 5:
                {
                    loop = false;
                }
                break;

                default:
                break;
            }
        }
        s.close();
    }
}

class Passenger
{
    static int id = 1; // Static variable to give id for every new passenger
    String name;
    int age;
    String berthPreference; // U or L or M
    int passengerId; // ID of passenger created automatically
    String alloted; // Alloted type (L, U, M, RAC, WL)
    int number; // Seat number

    public Passenger(String name, int age, String berthPreference)
    {
        this.name = name;
        this.age = age;
        this.berthPreference = berthPreference;
        this.passengerId = id++;
        alloted = "";
        number = -1;
    }
}

class TicketBooker
{
    // 63 berths (upper, lower, middle) + 18 RAC passengers + 10 waiting list tickets
    static int availableLowerBerths = 1; // Normally 21
    static int availableMiddleBerths = 1; // Normally 21
    static int availableUpperBerths = 1; // Normally 21
    static int availableRacTickets = 1; // Normally 18
    static int availableWaitingList = 1; // Normally 10

    static Queue<Integer> waitingList = new LinkedList<>(); // Queue of WL passengers
    static Queue<Integer> racList = new LinkedList<>(); // Queue of RAC passengers
    static List<Integer> bookedTicketList = new ArrayList<>(); // List of booked ticket passengers

    static List<Integer> lowerBerthsPositions = new ArrayList<>(Arrays.asList(1)); // Normally 1,2,...21
    static List<Integer> middleBerthsPositions = new ArrayList<>(Arrays.asList(1)); // Normally 1,2,...21
    static List<Integer> upperBerthsPositions = new ArrayList<>(Arrays.asList(1)); // Normally 1,2,...21
    static List<Integer> racPositions = new ArrayList<>(Arrays.asList(1)); // Normally 1,2,...18
    static List<Integer> waitingListPositions = new ArrayList<>(Arrays.asList(1)); // Normally 1,2,...10

    static Map<Integer, Passenger> passengers = new HashMap<>(); // Map of passenger IDs to passengers

    // Book ticket
    public void bookTicket(Passenger p, int berthInfo, String allotedBerth)
    {
        // Assign the seat number and type of berth (L, U, M)
        p.number = berthInfo;
        p.alloted = allotedBerth;
        // Add passenger to the map
        passengers.put(p.passengerId, p);
        // Add passenger ID to the list of booked tickets
        bookedTicketList.add(p.passengerId);
        System.out.println("--------------------------Booked Successfully");
    }

    // Adding to RAC
    public void addToRAC(Passenger p, int racInfo, String allotedRAC)
    {
        // Assign seat number and type (RAC)
        p.number = racInfo;
        p.alloted = allotedRAC;
        // Add passenger to the map
        passengers.put(p.passengerId, p);
        // Add passenger ID to the queue of RAC tickets
        racList.add(p.passengerId);
        // Decrease available RAC tickets by 1    
        availableRacTickets--;
        // Remove the position that was allotted to the passenger
        racPositions.remove(0);

        System.out.println("--------------------------Added to RAC Successfully");
    }

    // Adding to Waiting List
    public void addToWaitingList(Passenger p, int waitingListInfo, String allotedWL)
    {
        // Assign seat number and type (WL)
        p.number = waitingListInfo; 
        p.alloted = allotedWL;
        // Add passenger to the map
        passengers.put(p.passengerId, p);
        // Add passenger ID to the queue of WL tickets
        waitingList.add(p.passengerId);
        // Decrease available waiting list tickets by 1    
        availableWaitingList--;
        // Remove the position that was allotted to the passenger
        waitingListPositions.remove(0);

        System.out.println("--------------------------Added to Waiting List Successfully");
    }

    // Cancel ticket
    public void cancelTicket(int id)
    {
        // Check if passenger ID is valid
        if (!passengers.containsKey(id))
        {
            System.out.println("Passenger detail Unknown");
            return;
        }
        Passenger p = passengers.get(id);
        // Return seat based on berth type
        if (p.alloted.equals("L"))
        {
            availableLowerBerths++;
            lowerBerthsPositions.add(p.number);
        }
        else if (p.alloted.equals("M"))
        {
            availableMiddleBerths++;
            middleBerthsPositions.add(p.number);
        }
        else if (p.alloted.equals("U"))
        {
            availableUpperBerths++;
            upperBerthsPositions.add(p.number);
        }
        else if (p.alloted.equals("RAC"))
        {
            availableRacTickets++;
            racPositions.add(p.number);
            racList.remove(id); // Remove from RAC list
        }
        else if (p.alloted.equals("WL"))
        {
            availableWaitingList++;
            waitingListPositions.add(p.number);
            waitingList.remove(id); // Remove from waiting list
        }
        // Remove from booked list and map
        bookedTicketList.remove(Integer.valueOf(id));
        passengers.remove(id);

        System.out.println("--------------------------Ticket Cancelled Successfully");
    }

    // Print available tickets
    public void printAvailable()
    {
        System.out.println("Lower Berths Available: " + availableLowerBerths);
        System.out.println("Middle Berths Available: " + availableMiddleBerths);
        System.out.println("Upper Berths Available: " + availableUpperBerths);
        System.out.println("RAC Tickets Available: " + availableRacTickets);
        System.out.println("Waiting List Tickets Available: " + availableWaitingList);
    }

    // Print booked tickets
    public void printPassengers()
    {
        System.out.println("Booked Tickets: " + bookedTicketList);
        System.out.println("RAC List: " + racList);
        System.out.println("Waiting List: " + waitingList);
    }
}

