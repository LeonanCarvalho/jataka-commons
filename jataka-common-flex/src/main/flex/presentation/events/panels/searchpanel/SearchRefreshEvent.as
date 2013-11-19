package presentation.events.panels.searchpanel {
	import flash.events.Event;
	
	import presentation.model.grid.PagingEvent;

	public class SearchRefreshEvent extends Event {		
		private static const SEARCH_REFRESH_EVENT:String = "SEARCH_REFRESH_EVENT";
		
		// Optional paging event - can be used to reset/clear paging data.
		private var pagingRefreshEvent:PagingEvent = null;
		
		public function SearchRefreshEvent(type:String, pagingEvent:PagingEvent = null){
			super(type == null ? SEARCH_REFRESH_EVENT : type, true, false);
			this.pagingRefreshEvent = pagingEvent;
		}
		
		public function getPagingEvent():PagingEvent{
			return pagingRefreshEvent!=null? pagingRefreshEvent: null;
		}
	}
}