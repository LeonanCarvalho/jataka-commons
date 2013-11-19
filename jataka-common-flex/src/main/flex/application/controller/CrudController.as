package application.controller {
	import application.model.DomainObject;
	
	import mx.logging.ILogger;
	
	import utils.LogUtils;

	public class CrudController extends BaseController {
		private static const LOG:ILogger = LogUtils.getLogger(CrudController);
		protected static const CRUD_PARAMETERS:String = "CRUD_PARAMETERS";
		
		// Used to dispatch events to NON UI conponents
		[Bindable]
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function CrudController(contextRoot:String, debugIP:String) {
			super(contextRoot, debugIP);
		}
		
		public function onUpdate(domainObject:DomainObject, toCrudModel:Function, returnParams:Object=null):void {
			LOG.info("Posting crud save request to server !");
			var paremeters:Object = new Object();
			
			paremeters[CRUD_PARAMETERS] = getFlexXBEngineInstance().serialize(domainObject).toXMLString(); 

			getXMLContent(getServerURL() + getUpdateURL(), toCrudModel, paremeters, returnParams);
		}
		
		public function onCreate(domainObject:DomainObject, toCrudModel:Function, returnParams:Object=null):void {
			LOG.info("Posting crud save request to server !");
			var paremeters:Object = new Object();
			
			paremeters[CRUD_PARAMETERS] = getFlexXBEngineInstance().serialize(domainObject).toXMLString(); 
			
			getXMLContent(getServerURL() + getCreateURL(), toCrudModel, paremeters, returnParams);			
		}

		
		public function onDeleteClick(id:int, toCrudModel:Function):void {	
			LOG.info("Posting crud save request to server !");
			var paremeters:Object = new Object();
			
			paremeters[CRUD_PARAMETERS] = id; 
			
			getXMLContent(getServerURL() + getDeleteURL(), toCrudModel, paremeters);	
		}
		
		private function getUpdateURL():String {
			return BaseAPI.PROTECTED_SERVICE + "/" + getModelName() + BaseAPI.CRUD_UPDATE;
		}
		
		private function getCreateURL():String {
			return BaseAPI.PROTECTED_SERVICE + "/" + getModelName() + BaseAPI.CRUD_CREATE;
		}
		
		private function getDeleteURL():String {
			return BaseAPI.PROTECTED_SERVICE + "/" + getModelName() + BaseAPI.CRUD_DELETE;
		}
		
		protected function getModelName():String {
			throw new Error("CrudController - getModelName must be override by derived class.");
		}
	}
}