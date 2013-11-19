package presentation.view.lists
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	public class IconListItemClickEvent extends Event {
		private var buttonName:String;
		private var mouseEvent:MouseEvent;
		
		public static const EVENT_TYPE:String = "ICON_LIST_ITEM_CLICK";
		
		public function IconListItemClickEvent(mouseEvent:MouseEvent, iconListItem:Object, eventType:String = EVENT_TYPE){
			super(eventType, true, false);
			
			if (iconListItem!=null){
				this.buttonName = iconListItem["name"];
			}else{
				this.buttonName = mouseEvent.currentTarget.name;		
			}
		
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