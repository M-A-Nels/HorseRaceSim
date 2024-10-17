/**
 * The `Horse` class represents a horse in a horse race simulator
 * particularly the horse race in the associated Race Class.
 * It stores the horse's name, symbol in race, distance traveled, fallen status, and confidence level.
 * The class provides methods to update and retrieve the horse's information.
 *
 *
 * @author Madison Nelson
 * @version 1.0
 */
public class Horse {
    //Fields of class Horse
    private String HorseName;
    private char HorseSymbol;
    private int DistanceHorseTravelled;
    private boolean HasHorseFallen;
    private double HorseConfidence;

    //Constructor of class Horse
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.HorseName = horseName;
        this.HorseSymbol = horseSymbol;
        this.setConfidence(horseConfidence);
        this.DistanceHorseTravelled = 0;
        this.HasHorseFallen = false;
    }

    private void setDistanceHorseTravelled(int distanceHorseTravelled)
    {
        this.DistanceHorseTravelled = distanceHorseTravelled;
    }

    private void setHasHorseFallen(boolean hasHorseFallen)
    {
        this.HasHorseFallen = hasHorseFallen;
    }

    //Other methods of class Horse
    public void fall()
    {
        this.setHasHorseFallen(true);
    }

    public double getConfidence()
    {
        return this.HorseConfidence;
    }

    public int getDistanceTravelled()
    {
        return this.DistanceHorseTravelled;
    }

    public String getName()
    {
        return this.HorseName;
    }

    public char getSymbol()
    {
        return this.HorseSymbol;
    }

    public void goBackToStart()
    {
        this.setDistanceHorseTravelled(0);
        this.setHasHorseFallen(false);
    }

    public boolean hasFallen()
    {
        return this.HasHorseFallen;
    }

    public void moveForward()
    {
        this.setDistanceHorseTravelled(this.getDistanceTravelled() + 1);
    }

    public void setConfidence(double newConfidence)
    {
        if(newConfidence <= 0)
        {
            newConfidence = 0.1;
        }
        else if(newConfidence >= 1)
        {
            newConfidence = 1.0;
        }
        else
        {
            newConfidence = Math.floor(newConfidence*10.0)/10.0;
        }

        this.HorseConfidence = newConfidence;
    }

    public void setSymbol(char newSymbol)
    {
        this.HorseSymbol = newSymbol;
    }
}


