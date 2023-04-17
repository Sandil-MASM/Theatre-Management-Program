public class Ticket
{
    int row;
    int seat;
    int price;
    Person Person;

    public Ticket(int row,int seat, Person Person,int price)
    {
        this.row=row;
        this.seat=seat;
        this.Person =Person;
        this.price=price;
    }

    /** @return price */
    public int getPrice()
    {
        return price;
    }

    /** @return row */
    public int getRow()
    {
        return row;
    }

    /** @return seat */
    public int getSeat()
    {
        return seat;
    }

    /** Prints the tickets for the Theatre class. */
    public void print()
    {
        System.out.println("Name: " + Person.name + " " + Person.surname);
        System.out.println("Email: " + Person.email);
        System.out.println("Row:" + row + " Seat:" + seat + " Price:Rs." + price);
        System.out.print("\n");
    }
}
