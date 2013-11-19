package presentation.view.buttons
{
	import flash.display.InteractiveObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	public class TopButtonClickEvent extends Event
	{
		private var buttonName:String;
		private var mouseEvent:MouseEvent;
		
		public static const EVENT_TYPE:String = "TOP_BUTTOM_CLICK";
		
		public function TopButtonClickEvent(type:String, mouseEvent:MouseEvent){
			super(type, true, false);

			this.buttonName = mouseEvent.currentTarget.id;
			this.mouseEvent = mouseEvent;
		}
		
		public function getButtonName():String {
			return this.buttonName;	
		}
		
		public function getMouseEvent():MouseEvent{
			return mouseEvent; 
		}
	}
}