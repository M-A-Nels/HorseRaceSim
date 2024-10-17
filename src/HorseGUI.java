
/**
 * The `HorseGUI` class represents a horse in a horse race simulator
 * particularly the horse race in the associated Race Class.
 * It stores the horse's name, symbol in race, distance traveled, fallen status, and confidence level.
 * The class provides methods to update and retrieve the horse's information.
 * Version Works with gui class for RaceGUI.java
 *
 * @author Madison Nelson
 * @version 2.0
 */

public class HorseGUI
{
    //Fields of class Horse
    private String HorseName;
    private char HorseSymbol;
    private int DistanceHorseTravelled;
    private boolean HasHorseFallen;
    private double HorseConfidence;
    private String Strengths;
    private String Weaknesses;
    private int PosMod;
    private int NegMod;


    //Constructor of class Horse
    /**
     * Constructor for objects of class HorseGUI
     */
    public HorseGUI(char horseSymbol, String horseName, double horseConfidence)
    {
        this.HorseName = horseName;
        this.HorseSymbol = horseSymbol;
        this.setConfidence(horseConfidence);
        this.DistanceHorseTravelled = 0;
        this.HasHorseFallen = false;
        this.Strengths = setStrengths();
        this.Weaknesses = setWeaknesses();
        this.PosMod = setPosMod();
        this.NegMod = setNegMod();

    }

    private String setStrengths()
    {
        String[] tracks = {"hard Colour:DARK_GRAY", "firm Colour:GRAY", "good to firm Colour:LIGHT_GRAY", "good Colour:GREEN", "good to soft Colour:YELLOW", "soft Colour:CYAN", "heavy Colour:BLUE"};
        int randomIndex = (int) (Math.random() * tracks.length);
        return tracks[randomIndex];
    }

    private String setWeaknesses()
    {
        String[] tracks = {"hard Colour:DARK_GRAY", "firm Colour:GRAY", "good to firm Colour:LIGHT_GRAY", "good Colour:GREEN", "good to soft Colour:YELLOW", "soft Colour:CYAN", "heavy Colour:BLUE"};
        int randomIndex = (int) (Math.random() * tracks.length);
        while(tracks[randomIndex] == this.Strengths)
        {
            randomIndex = (int) (Math.random() * tracks.length);
        }
        return tracks[randomIndex];
    }

    private int setPosMod()
    {
        return (int) (Math.random() * 4);
    }

    private int setNegMod()
    {
        return (int) (Math.random() * 4);
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
    public String getStrengths()
    {
        return this.Strengths;
    }

    public String getWeaknesses()
    {
        return this.Weaknesses;
    }

    public int getPosMod()
    {
        return this.PosMod;
    }

    public int getNegMod()
    {
        return this.NegMod;
    }

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