package utils.grid
{
	import application.model.messages.ReturnStatus;
	
	import mx.core.IFactory;
	
	import spark.components.gridClasses.GridColumn;

	public class GridUtils {
		public function GridUtils() {}
		
		public static function getSortableReferenceColumn(name:String, headerText:String, itemRenderer:IFactory, sortable:Boolean, showDataTips:Boolean, width:int=0):GridColumn{
			var column:GridColumn = getColumn(name, headerText, showDataTips, width);
			
			column.itemRenderer = itemRenderer; 
			column.sortable = sortable;
			
			return column;
		}
		
		public static function getSortableImageColumn(name:String, headerText:String, itemRendererFunction:Function, sortable:Boolean, showDataTips:Boolean, width:int=0):GridColumn{
			var column:GridColumn = getColumn(name, headerText, showDataTips, width);
			 
			column.itemRendererFunction = itemRendererFunction; 
			column.sortable = sortable;

			return column;
		}
		
		public static function getSortableGraugeColumn(name:String, headerText:String, itemRendererFunction:Function, sortable:Boolean, showDataTips:Boolean, width:int=0):GridColumn{
			var column:GridColumn = getColumn(name, headerText, showDataTips, width);
			
			column.itemRendererFunction = itemRendererFunction; 
			column.sortable = sortable;
			
			return column;
		}
		
		public static function getSortableColumn(name:String, headerText:String, sortable:Boolean, showDataTips:Boolean, width:int=0):GridColumn{
			var column:GridColumn = getColumn(name, headerText, showDataTips, width);
			
			column.sortable = sortable;
			
			return column;
		}
		
		public static function getColumn(name:String, headerText:String, showDataTips:Boolean, width:int=0):GridColumn{
			var column:GridColumn = new GridColumn(name);
			
			column.headerText = headerText;
			column.dataField = name;
			column.showDataTips = showDataTips;
				
			if (width != 0)
				column.width = width;
				
			return column;
		}
	}
}