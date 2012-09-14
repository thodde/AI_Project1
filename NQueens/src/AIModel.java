/*
 *  A virtual parent class to manage the different AI models.  Will need to override performMove and testCanPerformMove
 *  in all child classes
 */

public class AIModel {
	public AIModel(){
		
	}
	
	public void performMove(){
		return;
	}
	
	public boolean testCanPerformMove(){
		return false;
	}
}
