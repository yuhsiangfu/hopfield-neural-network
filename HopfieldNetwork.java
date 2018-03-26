/***********************************/
/* Project 4                       */
/* Hopfield network                */
/*---------------------------------*/
/* Date:090513                     */
/*---------------------------------*/
/* Program by Fu,Yu-Hsiang in Aizu */
/***********************************/
//Import
import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;//import AWT
import java.awt.event.*;
import javax.swing.*;//import Swing

public class HopfieldNetwork{
    private String pathin;
    private String[] pattern_name;
    private boolean nosied;
    private int N,M,pattern_row,pattern_column;
    private int[] v;//state
    private int[][] pattern,w;//weight
    private double noise_rate,miss_rate,quality_average,quality_similarity;//noise, miss and quality rate;

    //GUI Component
    private JFrame jframe;
    private JTextArea jtarea;
    private JScrollPane jspane;
    private Container ccontrolpanel;
    private JPanel jpparameter,jpbutton;
	private JLabel jlrow,jlcolumn,jltype;
	private JTextField jtfrow,jtfcolumn,jtfnoisy,jtfmiss;
	private JCheckBox jcbnoisy,jcbmiss;
    private JButton jbrun,jbexit;

    public HopfieldNetwork(){
        //GUIInterface
        GUIInterface();
    }

    //GUIInterface
    private void GUIInterface(){
	    //Title Information
	    jframe = new JFrame("Aizu - Neural Network I: Fundamental Theory and Applications - Project 4");
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setLayout(new BorderLayout(2,2));
        jframe.setSize(625,300);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//Locate at center
	    jframe.setLocation(((screenSize.width-jframe.getSize().width)/2),
                                   ((screenSize.height-jframe.getSize().height)/2));

        //Container
		ccontrolpanel = new Container();
		ccontrolpanel.setLayout(new GridLayout(2,1));
		jframe.add(ccontrolpanel,BorderLayout.EAST);

	    //Display Area
	    jtarea = new JTextArea("Aizu - Neural Network I: Fundamental Theory and Applications - Project 4");
	    jtarea.append("\n--");
	    jtarea.append("\nHopfield neural netwrok.");
	    jtarea.append("\nProgram by Fu, Yu-Hsiang (m5128110), 05/15/2009 in Aizu.");
	    jtarea.append("\n--");
	    jtarea.setEditable(false);
	    jtarea.setDisabledTextColor(Color.BLACK);

		jspane = new JScrollPane(jtarea);//Scroll
		jframe.add(jspane,BorderLayout.CENTER);

		//Parameter Panel
		jpparameter = new JPanel(new GridLayout(5,1,2,2));
		jpparameter.setBorder(BorderFactory.createTitledBorder("Parameter"));
		ccontrolpanel.add(jpparameter);

		//Neuron row
		Box brow = Box.createHorizontalBox();
		jlrow = new JLabel("Neuron row:");
		jtfrow = new JTextField();
		jtfrow.setEditable(false);
		jtfrow.setColumns(6);
		brow.add(jlrow);
		brow.add(jtfrow);
        jpparameter.add(brow);

		//Neuron column
		Box bcolumn = Box.createHorizontalBox();
		jlcolumn = new JLabel("Neuron column:");
		jtfcolumn = new JTextField();
		jtfcolumn.setEditable(false);
		jtfcolumn.setColumns(6);
		bcolumn.add(jlcolumn);
		bcolumn.add(jtfcolumn);
        jpparameter.add(bcolumn);

		//Type
		jltype = new JLabel("Type:");
		jpparameter.add(jltype);

		//Noisy
		Box bnoisy = Box.createHorizontalBox();
		jcbnoisy = new JCheckBox("Noisy:");
	    Action anoisy = new AbstractAction(){
	        public void actionPerformed(ActionEvent evt){
	            JCheckBox jcb = (JCheckBox)evt.getSource();
	            if (jcb.isSelected()){
                    jcbmiss.setSelected(false);
                    jtfnoisy.setEditable(true);
                    jtfmiss.setEditable(false);
                    jbrun.setEnabled(true);

                    nosied=true;
	            }
	            else{
                    jtfnoisy.setEditable(false);
                    jcbmiss.setSelected(true);
                    jtfmiss.setEditable(true);

                    nosied=false;
                }
	        }
	    };
        jcbnoisy.addActionListener(anoisy);

		jtfnoisy = new JTextField();
		jtfnoisy.setEditable(false);
		jtfnoisy.setColumns(6);
		bnoisy.add(jcbnoisy);
		bnoisy.add(jtfnoisy);
        jpparameter.add(bnoisy);

		//Miss
		Box bmiss = Box.createHorizontalBox();
		jcbmiss = new JCheckBox("Miss:");
	    Action amiss = new AbstractAction(){
	        public void actionPerformed(ActionEvent evt){
	            JCheckBox jcb = (JCheckBox)evt.getSource();
	            if (jcb.isSelected()){
    	            jcbnoisy.setSelected(false);
                    jtfnoisy.setEditable(false);
                    jtfmiss.setEditable(true);
                    jbrun.setEnabled(true);

                    nosied=false;
	            }
	            else{
                    jcbnoisy.setSelected(true);
                    jtfnoisy.setEditable(true);
                    jtfmiss.setEditable(false);

                    nosied=true;
                }
	        }
	    };
        jcbmiss.addActionListener(amiss);

		jtfmiss = new JTextField();
		jtfmiss.setEditable(false);
		jtfmiss.setColumns(6);
		bmiss.add(jcbmiss);
		bmiss.add(jtfmiss);
        jpparameter.add(bmiss);

		//Control Panel
		jpbutton = new JPanel(new GridLayout(2,1,2,2));
		jpbutton.setBorder(BorderFactory.createTitledBorder("Control"));
		ccontrolpanel.add(jpbutton);

		//Run,Exit
        jbrun = new JButton("HNN Go!");
		jbrun.setEnabled(false);
	    jbrun.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
                    jtarea.setText("");
                    jtarea.append("Aizu - Neural Network I: Fundamental Theory and Applications - Project 4");
                    jtarea.append("\n--");
                    jtarea.append("\nHopfield neural netwrok.");
                    jtarea.append("\nProgram by Fu, Yu-Hsiang (m5128110), 05/15/2009 in Aizu.");
                    jtarea.append("\n--");

                    jtarea.append("\nHopfield Neurnal Network");
                    Initialization();//Initialization

                    jtarea.append("\n-Parameters:");
                    ShowParameter();

                    StorePhase();//StorePhase

                    RecallPhase();//RecallPhase
				}
			}
		);
        jbexit = new JButton("Exit");
	    jbexit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					JOptionPane.showMessageDialog(null,"Have a nice day! Bye~ : )");
					System.exit(0);
				}
			}
		);
        jpbutton.add(jbrun);
        jpbutton.add(jbexit);

	    //SetParameter
		jtfrow.setText("10");
		jtfcolumn.setText("12");
		jtfnoisy.setText("0.15");
		jtfmiss.setText("0.15");

	    //Show jframe
	    jframe.setVisible(true);
    }

    //Initialization
    private void Initialization(){
        pathin="input\\";

        pattern_row=Integer.parseInt(jtfrow.getText());
        pattern_column=Integer.parseInt(jtfcolumn.getText());
        N=pattern_row*pattern_column;//Total neurons

        //nosied=true;
        noise_rate=Double.parseDouble(jtfnoisy.getText());//noise
        miss_rate=Double.parseDouble(jtfmiss.getText());//miss
        quality_average=0;

        File f = new File(pathin);
        pattern_name = f.list();//Get list of patterns
        M = pattern_name.length;//Total patterns
        pattern = new int[M][pattern_row*pattern_column];//Patterns

        LoadPattern();//Load patterns data

        w = new int[N][N];//Initial weight
        v = new int[N];//State
    }

    //Load Pattern
    private void LoadPattern(){
        try{
            BufferedReader br;
            for(int a=0;a<M;a++){
                br = new BufferedReader(new FileReader(pathin+pattern_name[a]));
                for(int b=0;b<pattern_row;b++){
                    StringTokenizer st = new StringTokenizer(br.readLine());
                    for(int c=0;c<pattern_column;c++){
                        pattern[a][(b*pattern_column+c)]=Integer.parseInt(st.nextToken());
                    }
                }
                br.close();
            }
        }
        catch(Exception e){
            System.out.println("HopfieldNetwork-Initialization-LoadPattern:¨Ò¥~¿ù»~!!");
        }
    }

    //Stroe Phase
    private void StorePhase(){
        for(int a=0;a<N;a++){
            for(int b=0;b<N;b++){
                for(int c=0;c<M;c++){//i!=j
                    w[a][b]+=(2*pattern[c][a]-1)*(2*pattern[c][b]-1);//w[i][j]=(2*s[m][i]-1)*(2*s[m][j]-1),{0,1}
                }
            }
            w[a][a]=0;//i=j
        }
    }

    //Recall Phase
    private void RecallPhase(){
        for(int a=0;a<M;a++){
            InitialState(a);//Initial state
            RecallPattern();
            RecallQuality(a);//Quality of recalled pattern
        }
        ShowRecallQuality();//Recall quality
    }

    //InitialState
    private void InitialState(int m){
        jtarea.append("\n====");
        jtarea.append("\nOrginal pattern["+m+"]:");
        jtarea.append("\n====");
        ShowPattern(m);

        Random r = new Random(System.currentTimeMillis());//Randomize

        if(nosied){//Nosing pattern
            for(int a=0;a<N;a++){//Noised initial state
                if(r.nextDouble()<noise_rate){
                    if(pattern[m][a]==1){
                        v[a]=0;
                    }
                    else{
                        v[a]=1;
                    }
                }
                else{
                    v[a]=pattern[m][a];
                }
            }
        }
        else{//Missing pattern
            for(int a=0;a<N;a++){//Noised initial state
                if(pattern[m][a]==0){
                    if(r.nextDouble()<miss_rate){
                        v[a]=1;
                    }
                    else{
                        v[a]=0;
                    }
                }
                else{
                    v[a]=pattern[m][a];
                }
            }
        }

        jtarea.append("----");
        jtarea.append("\nInitial state of pattern["+m+"],Energy:"+EnergyFunction());
        jtarea.append("\n----");
        ShowState();
    }

    //RecallPattern
    private void RecallPattern(){
        boolean doWhile=true;
        int iteration=1;

        while(doWhile){//Recalling
            jtarea.append("----");
            jtarea.append("\nRecall of "+iteration+"-th state,");
            int stateChange=0;

            for(int a=0;a<N;a++){
                int v_new=0,net=0;

                for(int b=0;b<N;b++){//Calculate each net[i]
                    net+=w[a][b]*v[b];//w[i][j]*v^k[j]
                }

                if(net>=0){//Next state of v[i]
                    v_new=1;
                }
                if(net<0){
                    v_new=0;
                }

                if(v_new!=v[a]){
                    stateChange++;
                    v[a]=v_new;
                }
            }

            jtarea.append("Energy:"+EnergyFunction());
            jtarea.append("\n----");
            ShowState();

            if(stateChange==0){//if Converge(stable)?
                doWhile=false;
            }

            iteration++;
        }
    }

    //EnergyFunction
    private double EnergyFunction(){
        double E=0;

        for(int a=0;a<N;a++){
            for(int b=0;b<N;b++){
                if(a!=b){
                    E+=w[a][b]*v[a]*v[b];
                }
            }
        }

        return ((-0.5)*E);
    }

    //RecallQuality
    private void RecallQuality(int m){
        NEuclideanDistance NED = new NEuclideanDistance(N);//Euclidean distance

        int[] rowA = new int[N];

        for(int a=0;a<N;a++){//Set rowA
            rowA[a]=pattern[m][a];
        }

        quality_similarity=0;
        quality_similarity=NED.Calculate(rowA,v);//S(Pattern,state)
        quality_average+=quality_similarity;

        jtarea.append("\nQuality:"+Math.round((quality_similarity*10000.0))/100.0+"%");
    }

    //ShowParameter
    private void ShowParameter(){
        jtarea.append("\nNeurons:"+N);
        jtarea.append("\n  -row:"+pattern_row);
        jtarea.append("\n  -column:"+pattern_column);
        jtarea.append("\nNoisy rate:"+noise_rate);
        jtarea.append("\nMiss  rate:"+miss_rate);
        jtarea.append("\nTotal patterns:"+M);
        for(int a=0;a<M;a++){
            jtarea.append("\n  -Pattern["+a+"]:"+pattern_name[a]);
        }
    }

    //ShowPattern
    private void ShowPattern(){
        for(int a=0;a<M;a++){
            ShowPattern(a);
        }
    }

    private void ShowPattern(int m){
        jtarea.append("\n");
        for(int a=0;a<pattern_row;a++){
            for(int b=0;b<pattern_column;b++){
                if(pattern[m][(a*pattern_column+b)]==1){
                    jtarea.append("¡@");//1,White
                }
                else{
                    jtarea.append("¡½");//-1,Black
                }
            }
            jtarea.append("\n");
        }
    }

    //ShowState
    private void ShowState(){
        jtarea.append("\n");
        for(int a=0;a<pattern_row;a++){
            for(int b=0;b<pattern_column;b++){
                if(v[(a*pattern_column+b)]==1){
                    jtarea.append("¡@");//1,White
                }
                else{
                    jtarea.append("¡½");//-1 or 0,Black
                }
            }
            jtarea.append("\n");
        }
    }

    //ShowQuality
    private void ShowRecallQuality(){
        String result="";
        quality_average=(quality_average/(double)M);

        if(quality_average>=0.9){//A
            result="A";
        }
        else if(quality_average>=0.8){//B
            result="B";
        }
        else if(quality_average>=0.7){//C
            result="C";
        }
        else if(quality_average>=0.6){//D
            result="D";
        }
        else if(quality_average>=0.5){//E
            result="E";
        }
        else{//F
            result="F";
        }

        jtarea.append("\n--");
        jtarea.append("\nAverage recall quality: "+Math.round((quality_average*10000.0))/100.0+"%, "+result);
    }

    public static void main(String args[]){
        HopfieldNetwork hn = new HopfieldNetwork();
    }
}