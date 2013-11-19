package application.controller {
	import application.model.grid.GridKeywordParameters;
	import application.model.grid.GridParameters;
	
	import flash.xml.XMLNode;
	
	public class GridController extends CrudController {
		public function GridController(contextRoot:String, debugIP:String){
			super(contextRoot, debugIP);
		}
		
		public function all(gridParameters:GridParameters, toModel:Function = null):void{
			var parameters:Object = new Object();
			parameters[GridParameters.XML_PARAMETER] = getFlexXBEngineInstance().serialize(gridParameters).toXMLString();
			
			getXMLContent(getServerURL() + getAllURL(), toModel!=null ? toModel: toGridModel, parameters);
		}
			
		public function search(keyword:String, addGridParameters:Function = null): void{
			this.gridParameters.keyword = keyword;
			
			// Callback hook for adding additional search parameters to GridParameters. 
			if (addGridParameters != null){
				addGridParameters(this.gridParameters);
			}
			
			var parameters:Object = new Object();
			parameters[GridParameters.XML_PARAMETER] = getFlexXBEngineInstance().serialize(this.gridParameters).toXMLString();
			
			getXMLContent(getServerURL() + getSearchURL(), toGridModel, parameters);
		}
		
		public function searchByParameters(callBack:Function, gridParameters:GridParameters): void{
			var parameters:Object = new Object();
			parameters[GridParameters.XML_PARAMETER] = getFlexXBEngineInstance().serialize(gridParameters).toXMLString();
			
			getXMLContent(getServerURL() + getSearchURL(), callBack, parameters);
		}
		
		
		public function toGridModel(xml:XMLNode):void{
			throw new Error("GridController - toGridModel must be override by derived class.");
		}
		
		public function get gridParameters():GridKeywordParameters {
			throw new Error("GridController - gridParameters must be override by derived class.");
		}
		
		private function getSearchURL():String {
			return BaseAPI.PROTECTED_SERVICE + "/" + getModelName() + BaseAPI.GRID_SEARCH;
		}
		
		private function getAllURL():String {
			return BaseAPI.PROTECTED_SERVICE + "/" + getModelName() + BaseAPI.GRID_ALL;
		}
	}
}