package application.model.grid {
	[XmlClass(alias="GRID_PARAMETERS")]
	public  class GridParameters {
		public static const XML_PARAMETER:String = "GRID_PARAMETERS";
		public static const DEFAULT_PAGE_SIZE:int = 25;
		public static const DEFAULT_PAGE_START:int = 0;
		
		[XmlAttribute(alias="limit")]
		public var limit:int = DEFAULT_PAGE_SIZE;
		
		[XmlAttribute(alias="start")]
		public var start:int = 0;
		
		[XmlAttribute(alias="sorting")]
		public var sorting:String = "id";
		
		public function GridParameters(limit:int = DEFAULT_PAGE_SIZE, start:int = DEFAULT_PAGE_START, sorting:String = "id"){
			super();
			
			this.limit = limit;
			this.start = start;
			this.sorting = sorting;
		}
		
		public function getLimit():int{
			return limit;	
		}
		
		public function setLimit(limit:int):void{
			this.limit = limit;	
		}
		
		public function getStart():int{
			return start;	
		}
		
		public function setStart(start:int):void{
			this.start = start;	
		}
		
		public function getSorting():String{
			return sorting;
		}
		
		public function setSorting(sorting:String):void{
			this.sorting = sorting;
		}
	}
}