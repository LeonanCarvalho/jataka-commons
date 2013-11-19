package presentation.model.panels {
	import mx.collections.ArrayList;
	import mx.collections.ListCollectionView;
	
	import presentation.events.panels.searchpanel.SearchCompletedEvent;
	import presentation.model.grid.PagingEvent;

	public interface ISearchable {
		function get columns():ArrayList;
		function set columns(colums:ArrayList):void;
	
		function get data():ListCollectionView;
		function set data(data:ListCollectionView):void;
		
		function get freeText():String;
		function set freeText(freeText:String):void;
		
		function search(freeText:String, pagingEvent:PagingEvent=null):void;
		function searchCompleted(event:SearchCompletedEvent):void;
	}
}