/*  REFERENCES
1. https://www.scaler.com/topics/object-to-string-java/
2. https://stackoverflow.com/questions/5235401/split-string-into-array-of-character-strings
3. https://www.geeksforgeeks.org/enhancements-for-switch-statement-in-java-13/
4. https://www.w3schools.com/java/ref_string_contains.asp
5. https://stackoverflow.com/questions/25852961/how-to-remove-brackets-character-in-string-java
6. https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
7. https://www.javatpoint.com/post/java-scanner-hasnext-method
8. https://www.geeksforgeeks.org/arraylist-array-conversion-java-toarray-methods/
9. https://www.digitalocean.com/community/tutorials/java-write-to-file
10. https://www.javatpoint.com/java-integer-valueof-method#:~:text=3.-,Java%20Integer%20valueOf(String%20s%2C%20int%20radix)%20Method,given%20by%20the%20second%20argument.
*/

import java.util.*;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
public class Theatre
{

    public static ArrayList <Integer> arraylist_showAvailable = new ArrayList<>();
    public static ArrayList <Ticket> ArrayListTickets = new ArrayList<>();
    public static Scanner input = new Scanner(System.in);
    public static Person person;

    //arrays for all rows of theatre
    static int[] row1 = new int[12];
    static int[] row2 = new int[16];
    static int[] row3 = new int[20];
    static int[][] rows = {row1 , row2 , row3};        //row1,row2 and row3's in a 2-D array to be used in load() and show_available().
    //static String name, surname, email;    //string_for_load is for the text file conversion in the load() method.
    static int i, row, seat, price;

    //for row and seat selection in buy_ticket and cancel_ticket
    static boolean row_boolean = false;
    static boolean seat_boolean = false;
    public static void main ( String[] args )
    {
        System.out.println("Welcome to the New Theatre");
        while (true)
        {
            System.out.println("---------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load to file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("\t0) Quit");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter option: ");

            String option = input.next();
            switch (option)
            {
                case "1" :
                    buy_ticket();
                    row_boolean = false;
                    seat_boolean = false;
                    continue;
                case "2" :
                    print_seating_area();
                    continue;
                case "3" :
                    cancel_ticket();
                    row_boolean = false;
                    seat_boolean = false;
                    continue;
                case "4" :
                    show_available();
                    continue;
                case "5" :
                    save();
                    continue;
                case "6" :
                    load();
                    continue;
                case "7" :
                    show_tickets_info();
                    continue;
                case "8" :
                    continue;
                case "0" :
                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    /**
     Checks if a seat is occupied or vacant. It runs tasks from the buy_ticket() and cancel_ticket() methods after the row is confirmed in those methods.
     Part for buy_ticket() inputs prices of tickets and then sends the person and ticket information to ArrayListTickets.
     Part for cancel_ticket() removes the ticket from ArrayListTickets. Incorrect input send to "junk_input".
     @param rowNumber row number selected in buy_ticket() and cancel_ticket() methods.
     @param row row arrays of row1, row2 and row3 are sent to this one.
     @param seatSuccessful sentence for when the seat condition for the respective methods is satisfied.
     @param seatUnsuccessful sentence for when the seat condition for the respective methods is unsatisfied.
     @param seat0or1 changes row array's value to 1 for buy_ticket() and 0 for cancel_ticket(). 1-seat's vacant, 0-seat's occupied.
     @param seat1or0 changes row array's value to 0 for buy_ticket() and 1 for cancel_ticket(). 1-seat's vacant, 0-seat's occupied.
     */
    private static void buyTicket_and_cancelTicket(int rowNumber, int[] row, String seatSuccessful, String seatUnsuccessful, int seat0or1, int seat1or0)
    {
        row_boolean = true;
        while (!seat_boolean)
        {
            System.out.print("Input seat number: ");
            if (input.hasNextInt())
            {
                seat = input.nextInt();
                if ((0 < seat) && (seat < row.length + 1))
                {
                    seat_boolean = true;
                    if (row[seat-1] == (seat0or1))        //seat is 0 for buy_ticket() and 1 for cancel_ticket()
                    {
                        if (row[seat-1]==0)
                        {
                            price=price_check();
                        }

                        row[seat-1] = seat1or0;       //opposite of similar name typed above. seat is 1 for buy_ticket() and 0 for cancel_ticket()
                        System.out.println(seatSuccessful);

                        if (seat1or0 == 1)
                        {
                            Ticket Ticket = new Ticket(rowNumber, seat, person, price);//PART B row and seat numbers along with person details...
                            ArrayListTickets.add(Ticket);                          //...and price entered into arraylist
                        }
                        else
                        {
                            for (i = 0; i < ArrayListTickets.size(); i++)
                            {
                                Ticket Ticket = ArrayListTickets.get(i);
                                int delete_seat = Ticket.getSeat();       //uses Ticket class's getSeat() getter method
                                int delete_row = Ticket.getRow();         //uses Ticket class's getRow() getter method

                                if (delete_row == rowNumber && delete_seat == seat)
                                {
                                    ArrayListTickets.remove(i);      //ticket removed from arraylist
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println(seatUnsuccessful);  //Sentence for parts in both buy_ticket() and cancel_ticket() when an option is incorrectly entered.
                    }
                }
                else
                {
                    System.out.println("Incorrect option entered. Row 1: 12 seats, Row 2: 16 seats, Row 3: 20 seats.");
                }
            }
            else
            {
                System.out.println("Enter a valid number.");
                seat = 1;
                String junk_input = input.next();      //incorrect input sent to "junk_input" variable and value given to seat variable.
            }
        }

    }

    /**
     Checks if the price entered is an integer and a positive number
     @return price value to buyTicket_and_cancelTicket() method.
     */
    private static int price_check()
    {
        while (true)
        {
            System.out.print ("Enter price of seat: Rs.");
            if (input.hasNextInt())
            {
                price = input.nextInt();
                if (price>=0)
                {
                    return price;
                }
                else
                {
                    System.out.println("Enter a valid number.");
                }
            }
            else
            {
                String junk_input= input.next();
                price=1;
                System.out.println("Enter a valid number.");
            }
        }
    }

    /**
     Method verifies the email address for the buy_ticket() method.
     @return the entered email address
     */
    private static String email_format_check()
    {
        while (true)
        {
            System.out.print("Enter email address: ");
            String email = input.next();
            if (email.contains("@") && (email.contains(".")) && (email.length()>4))
            {
                return email;
            }
            else
            {
                System.out.println("Enter a valid email address.");
            }
        }
    }

    /**
     Method asks for the customer's details and sends it to a Person object.
     Prompt for the email address is send to the email_format_check() method for input and verification.
     It prompts for the row number when buying a ticket. It then checks if the entered row number is valid and if it is,
     calls the for_Buy_and_Cancel() method or sends it to a "junk_input" variable if it isn't.
     */
    private static void buy_ticket()
    {
        String name, surname, email;
        System.out.print("Enter first name: ");
        name = input.next();

        System.out.print("Enter surname: ");
        surname = input.next();

        email= email_format_check();

        person = new Person(name, surname, email);

        String vacantSeat = ("Seat is available. Reserving seat...");
        String reservedSeat = ("Seat is unavailable.");
        while (!row_boolean)
        {
            System.out.print("Input row number: ");
            if (input.hasNextInt())
            {
                row = input.nextInt();
                switch (row)
                {
                    case 1:
                        //0 - when seat's available, 1 - when seat's unavailable. Above method.
                        buyTicket_and_cancelTicket(row, row1, vacantSeat, reservedSeat, 0, 1);
                        continue;
                    case 2:
                        buyTicket_and_cancelTicket( row, row2, vacantSeat, reservedSeat, 0, 1);
                        continue;
                    case 3:
                        buyTicket_and_cancelTicket( row, row3, vacantSeat, reservedSeat, 0, 1);
                        continue;
                    default:
                        System.out.println("Incorrect option. Enter a number from 1 to 3.");
                        break;
                }
            }
            else
            {
                System.out.println("Incorrect option. Enter a number from 1 to 3.");
                row = 1;
                String junk_input = input.next();      //incorrect input sent to "junk_input" variable
            }
        }
    }


    /**
     Method prints the stage with the stars and the seat layout.
     It calls the taskOffload_print_seating_area() method to print the 0s and Xs.
     */
    private static void print_seating_area()
    {
        System.out.print("\t\t");           //stage graphic
        for (i = 0; i < 9; i++)
        {
            System.out.print("*");          //first row of stars
        }
        System.out.println(" ");
        System.out.print("\t\t");
        System.out.println("*" + " " + "STAGE" + " " + "*");       //stage name
        System.out.print("\t\t");
        for (i = 0; i < 9; i++)
        {
            System.out.print("*");          //last row of stars
        }
        System.out.print("\n");



        System.out.print("      ");  //used to align row1 with stage
        taskOffload_print_seating_area(row1);
        System.out.print("    ");   //used to align row2 with stage
        taskOffload_print_seating_area(row2);
        System.out.print("  ");     //used to align row3 with stage
        taskOffload_print_seating_area(row3);
    }


    /**
     Method is called from print_seating_area() method and prints the 0s and Xs in the theatre layout which shows the
     available and reserved seats in the form of 0s and Xs.
     @param row Gets 0s and 1s from arrays for row1, row2 and row3 to be used in switch-case.
     */
    private static void taskOffload_print_seating_area(int[] row)   //tasks offloaded from print_seating_area()
    {
        for (i = 0; i < row.length / 2; i++)     //seats of row on the left
        {
            switch (row[i])
            {
                case 0:
                    System.out.print("O");    //case 0 - seat is available
                    continue;

                case 1:
                    System.out.print("X");    //case 1 - seat is occupied
            }
        }

        System.out.print(" ");
        for (i = row.length/2; i < row.length; i++)//seats of row on the right
        {
            switch (row[i])
            {
                case 0:
                    System.out.print("O");    //case 0 - seat available
                    continue;

                case 1:
                    System.out.print("X");    //case 1 - seat occupied
            }
        }
        System.out.print("\n");
    }


    /**
     Method to cancel tickets asks for a row number, uses a switch case to call the for_Buy_and_Cancel() method
     which takes care of the deleting of the ticket. Incorrect inputs are send to a "junk_input" variable.
     */
    private static void cancel_ticket()
    {
        String vacantSeat=("Error. Seat isn't reserved.");
        String occupiedSeat=("Seat is occupied. Canceling ticket...");
        while (!row_boolean)
        {
            System.out.print("Input row number: ");
            if (input.hasNextInt())
            {
                row = input.nextInt();
                switch (row)
                {
                    case 1:
                        buyTicket_and_cancelTicket( row, row1, occupiedSeat, vacantSeat, 1, 0);//0 - when seat's available, 1 - when seat's unavailable. Above method.
                        continue;
                    case 2:
                        buyTicket_and_cancelTicket( row, row2, occupiedSeat, vacantSeat, 1, 0);//0 - when seat's available, 1 - when seat's unavailable. Above method.
                        continue;
                    case 3:
                        buyTicket_and_cancelTicket( row, row3, occupiedSeat, vacantSeat, 1, 0);//0 - when seat's available, 1 - when seat's unavailable. Above method.
                        continue;
                    default:
                        System.out.println("Incorrect option. Enter a number from 1 to 3.");
                        break;
                }
            }
            else
            {
                System.out.println("Incorrect option. Enter a number from 1 to 3.");
                row = 1;
                String junk_input = input.next();//incorrect input sent to "junk_input" variable
            }
        }
    }



    /**
     Method shows the seat numbers of the available seats in the 3 rows. A 2-D array and an arraylist is used to create
     those rows.
     */
    private static void show_available()
    {
        String arraylist_toString;
        int j = 0;
        for (int[] row : rows)  //"rows" is the 2D array declared at the class level
        {
            j++;
            System.out.print("Seats available in row " + j + ": ");
            arraylist_showAvailable.clear();
            for(i = 0; i < row.length; i++)
            {
                if (row[i] == 0)
                {
                    arraylist_showAvailable.add( i + 1 );
                }
            }
            arraylist_toString = String.valueOf(arraylist_showAvailable);
            arraylist_toString = arraylist_toString.replaceAll("[\\[\\]]","");//arraylist's brackets are removed here
            System.out.println(arraylist_toString + ".");
        }
    }


    /**
     Method saves the 3 row arrays into a text file using FileWriter. A try-catch block will print a message
     if an error is encountered while saving.
     */
    private static void save()
    {
        try
        {
            FileWriter writerSeatSave = new FileWriter("Theatre Seat Reservations.txt");
            writerSeatSave.write(Arrays.toString(row1) + "\n" + Arrays.toString(row2) + "\n" + Arrays.toString(row3));
            writerSeatSave.close();
        }
        catch (IOException e)
        {
            System.out.println("An error has occurred.");
        }
    }



    /**
     Method extracts the row arrays from the stored text file and reads the arrays line by line.
     It then sends it to a String variable called string_for_load which then moves those values into
     an array called array_for_load which then enters those values into the 2-D array rows using a for-loop.
     */
    private static void load()
    {
        String string_for_load;
        String[] array_for_load;
        try
        {
            File file_read = new File("Theatre Seat Reservations.txt");
            Scanner file_reader = new Scanner(file_read);
            while (file_reader.hasNext())
            {
                for (int[] row: rows)     //"rows" is the 2D array declared at the class level
                {
                    string_for_load = file_reader.nextLine();
                    string_for_load = string_for_load.substring(1, string_for_load.length() - 1);
                    array_for_load = string_for_load.split(", ");   //separates the file's 0 and 1 values.
                    for (i = 0; i < array_for_load.length; i++)
                    {
                        row[i] = Integer.parseInt(array_for_load[i]);  //string's integer value is send to 2-D array.
                    }
                }
            }
            file_reader.close();
        }
        catch(IOException e){
            System.out.println("Error. Check for file in directory.");
        }
    }


    /**
     Method calls the Ticket class's print() method to print the tickets with all the information
     along with the price. The total is then calculated and displayed after the tickets are displayed.
     */
    private static void show_tickets_info()
    {
        i = 0;
        int total = 0;
        for (Ticket element: ArrayListTickets)
        {
            element.print();    //accesses print() method in the Ticket class.
            int Price_ticket=element.price;      //accesses price from the Ticket class
            total+=Price_ticket;    //creates total
        }
        System.out.print("\n");
        System.out.println("Total: Rs." + total);
    }


}