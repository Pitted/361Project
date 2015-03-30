
import java.io.*;
import java.util.*;
public class ChronoTimer {

	protected static boolean power;   //true = on, false = off
	private static Channel [] channels = {new Channel(1), new Channel(2)};  //2 channels for first sprint, initialized to 8 disarmed channels	
	protected static ArrayList<Competitor> toStart = new ArrayList<Competitor>();  //the racers who have not yet started
	protected static Queue<Competitor> toFinish  = new LinkedList<Competitor>();   //the racers who have started but not finished yet
	protected static ArrayList<Competitor> completedRacers = new ArrayList<Competitor>();   //racers who have finished   
	//private static Time time;  //used for recording the system time at any given moment in time
	protected static EventInterface typeEvent  = new IndEvent();
	public static FileOutputStream eventlog ;   
	public static String logStr [] = new String [50] ;  //  50 strings (event logs) for 50 runs
	private static int run = 1 ;  // default run number
	private static int [] numbers ;    // this field holds the num of the competitors that have been 
	
	
	public static boolean getPower(){
		return power;
	}
	public static void powerOn()
	{
		power = true ;
		
	}

	public static void powerOff()
	{
		power = false ;
	}
	
	public static void start() {
		if(!power) throw new IllegalStateException("Timer is OFF");
		if(toStart.isEmpty()) throw new IllegalStateException("NO Competitor in queue");
        
		numbers = typeEvent.st();
		log (numbers, "START" , Time.getCurrentTime() ) ;
	}
	public static void finish() {
		// TODO Auto-generated method stub
		if(!power) throw new IllegalStateException("Timer is OFF");
		if(toFinish.peek() == null) throw new IllegalStateException("No competitor");

		numbers = typeEvent.fn();
		log (numbers, "FINISH" , Time.getCurrentTime() ) ;

	}
	public static void exit()
	{
		if(power == true)
			System.exit(0);   //might need to be redone, not sure what exit is supposed to do
	}

	public static void reset()
	{
		//default values
		if(power == true)
		{
			toStart.clear();
			toFinish.clear();
			completedRacers.clear();
			disarmAll();
		}
	}
/**
 * Connects a sensor to a channel
 * @param type the type of sensor to be connected
 * @param index the channel 
 */
	public static void connectChannel(String type, int index)   // we don't need this, all 8 channels are connected by default
	{
		if(index>channels.length||index<1) throw new IllegalArgumentException(channels.length + " Channels");
		channels[index-1].connectSensor(new Sensor(type));
	}
	
	public static boolean isArmed(int i){
		return channels[i-1].isArmed();
	}
	
	public static void armChannel(int index)
	{
		if(index>channels.length) throw new IllegalArgumentException (channels.length + " Channels");
		if(channels[index-1] == null) throw new IllegalArgumentException ("Channel not initialized" );
		channels[index-1].arm();

	}

	public static void disarmChannel(int index)
	{
		if(index>channels.length) throw new IllegalArgumentException (channels.length + " Channels");
		if(channels[index-1] == null) throw new IllegalArgumentException ("Channel not initialized");
		channels[index-1].disArm();
	}

	/**Disarms all the channels
	 * Precondition: The system must be on 
	 * 
	 */
	public static void disarmAll()
	{
		if(!power) throw new IllegalStateException("Timer is OFF") ;

		for (Channel ch : channels){
			if(ch == null);
			else
				ch.disArm();
		}

	}
	
	public static void TriggerChannel (int index){
		channels[index-1].channelTrigger();
	}
	/**
	 * Switch the state of a channel from armed/disarmed to disarmed/armed
	 * @param index represents the channel
	 */
	public static void toggleChannel (int index){
		channels[index - 1].toggle();
	}


	/**
	 * adds a competitor as the next to start 
	 * Precondition: System must be on
	 * @param c the competitor
	 */
	public static void addCompetitor(Competitor c)
	{
		if(!power) throw new IllegalStateException("Timer is OFF");
		if(toStart != null && toStart.contains(c)) throw new IllegalStateException("Competitor already in queue");
		toStart.add(c);    	   	
	}
	
	/**Takes the next competitor in line and set its start time
	 * @precondition System is on and the queue of competitors isnt empty
	 * @postcondition the first competitor in line has been added to the line for finish
	 * 
	 */
	
/**Marks the next running competitor as did not finish
 * 
 */
	public static void dnf()
	{
		if(!power) throw new IllegalStateException ("Timer is OFF") ;
		if(toFinish.peek() == null) throw new IllegalStateException ("No competitor running") ;

		//completedRacers.add(toFinish.remove().setFinishTime(-1));
		Competitor c = toFinish.remove();
		c.setFinishTime(Double.NaN); //maybe add "did not finish" variable in competitor? TODO TODO TODO
		completedRacers.add(c);
		int[] b = {c.getNumber()};
		log (b, "CANCEL", Time.getCurrentTime()) ;
	}

	
	public static void cancel()
	{
		if(!power) throw new IllegalStateException("Timer is OFF") ;
		if(toFinish.peek() == null) throw new IllegalStateException("No competitor running") ;

		//toStart.set(0,toFinish.remove().setStartTime(0));
		Competitor c = toFinish.remove();   
		c.setStartTime(0.0);  //clear the start time
		toStart.set(0, c);   //adds the canceled racer back to the head of toStart so they can redo the start.
		int[] b = {c.getNumber()};
		log (b, "CANCEL", Time.getCurrentTime()) ;
	}

	public Competitor getCompetitor(int bibNumber)
	{
		for(Competitor c : toStart)
		{
			if(bibNumber == c.getNumber())
				return c;
		}

		for(Competitor c : toFinish)
		{
			if(bibNumber == c.getNumber())
				return c;
		}

		for(Competitor c : completedRacers)
		{
			if(bibNumber == c.getNumber())
				return c;
		}

		return null;
	}
	/**Opens a new run
	 * verifies first if string log at next run is null meaning that the current run has not ended
	 * because when it has, string log at next run is always open 
	 * @precondition 
	 * 		current run has been ended;next string log is not null
	 * 
	 */
	public static void newRun () {
		if (logStr[run ] == null ) throw new IllegalStateException("End a run first");  // run has not ended because otherwise the next string log would be open
			
			++run ;
	}
	
	/** Ends the current run
	 * verifies if string log at next run is null meaning that is the current run
	 * hasn't been ended if not null (open) then it is already been ended but a new run has 
	 * not been started
	 * @precondition
	 * 		a new run has been started but not yet ended
	 */
	public static void endRun(){
		if (logStr[run ] != null) throw new IllegalStateException("Open a new run first");               // run ended already because otherwise next run would be null
			
			logStr[run] = "";                                    // when we end a run we open a new string log for the next run
	}
	
	
	public static void print()
	{
		if(!power) throw new IllegalStateException ("Timer is OFF") ;
		System.out.println("Run \t BIB \t TIME");
		for (Competitor c : completedRacers) {
			//System.out.println(c.getNumber() + ": " + c.getStartTime());
			if(!c.isDNF())
				System.out.println("1 \t" + c.getNumber() + "\t" + String.format("%.2f", c.calculateTotalTime()));
			else
				System.out.println("1 \t" + c.getNumber() + "\t" + "DNF");
			
			int[] a = {c.getNumber()};
			log(a, "ELAPSED", c.calculateTotalTime()) ;			
		}

	}
	
	public static void export (){
		try {
			eventlog = new FileOutputStream ("RUN " + run + ".xml");
		DataOutputStream out = new DataOutputStream(eventlog);
		for (String s : logStr)
			out.writeBytes(s);
		}catch (IOException e){
			log (Time.getCurrentTime(), "Input Output Exception when exporting") ;
		}
		
	}
	
	
	public static void log (double time, String event){
		logStr [run-1] += Time.toString(time) + "\t" + event +  System.getProperty("line.separator")  ;
	}
	public static void log (int [] number, String event, double time){
		for (int n : number)
		logStr [run-1] += n + "\t" + event + " \t"+ (time == 0 ? "DNF" : Time.toString(time) ) +  System.getProperty("line.separator")  ;
	}
	
	

}