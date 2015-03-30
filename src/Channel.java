public class Channel {
	private int type ;     // odd is start - even is finish
	private boolean isArmed = false ;
	private Sensor sens ;
	double trigger ;

	Channel (int t){
		type = t ; 
	}
	public boolean isArmed(){
		return isArmed;
	}
	public void toggle () {
		isArmed = isArmed  ?  false : true ;		
	}

	public void arm (){
		isArmed = true;
	}
	public void disArm(){
		isArmed = false ;
	}

	/**Links a sensor and this channel
	 * Precond: the argument is not null
	 * @param s the sensor
	 * @return true if the connection is established
	 */
	public boolean connectSensor (Sensor s){
		if (s == null) throw new IllegalArgumentException("Sensor null");
		sens = s ;
		return s.connectChannel(this);
	}

	/**
	 * Precond: Must have a sensor connected to it 
	 * Unlinks any sensor and this channel 
	 */
	public void disconnectSensor (){
		if (sens == null) throw new IllegalStateException ("No Sensor connected");

		sens.disConnectChannel();
		sens = null ; 		
	}
	public int getType(){
		return type;
	}

	/**
	 * calls start or finish depending on whether the type is even or odd
	 * @return time in seconds 
	 */
	public void channelTrigger(){
		if (!isArmed) throw new IllegalStateException ("Channel disarmed");
		if(type % 2 == 1)
			ChronoTimer.start();
		else
			ChronoTimer.finish();
	}
	

	


}