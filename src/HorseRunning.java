import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class HorseRunning extends RaceGUI {
    String msg = "";
    Font font = new Font("Arial", Font.BOLD, 28);
    int PANELWIDTH = 300;
    int PANELHEIGHT = 80;
    int TEXT_LOC_Y = 50;
    int DELAY=50;      // milliseconds
    int INCREMENT = 2; // amount in pixels to update x loc
    int startx = 0;
    Timer timer = new Timer(DELAY, (ae) -> repaint());
    boolean Winner;
    HorseGUI theHorse;


    public void start(HorseGUI horse, int StartX, int TrackLength, String TrackType, Color BorderColor) {
        msg = String.valueOf(horse.getSymbol());
        theHorse = horse;
        startx = StartX;
        this.Winner = false;
        theHorse.goBackToStart();
        PANELWIDTH = TrackLength;
        this.setForeground(Color.WHITE);

        this.setBorder(BorderFactory.createLineBorder(BorderColor));
        switch (TrackType) {
            case "hard Colour:DARK_GRAY" -> {
                this.setBackground(Color.DARK_GRAY);
                if (Objects.equals(theHorse.getStrengths(), "hard Colour:DARK_GRAY")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "hard Colour:DARK_GRAY")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "firm Colour:GRAY" -> {
                this.setBackground(Color.gray);
                if (Objects.equals(theHorse.getStrengths(), "firm Colour:GRAY")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "firm Colour:GRAY")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "good to firm Colour:LIGHT_GRAY" -> {
                this.setBackground(Color.lightGray);
                if (Objects.equals(theHorse.getStrengths(), "good to firm Colour:LIGHT_GRAY")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "good to firm Colour:LIGHT_GRAY")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "good Colour:GREEN" -> {
                this.setBackground(Color.green);
                if (Objects.equals(theHorse.getStrengths(), "good Colour:GREEN")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "good Colour:GREEN")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "good to soft Colour:YELLOW" -> {
                this.setBackground(Color.yellow);
                if (Objects.equals(theHorse.getStrengths(), "good to soft Colour:YELLOW")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "good to soft Colour:YELLOW")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "soft Colour:CYAN" -> {
                this.setBackground(Color.CYAN);
                if (Objects.equals(theHorse.getStrengths(), "soft Colour:CYAN")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "soft Colour:CYAN")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case "heavy Colour:BLUE" -> {
                this.setBackground(Color.BLUE);
                if (Objects.equals(theHorse.getStrengths(), "heavy Colour:BLUE")) {
                    INCREMENT = INCREMENT + theHorse.getPosMod();
                } else if (Objects.equals(theHorse.getWeaknesses(), "heavy Colour:BLUE")) {
                    INCREMENT = INCREMENT - theHorse.getNegMod();
                    if (INCREMENT <= 0) {
                        INCREMENT = 1;
                    }
                }
            }
            case null, default -> System.out.println("Invalid Track Type");
        }
        timer.start();

    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANELWIDTH, PANELHEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.drawString(msg, startx, TEXT_LOC_Y);
        if (Math.random() < theHorse.getConfidence() && !Winner && !theHorse.hasFallen())
        {
            g2d.drawString(msg, startx-PANELWIDTH, TEXT_LOC_Y);
            g2d.drawString(msg, startx+=INCREMENT, TEXT_LOC_Y);
        }

        if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
        {
            theHorse.fall();
            timer.stop();
            msg = "X";
            g2d.drawString("X", startx, TEXT_LOC_Y);
            this.setForeground(Color.RED);
        }


        FontMetrics fontMetrics = g2d.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(msg);
        if(startx >= PANELWIDTH - stringWidth && !theHorse.hasFallen()) {
            System.out.println("Winner");
            Winner = true;
            timer.stop();
        }

    }
}