package presentation.events.panels.toppanel
{
	import flash.events.Event;

	public class LogoutEvent extends Event
	{		
		/** Event type for logout */
		public static const LOGOUT:String = "logout";
		
		public function LogoutEvent(){
			super(LOGOUT);
		}
	}
}