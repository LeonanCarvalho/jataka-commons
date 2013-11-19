package presentation.model.grid
{
	import application.model.grid.GridParameters;
	
	import flash.events.Event;
	
	public class PagingEvent extends Event {
		private static const GRID_PAGING_CHANGED:String = "gridPagingChanged";
		
		private var offset:int;
		private var reverseSorting:Boolean;
		
		public function PagingEvent(offset:int, reverseSorting:Boolean=false)
		{
			super(GRID_PAGING_CHANGED, true, false);
			this.offset = offset;
			this.reverseSorting = reverseSorting;
		}
		
		public function getOffset():int{
			return offset;
		}
		
		public function getReveseSorting():Boolean{
			return reverseSorting;
		}
		
		public static function getResetPagingEvent():PagingEvent{
			return new PagingEvent(GridParameters.DEFAULT_PAGE_START);
		}
	}
}