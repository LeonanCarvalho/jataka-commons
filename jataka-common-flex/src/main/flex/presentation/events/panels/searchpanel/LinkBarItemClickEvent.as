package presentation.events.panels.searchpanel
{
	import flash.events.Event;
	import presentation.events.Subscriber;

	public class LinkBarItemClickEvent extends Event implements Subscriber {		
		
		// The subscriber object can be used to identify the original initiater,
		// in case several subscribers are registered to this event.
		private var subscriber:Object;
		
		public function LinkBarItemClickEvent(eventType:String, subscriber:Object=null){
			super(eventType, true, false);
			
			this.subscriber = subscriber;
		}
		
		
		public function getSubscriber():Object {
			return subscriber;
		}
		
	}
}