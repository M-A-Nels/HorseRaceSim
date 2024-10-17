import javax.swing.event.ChangeEvent;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;



public class RaceGUI extends JPanel implements ActionListener{


    static JFrame frame = new JFrame("Runner");
    boolean firstLoad = true;
    //Timer timer;
    int TrackLength = 100;
    int TrackLanes = 2;
    String TrackType = "hard Colour:DARK_GRAY";
    JTextField TypeText = new JTextField("Track Type: "+TrackType);
    HorseGUI[] Horses = new HorseGUI[10];
    ArrayList<HorseGUI> HorsesRacing = new ArrayList<>(TrackLanes);
    ArrayList<HorseRunning> HorsesRunning = new ArrayList<>();
    String[][] stats = new String[10][6];
    HashMap<String, Integer> bets = new HashMap<>();

    Color BorderColor = Color.BLACK;
    String BorderColorString = "Black";
    JTextField BorderText = new JTextField("Track Border:"+BorderColorString);


    public static void main(String[] args) {
        RaceGUI race = new RaceGUI();
        race.RaceTrack();
    }

    public void ReadFromHorses()
    {
        String file = "src/Horses.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < Horses.length) {
                String[] horseData = line.split(" ");
                String name = horseData[0];
                double confidence = Double.parseDouble(horseData[1]);
                char symbol = horseData[2].charAt(0);
                Horses[index] = new HorseGUI(symbol, name, confidence);
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file Horses.txt");
            e.printStackTrace();
        }
    }

    public void writetoHorses()
    {
        try {
            FileWriter fileWriter = new FileWriter("src/Horses.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(int a = 0; a < 10; a++)
            {
                bufferedWriter.write(Horses[a].getName() + " " + Horses[a].getConfidence() + " " + Horses[a].getSymbol());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException et) {
            et.printStackTrace();
        }
    }

    public void ReadFromStats()
    {
        String file = "src/HorsesStats.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < Horses.length) {
                String[] horseData = line.split(" ");
                for(int i = 0; i < 6; i++)
                {
                    stats[index][i] = horseData[i];
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file HorsesStats.txt");
            e.printStackTrace();
        }
    }

    public void writetoStats()
    {
        try {
            FileWriter fileWriter = new FileWriter("src/HorsesStats.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(int a = 0; a < 10; a++)
            {
                for(int j = 0; j < 6; j++)
                {
                    bufferedWriter.write(stats[a][j] + " ");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException et) {
            et.printStackTrace();
        }
    }

    public void processBetResults()
    {
        if(bets.isEmpty())
        {
            return;
        }
        else{
            StringBuilder message = new StringBuilder("Bet Results:\n");
            Set<String> keys = bets.keySet();
            for (String key : keys) {
                int betAmount = bets.get(key);
                for (HorseGUI horse : HorsesRacing) {
                    if (horse.getName().equals(key)) {
                        double odds = calculateOdds(horse);
                        if (HorsesRunning.get(HorsesRacing.indexOf(horse)).Winner) {
                            betAmount = (int) Math.round(betAmount * odds);
                        } else {
                            betAmount = 0;
                        }
                        message.append(key).append(": ").append(betAmount).append("\n");
                    }
                }

                bets.clear();
                JOptionPane.showMessageDialog(frame, message.toString(), "Bet Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }


    }
    public void RaceTrack(){

        ReadFromHorses();

        ReadFromStats();

        JPanel HomePanel = new JPanel();
        HomePanel.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 5, 5, 5));
        HomePanel.add(buttonsPanel, BorderLayout.NORTH);

        JTextField Data = new JTextField();
        Data.setEditable(false);
        HomePanel.add(Data, BorderLayout.SOUTH);

        JPanel RaceTrackPanel = new JPanel(new BorderLayout());



        JTextArea HomeTextField = new JTextArea();
        HomeTextField.setEditable(false);
        HomeTextField.setFont(new Font("Arial", Font.BOLD, 28));

        HomeTextField.setBackground(Color.LIGHT_GRAY);
        HomeTextField.setText("Home - Press Start for default race or customise track\n and horses before. \n You can even bet which horse will win.");


        HomePanel.add(HomeTextField, BorderLayout.CENTER);

        JButton Start = new JButton("Start");
        Start.setBackground(Color.GREEN);
        Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Home")){
                    HomePanel.removeAll();
                    HomePanel.updateUI();

                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(HomeTextField, BorderLayout.CENTER);
                    HomePanel.add(Data, BorderLayout.SOUTH);
                    Start.setText("Start");
                    Start.setBackground(Color.GREEN);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                else if(e.getActionCommand().equals("Start") && HorsesRacing.size() == TrackLanes)
                {
                    JPanel LanePanel = new JPanel(new GridLayout(TrackLanes, 1,0,5));
                    JPanel TrackPanel = new JPanel(new GridLayout(TrackLanes, 1, 0, 5));
                    RaceTrackPanel.add(LanePanel, BorderLayout.WEST);
                    RaceTrackPanel.add(TrackPanel, BorderLayout.CENTER);
                    HomePanel.removeAll();
                    HomePanel.updateUI();

                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(RaceTrackPanel, BorderLayout.CENTER);
                    HomePanel.add(Data, BorderLayout.SOUTH);

                    for(int i = 0; i < TrackLanes; i++)
                    {
                        JTextField Lane = new JTextField();
                        Lane.setEditable(false);
                        Lane.setText(HorsesRacing.get(i).getName()+" Confidence: "+HorsesRacing.get(i).getConfidence());
                        Lane.setFont(new Font("Arial", Font.BOLD, 12));
                        Lane.setHorizontalAlignment(JTextField.CENTER);
                        TrackPanel.add(Lane);
                        TrackPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        TrackPanel.setBackground(Color.LIGHT_GRAY);
                    }

                    for(int i = 0; i < TrackLanes; i++)
                    {
                        HorsesRunning.add(new HorseRunning());
                        LanePanel.add(HorsesRunning.get(i));
                    }

                    Start.setText("Race in Progress");
                    Start.setBackground(Color.RED);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    RaceTrackPanel.updateUI();
                    Data.setText("");
                    // double[] startTimes = new double[TrackLanes];
                    for(int i = 0; i < TrackLanes; i++)
                    {
                        HorsesRunning.get(i).start(HorsesRacing.get(i),0,TrackLength, TrackType, BorderColor);
                        //startTimes[i] = System.currentTimeMillis() / 1000.0;

                    }
                    //double[] endTimes = new double[TrackLanes];
                    Timer timer = new Timer(0, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            TrackPanel.removeAll();
                            for(int i = 0; i < TrackLanes; i++)
                            {
                                JTextField Lane = new JTextField();
                                Lane.setEditable(false);
                                Lane.setText(HorsesRacing.get(i).getName()+" Confidence: "+HorsesRacing.get(i).getConfidence());
                                TrackPanel.add(Lane);
                            }
                            TrackPanel.updateUI();


                            int winner = 0;
                            int fallen = 0;
                            ArrayList<Integer> winners = new ArrayList<>();
                            for(int Check = 0; Check < TrackLanes; Check++)
                            {
                                if(!HorsesRacing.get(Check).hasFallen())
                                {
                                    if(HorsesRunning.get(Check).Winner)
                                    {
                                        winner++;
                                        winners.add(Check);
                                    }
                                }
                                else
                                {
                                    fallen++;
                                }
                            }


                            if(fallen == TrackLanes)
                            {
                                Data.setText("All horses have fallen, no winner");
                                for(int j = 0; j < TrackLanes; j++)
                                {
                                    racewonby(HorsesRacing.get(j),false, winner);
                                    HorsesRunning.get(j).timer.stop();
                                    //Related to speed calculation
                                    // endTimes[j] = (int) System.currentTimeMillis()/ 1000.0;
                                    // for(int q =0; q< 10;q++)
                                    // {
                                    //     if(HorsesRacing.get(j).equals(Horses[q]))
                                    //     {
                                    //         CalcSpeed(startTimes[j], endTimes[j], q);
                                    //     }
                                    // }
                                }

                                ((Timer) e.getSource()).stop();
                                processBetResults();
                                writetoHorses();
                                writetoStats();
                                TrackPanel.removeAll();
                                for(int i = 0; i < TrackLanes; i++)
                                {
                                    JTextField Lane = new JTextField();
                                    Lane.setEditable(false);
                                    Lane.setText(HorsesRacing.get(i).getName()+" Confidence: "+HorsesRacing.get(i).getConfidence());
                                    TrackPanel.add(Lane);
                                }
                                TrackPanel.updateUI();
                                Start.setText("Start");
                            }

                            if(winner > 1)
                            {

                                Data.setText("It's a tie between: ");
                                for(int j = 0; j < TrackLanes; j++)
                                {
                                    if(HorsesRunning.get(j).Winner == true)
                                    {
                                        for(int k = 0; k < winners.size(); k++)
                                        {
                                            racewonby(HorsesRacing.get(k),true,winner);
                                        }
                                        Data.setText(Data.getText() + HorsesRacing.get(j).getName());
                                        if(j < TrackLanes-1)
                                        {
                                            Data.setText(Data.getText() + " and ");
                                        }
                                        else
                                        {
                                            Data.setText(Data.getText() + "!");
                                        }
                                        TrackPanel.removeAll();
                                        for(int i = 0; i < TrackLanes; i++)
                                        {
                                            JTextField Lane = new JTextField();
                                            Lane.setEditable(false);
                                            Lane.setText(HorsesRacing.get(i).getName()+" Confidence: "+HorsesRacing.get(i).getConfidence());
                                            TrackPanel.add(Lane);
                                        }
                                        TrackPanel.updateUI();
                                    }
                                }

                                for(int j = 0; j < TrackLanes; j++)
                                {
                                    HorsesRunning.get(j).timer.stop();
                                    //Related to speed calculation
                                    // endTimes[j] = (int) System.currentTimeMillis()/ 1000.0;
                                    // for(int q =0; q< 10;q++)
                                    // {
                                    //     if(HorsesRacing.get(j).equals(Horses[q]))
                                    //     {
                                    //         CalcSpeed(startTimes[j], endTimes[j], q);
                                    //     }
                                    // }
                                }
                                ((Timer) e.getSource()).stop();
                                processBetResults();
                                writetoHorses();
                                writetoStats();
                                Start.setText("Start");
                            }
                            else if (winner == 1)
                            {
                                for(int k = 0; k < winners.size(); k++)
                                {
                                    racewonby(HorsesRacing.get(k),true,winner);
                                }

                                Data.setText(HorsesRacing.get(winners.getFirst()).getName()+" is the winner");
                                for(int j = 0; j < TrackLanes; j++)
                                {
                                    HorsesRunning.get(j).timer.stop();
                                    //Related to speed calculation
                                    // endTimes[j] = (int) System.currentTimeMillis()/ 1000.0;
                                    // for(int q =0; q< 10;q++)
                                    // {
                                    //     if(HorsesRacing.get(j).equals(Horses[q]))
                                    //     {
                                    //         CalcSpeed(startTimes[j], endTimes[j], q);
                                    //     }
                                    // }

                                }
                                ((Timer) e.getSource()).stop();
                                processBetResults();
                                writetoHorses();
                                writetoStats();
                                TrackPanel.removeAll();
                                for(int i = 0; i < TrackLanes; i++)
                                {
                                    JTextField Lane = new JTextField();
                                    Lane.setEditable(false);
                                    Lane.setText(HorsesRacing.get(i).getName()+" Confidence: "+HorsesRacing.get(i).getConfidence());
                                    TrackPanel.add(Lane);
                                }
                                TrackPanel.updateUI();
                                Start.setText("Start");
                                Start.setBackground(Color.GREEN);
                                Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            }
                        }
                    });

                    timer.setRepeats(true);
                    timer.start();
                }
                else
                {
                    Data.setText("Please select the correct amount of horses to race");
                }
            }
        });
        buttonsPanel.add(Start);

        JPanel EditTrackPanel = SetUpEditTrack();

        JButton EditTrack = new JButton("Edit Track");
        EditTrack.setBackground(Color.pink);
        EditTrack.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        EditTrack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!Start.getText().equals("Race in Progress"))
                {
                    HomePanel.removeAll();
                    HomePanel.updateUI();
                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(EditTrackPanel, BorderLayout.CENTER);
                    Start.setText("Home");
                    Start.setBackground(Color.pink);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        });
        buttonsPanel.add(EditTrack);

        //JPanel EditHorsePanel = SetUpEditHorse();
        JButton EditHorse = new JButton("Edit Horse");
        EditHorse.setBackground(Color.pink);
        EditHorse.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        EditHorse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!Start.getText().equals("Race in Progress"))
                {
                    JPanel EditHorsePanel = SetUpEditHorse();
                    HomePanel.removeAll();
                    HomePanel.updateUI();
                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(EditHorsePanel, BorderLayout.CENTER);
                    Start.setText("Home");
                    Start.setBackground(Color.pink);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        });
        buttonsPanel.add(EditHorse);

        JButton Stats = new JButton("Stats");
        Stats.setBackground(Color.pink);
        Stats.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Stats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!Start.getText().equals("Race in Progress"))
                {
                    JPanel StatsPanel = SetUpEditStats();
                    HomePanel.removeAll();
                    HomePanel.updateUI();
                    StatsPanel.updateUI();
                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(StatsPanel, BorderLayout.CENTER);
                    Start.setText("Home");
                    Start.setBackground(Color.pink);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        });
        buttonsPanel.add(Stats);

        //JPanel BetPanel = SetUpEditBet();

        JButton Bet = new JButton("Bet");
        Bet.setBackground(Color.pink);
        Bet.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Bet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!Start.getText().equals("Race in Progress"))
                {
                    JPanel BetPanel = SetUpEditBet();
                    HomePanel.removeAll();
                    HomePanel.updateUI();
                    HomePanel.add(buttonsPanel, BorderLayout.NORTH);
                    HomePanel.add(BetPanel, BorderLayout.CENTER);
                    Start.setText("Home");
                    Start.setBackground(Color.pink);
                    Start.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        });
        buttonsPanel.add(Bet);


        HomePanel.add(buttonsPanel, BorderLayout.NORTH);

        frame.getContentPane().add(HomePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true );
        frame.setMinimumSize(new Dimension(800, 600));
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void racewonby(HorseGUI horse, boolean Won, int Winners)
    {
        boolean fallen = false;
        if(Won)
        {
            for(int j = 0; j < 10; j++)
            {
                if(stats[j][0].equals(horse.getName()))
                {
                    System.out.println(Horses[j].getConfidence());
                    Horses[j].setConfidence(Horses[j].getConfidence() + 0.1);
                    System.out.println(Horses[j].getConfidence());
                    if(Winners > 1)
                    {
                        stats[j][2] = Integer.toString(Integer.parseInt(stats[j][2])+1);
                    }
                    else
                    {
                        stats[j][1] = Integer.toString(Integer.parseInt(stats[j][1])+1);
                    }
                    stats[j][4] = Integer.toString(Integer.parseInt(stats[j][4])+1);
                    break;
                }
            }
        }
        else{

            if(horse.hasFallen())
            {
                fallen = true;
            }

            for(int k=0; k<TrackLanes; k++)
            {
                for(int j = 0; j < 10; j++)
                {
                    if(stats[j][0].equals(HorsesRacing.get(k).getName()))
                    {
                        if(fallen){
                            Horses[j].setConfidence(Horses[k].getConfidence() - 0.1);
                        }
                        stats[j][3] = Integer.toString(Integer.parseInt(stats[j][3])+1);
                        stats[j][4] = Integer.toString(Integer.parseInt(stats[j][4])+1);
                        break;
                    }
                }

            }
        }
    }

    public JPanel SetUpEditTrack()
    {
        JPanel EditTrackPanel = new JPanel(new GridLayout(8, 1, 0, 0));
        JPanel LengthPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        LengthPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JTextField LengthText = new JTextField();
        LengthText.setText("Track Length: "+TrackLength);
        LengthText.setEditable(false);
        JSlider Length = new JSlider(JSlider.HORIZONTAL, 1, 10 , 1);
        Length.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                TrackLength = source.getValue()*100;
                LengthText.setText("Track Length: "+TrackLength);
            }
        });

        LengthPanel.add(LengthText);
        LengthPanel.add(Length);
        JLabel lengthLabel = new JLabel("Track Length");
        EditTrackPanel.add(lengthLabel);
        EditTrackPanel.add(LengthPanel);

        JPanel LaneAmountPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        LaneAmountPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JTextField LaneText = new JTextField();
        LaneText.setText("Track Lanes: "+TrackLanes);
        LaneText.setEditable(false);
        JSlider Lane = new JSlider(JSlider.HORIZONTAL, 2, 5 , 2);
        Lane.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                TrackLanes = source.getValue();
                LaneText.setText("Track Lanes: "+TrackLanes);
            }

        });

        LaneAmountPanel.add(LaneText);
        LaneAmountPanel.add(Lane);
        JLabel LaneLabel = new JLabel("Track Lanes");
        EditTrackPanel.add(LaneLabel);
        EditTrackPanel.add(LaneAmountPanel);

        JPanel TypeAmountPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        TypeAmountPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        TypeText.setText("Track Type: "+TrackType);
        TypeText.setEditable(false);

        JRadioButton Type = new JRadioButton("hard Colour:DARK_GRAY");
        Type.setSelected(true);
        JRadioButton Type1 = new JRadioButton("firm Colour:GRAY");
        JRadioButton Type2 = new JRadioButton("good to firm Colour:LIGHT_GRAY");
        JRadioButton Type3 = new JRadioButton("good Colour:GREEN");
        JRadioButton Type4 = new JRadioButton("good to soft Colour:YELLOW");
        JRadioButton Type5 = new JRadioButton("soft Colour:CYAN");
        JRadioButton Type6 = new JRadioButton("heavy Colour:BLUE");

        ButtonGroup group = new ButtonGroup();
        group.add(Type);
        group.add(Type1);
        group.add(Type2);
        group.add(Type3);
        group.add(Type4);
        group.add(Type5);
        group.add(Type6);

        Type.addActionListener(this);
        Type1.addActionListener(this);
        Type2.addActionListener(this);
        Type3.addActionListener(this);
        Type4.addActionListener(this);
        Type5.addActionListener(this);
        Type6.addActionListener(this);

        JPanel TypePanel = new JPanel(new GridLayout(1, 7, 10, 0));
        TypePanel.add(Type);
        TypePanel.add(Type1);
        TypePanel.add(Type2);
        TypePanel.add(Type3);
        TypePanel.add(Type4);
        TypePanel.add(Type5);
        TypePanel.add(Type6);

        JLabel TypeLabel = new JLabel("Track Type");
        EditTrackPanel.add(TypeLabel);

        TypeAmountPanel.add(TypeText);
        TypeAmountPanel.add(TypePanel);

        EditTrackPanel.add(TypeAmountPanel);

        JLabel BorderLabel = new JLabel("Track Border");
        BorderText.setEditable(false);
        BorderText.setText("Track Border:"+BorderColorString);
        String[] BorderColors = {"Red", "Blue", "Green", "Yellow", "Black"};
        JPanel BorderColorPanels = new JPanel(new GridLayout(1, 5, 5, 5));
        for(int i = 0; i < 5; i++)
        {
            JButton BorderButton = new JButton(BorderColors[i]);
            BorderButton.addActionListener(this);
            BorderColorPanels.add(BorderButton);
        }

        JPanel Border = new JPanel(new GridLayout(2, 1, 0, 0));
        Border.add(BorderText);
        Border.add(BorderColorPanels);
        Border.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        EditTrackPanel.add(BorderLabel);
        EditTrackPanel.add(Border);

        EditTrackPanel.setBackground(Color.orange);

        return EditTrackPanel;
    }

    public JPanel SetUpEditHorse()
    {

        JPanel EditHorsePanel = new JPanel(new BorderLayout());
        JPanel Display = new JPanel(new GridLayout(11, 1, 0, 0));

        JPanel Labels = new JPanel(new GridLayout(1, 7, 0, 0));
        JLabel RACING = new JLabel("RACING");
        JLabel HorseLabel = new JLabel("Horse Name");
        JLabel ConfidenceLabel = new JLabel("Confidence");
        JLabel StrengthLabel = new JLabel("Strengths");
        JLabel WeaknessLabel = new JLabel("Weakness");
        JLabel Symbol = new JLabel("Symbol");
        JLabel Edit = new JLabel("Edit");

        Labels.add(RACING);
        Labels.add(HorseLabel);
        Labels.add(ConfidenceLabel);
        Labels.add(StrengthLabel);
        Labels.add(WeaknessLabel);
        Labels.add(Symbol);
        Labels.add(Edit);

        Display.add(Labels);

        // Read from text file
        try {
            FileReader fileReader = new FileReader("src/Horses.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int Written = 0;
            int HorseCount = 0;
            // Loop through each line in the text file
            while ((line = bufferedReader.readLine()) != null) {

                // Split the line into different values
                String[] values = line.split(" ");

                // Create components for each value
                JCheckBox HorseRacing = new JCheckBox("Racing");
                if (Written < TrackLanes && firstLoad) {
                    HorseRacing.setSelected(true);
                    HorsesRacing.add(Horses[HorseCount]);
                    Written++;
                } // Set the value of the check box to true

                if(!firstLoad)
                {
                    if(HorsesRacing.contains(Horses[HorseCount]))
                    {
                        HorseRacing.setSelected(true);
                    }
                }

                JTextField horseName = new JTextField(values[0]);
                horseName.setEditable(false); // Set the text field to be uneditable
                JTextField horseConfidence = new JTextField(values[1]);
                horseConfidence.setEditable(false);

                String strengths = Horses[HorseCount].getStrengths();
                int firstOccurrence = strengths.indexOf("Colour");
                String modifiedStrengths = strengths.substring(0, firstOccurrence);
                JTextField horseStrengthField = new JTextField(modifiedStrengths);
                horseStrengthField.setEditable(false);

                String weaknesses = Horses[HorseCount].getWeaknesses();
                firstOccurrence = weaknesses.indexOf("Colour");
                String modifiedWeaknesses = weaknesses.substring(0, firstOccurrence);
                JTextField horseWeaknessField = new JTextField(modifiedWeaknesses);
                horseWeaknessField.setEditable(false);
                JTextField horseSymbol = new JTextField(values[2]);
                horseSymbol.setEditable(false);
                JButton HorseEdit = new JButton("Edit");
                HorseEdit.setBackground(Color.cyan);

                // Add components to the panel
                JPanel horsePanel = new JPanel(new GridLayout(1, 7, 0, 0));
                horsePanel.add(HorseRacing);
                horsePanel.add(horseName);
                horsePanel.add(horseConfidence);
                horsePanel.add(horseStrengthField);
                horsePanel.add(horseWeaknessField);
                horsePanel.add(horseSymbol);
                horsePanel.add(HorseEdit);
                Display.add(horsePanel);

                JPanel SymbolPanel = new JPanel(new GridLayout(2, 1));
                JPanel Symbolsbuttons = new JPanel(new GridLayout(2, 6, 5, 5));
                JLabel Symbols = new JLabel("Horse Symbols");
                String[] SymbolsArray = {"¢", "¤", "»", "#", "©", "®", "±", "¥", "§", "^"};
                for(int i = 0; i < 10; i++)
                {
                    JButton SymbolButton = new JButton(SymbolsArray[i]);
                    SymbolButton.setBackground(Color.cyan);
                    SymbolButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            horseSymbol.setText(e.getActionCommand());
                        }
                    });
                    Symbolsbuttons.add(SymbolButton);
                }

                SymbolPanel.add(Symbols);
                SymbolPanel.add(Symbolsbuttons);

                EditHorsePanel.add(Display, BorderLayout.CENTER);

                HorseEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(e.getActionCommand().equals("Save"))
                        {
                            HorseEdit.setText("Edit");
                            horseName.setEditable(false);
                            EditHorsePanel.remove(SymbolPanel);

                            try {
                                FileWriter fileWriter = new FileWriter("Part2\\Horses.txt");
                                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                                // Loop through each horse panel
                                for (Component component : Display.getComponents()) {
                                    if (component instanceof JPanel) {
                                        JPanel horsePanel = (JPanel) component;

                                        // Get the components of the horse panel
                                        Component[] components = horsePanel.getComponents();

                                        // Get the values from the components
                                        try {
                                            JTextField horseName = (JTextField) components[1];
                                            JTextField horseConfidence = (JTextField) components[2];
                                            JTextField horseSymbol = (JTextField) components[5];

                                            // Write the values to the file
                                            bufferedWriter.write(horseName.getText() + " " + horseConfidence.getText() + " " + horseSymbol.getText());
                                            bufferedWriter.newLine();
                                        } catch (Exception ex) {
                                            continue;
                                        }
                                    }
                                }

                                bufferedWriter.close();
                            } catch (IOException et) {
                                et.printStackTrace();
                            }
                        }
                        else{
                            HorseEdit.setText("Save");
                            horseName.setEditable(true);
                            EditHorsePanel.add(SymbolPanel, BorderLayout.SOUTH);
                        }
                    }


                });
                int HorsePointer = HorseCount;
                HorseRacing.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (HorseRacing.isSelected()) {
                            if(HorsesRacing.size() == TrackLanes)
                            {
                                HorseRacing.setSelected(false);
                                return;
                            }
                            else{
                                // Add the horse to the racing horse list
                                HorsesRacing.add(Horses[HorsePointer]);
                            }

                        } else {
                            // Remove the horse from the racing horse list
                            try {
                                HorsesRacing.remove(Horses[HorsePointer]);
                            } catch (Exception ex) {
                                return;
                            }
                        }
                    }
                });
                HorseCount++;
            }

            firstLoad = false;
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return EditHorsePanel;
    }

    //Does not work as wanted removeing for now
    // public void CalcSpeed(double startTime, double endTime, int position)
    // {
    //     int gamesPlayed = Integer.parseInt(stats[position][4]);
    //     double oldspeed = Double.parseDouble(stats[position][5]);
    //     double speed = oldspeed * (gamesPlayed - 1);
    //     speed = speed + (endTime - startTime);
    //     speed = speed / gamesPlayed;
    //     speed = Math.round(speed * 100.0) / 100.0;
    //     stats[position][5] = Double.toString(speed);
    // }
    public JPanel SetUpEditStats()
    {
        JPanel StatsPanel = new JPanel(new FlowLayout());
        StatsPanel.setBackground(Color.orange);
        JTextField Stats = new JTextField("Horse Stats");
        Stats.setEditable(false);
        Stats.setFont(new Font("Arial", Font.BOLD, 28));
        StatsPanel.add(Stats);

        try {
            FileReader fileReader = new FileReader("src/HorsesStats.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int HorseCount = 0;
            while ((line = bufferedReader.readLine()) != null) {

                JPanel HorsePanel = new JPanel(new GridLayout(2, 1, 0, 0));
                JTextField Name = new JTextField();
                Name.setEditable(false);
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);

                // Split the line into different values
                String[] values = line.split(" ");
                Name.setText(values[0]);
                Name.setFont(new Font("Arial", Font.BOLD, 16));
                Name.setHorizontalAlignment(JTextField.CENTER);
                HorsePanel.add(Name);
                textArea.append("Wins: " + values[1] + "\n");
                textArea.append("Draws: " + values[2] + "\n");
                textArea.append("Losses: " + values[3] + "\n");
                textArea.append("Ganes Played: " + values[4] + "\n");
                //textArea.append("Average Speed: " + values[5] + "\n");
                textArea.append("Modifer Postive: " + Horses[HorseCount].getPosMod() + "\n");
                textArea.append("Modifer Negative: " + Horses[HorseCount].getNegMod() + "\n");

                HorsePanel.add(textArea);
                StatsPanel.add(HorsePanel);
                HorseCount++;
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return StatsPanel;
    }

    public double calculateOdds(HorseGUI shorse) {
        double odds = 0;
        for (HorseGUI horse : HorsesRacing) {
            if (horse.equals(shorse)) {
                odds = 1 / shorse.getConfidence();
            }
        }

        if(TrackType.equals(shorse.getStrengths()))
        {
            odds = 1/ (shorse.getConfidence()/ shorse.getPosMod());
        }
        else if(TrackType.equals(shorse.getWeaknesses()))
        {
            odds = 1/ (shorse.getConfidence() * shorse.getNegMod() *shorse.getNegMod());
        }

        return odds;
    }

    public JPanel SetUpEditBet()
    {
        JPanel BetPanel = new JPanel(new GridLayout(4, 2, 0, 0));

        JLabel horseSelectionLabel = new JLabel("Horse Selection:");
        horseSelectionLabel.setHorizontalAlignment(JLabel.CENTER);
        horseSelectionLabel.setVerticalAlignment(JLabel.CENTER);
        horseSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        horseSelectionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JComboBox<String> horseSelectionComboBox = new JComboBox<String>();
        horseSelectionComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Add horses in race options to the combo box
        for (HorseGUI horse : HorsesRacing) {
            horseSelectionComboBox.addItem(horse.getName());
        }

        JLabel oddsLabel = new JLabel("Odds:");
        oddsLabel.setHorizontalAlignment(JLabel.CENTER);
        oddsLabel.setVerticalAlignment(JLabel.CENTER);
        oddsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        oddsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextField oddsTextField = new JTextField();
        oddsTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        oddsTextField.setFont(new Font("Arial", Font.BOLD, 16));
        oddsTextField.setHorizontalAlignment(JTextField.CENTER);
        oddsTextField.setEditable(false);

        horseSelectionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedHorse = (String) horseSelectionComboBox.getSelectedItem();
                HorseGUI selectedhorse = null;
                for (HorseGUI horse : HorsesRacing)
                { if (horse.getName().equals(selectedHorse))
                { selectedhorse = horse;
                    break; }
                }
                double odds = calculateOdds(selectedhorse);
                oddsTextField.setText(Double.toString(odds)+"of winning");
            }
        });

        JLabel betAmountLabel = new JLabel("Bet Amount:");
        betAmountLabel.setHorizontalAlignment(JLabel.CENTER);
        betAmountLabel.setVerticalAlignment(JLabel.CENTER);
        betAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        betAmountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JTextField betAmountTextField = new JTextField();
        betAmountTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        betAmountTextField.setFont(new Font("Arial", Font.BOLD, 16));

        BetPanel.add(horseSelectionLabel);
        BetPanel.add(horseSelectionComboBox);
        BetPanel.add(oddsLabel);
        BetPanel.add(oddsTextField);
        BetPanel.add(betAmountLabel);
        BetPanel.add(betAmountTextField);
        //Horse Selection - odds display
        //Bet Amount -
        JButton placeBetButton = new JButton("Place Bet");
        placeBetButton.setBackground(Color.GREEN);
        BetPanel.add(placeBetButton);

        JTextArea CurrentBets = new JTextArea();
        CurrentBets.setEditable(false);
        CurrentBets.setFont(new Font("Arial", Font.BOLD, 16));



        placeBetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double betAmount;
                String selectedHorse = (String) horseSelectionComboBox.getSelectedItem();
                HorseGUI selectedhorse = null;
                for (HorseGUI horse : HorsesRacing)
                { if (horse.getName().equals(selectedHorse))
                { selectedhorse = horse;
                    break; }
                }
                double odds = calculateOdds(selectedhorse);
                try {
                    betAmount = Double.parseDouble(betAmountTextField.getText());

                    if (betAmount < 0) {
                        JOptionPane.showMessageDialog(null, "Invalid bet amount. Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else
                    {
                        CurrentBets.setText("");
                        bets.put(selectedHorse, (int) betAmount);
                        StringBuilder message = new StringBuilder("Bet Results:\n");
                        Set<String> keys = bets.keySet();
                        for (String key : keys) {
                            betAmount = bets.get(key);
                            message.append(key).append(": ").append(betAmount).append("\n");
                        }
                        CurrentBets.setText(message.toString());
                        BetPanel.add(CurrentBets);



                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid bet amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });







        return BetPanel;
    }



    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();

        if (command.equals("Red"))
        {
            BorderColorString = command;
            BorderColor = Color.RED;
        }
        else if (command.equals("Blue"))
        {
            BorderColorString = command;
            BorderColor = Color.BLUE;
        }
        else if (command.equals("Green"))
        {
            BorderColorString = command;
            BorderColor = Color.GREEN;
        }
        else if (command.equals("Yellow"))
        {
            BorderColorString = command;
            BorderColor = Color.YELLOW;
        }
        else if (command.equals("Black"))
        {
            BorderColorString = command;
            BorderColor = Color.BLACK;
        }
        else
        {
            TrackType = command;
            TypeText.setText("Track Type: " + TrackType);


        }
        BorderText.setText("Track Border: "+BorderColorString);

    }
}