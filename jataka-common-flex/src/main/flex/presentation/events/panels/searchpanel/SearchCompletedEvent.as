package presentation.events.panels.searchpanel
{
	import flash.events.Event;
	
	import mx.collections.ArrayList;
	
	import presentation.events.Subscriber;

	public class SearchCompletedEvent extends Event implements Subscriber {		
		
		private static const SEARCH_COMPLETED_EVENT:String = "SEARCH_COMPLETED_EVENT";
		private var result:ArrayList = null;
		private var _hasNext:Boolean;
		
		// The subscriber object can be used to identify the original initiater,
		// in case several subscribers are registered to this event.
		private var subscriber:Object;
		
		public function SearchCompletedEvent(type:String, result:ArrayList, hasNext:Boolean, subscriber:Object=null) {
			super(type == null ? SEARCH_COMPLETED_EVENT : type, true, false);
			this.result = result;
			this._hasNext = hasNext;
			this.subscriber = subscriber;
		}
		
		public function getResult():ArrayList {
			return result;
		}
		
		public function getSubscriber():Object {
			return subscriber;
		}
		
		public function get hasNext():Boolean { 
			return _hasNext;
		}
	}
	
}