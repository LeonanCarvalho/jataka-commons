package application.model.grid {
	[XmlClass(alias="GRID_PARAMETERS")]
	public class GridKeywordParameters extends GridParameters {
		
		[XmlElement(alias="KEYWORD")]
		public var keyword:String;
		
		public function GridKeywordParameters(limit:int = GridParameters.DEFAULT_PAGE_SIZE, start:int = GridParameters.DEFAULT_PAGE_START, sorting:String = "id"){
			super(limit, start, sorting);
		}
		
		public function setKeyword(keyword:String):void{
			this.keyword=keyword;
		}
	}
}