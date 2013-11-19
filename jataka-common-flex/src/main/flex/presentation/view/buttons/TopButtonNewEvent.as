package presentation.view.buttons
{
	import flash.display.InteractiveObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	public class TopButtonNewEvent extends Event
	{
		private var topButton:TopButton;
		
		public static const EVENT_TYPE:String = "TOP_BUTTON_NEW";
		
		public function TopButtonNewEvent(topButton:TopButton){
			super(EVENT_TYPE, true, false);

			this.topButton = topButton;
		}

		
		public function getButton():TopButton {
			return topButton;
		}
	}
}