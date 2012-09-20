/*
 *  A virtual parent class to manage the different AI models.  Will need to override performMove and testCanPerformMove
 *  in all child classes
 */

public abstract class AIModel {
	
	public abstract void performMove();
	public abstract boolean testCanPerformMove();
	public String printModelAdditionalInfo() {
		return "";
	}
}
