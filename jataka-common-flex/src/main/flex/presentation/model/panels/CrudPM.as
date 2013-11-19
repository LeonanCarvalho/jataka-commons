package presentation.model.panels {
	import application.controller.CrudController;
	import application.model.DomainObject;
	import application.model.messages.ReturnStatus;
	
	import flash.events.MouseEvent;
	import flash.xml.XMLNode;
	
	import mx.core.IFlexDisplayObject;
	import mx.core.UIComponent;
	
	import presentation.events.panels.searchpanel.SearchRefreshEvent;
	import presentation.model.BasePM;
	import presentation.view.crud.ICrudPanel;

	public class CrudPM extends BasePM implements ICrud {
		
		[Bindable]
		private var domainObject:DomainObject; // Attached to the form data
		[Bindable]
		protected var controller:CrudController; // Perform all server requests
		
		private var state:CrudState = CrudState.NEWS; // Current state (NEW/UPDATE)
		public var validateFunction:Function; // Define additional form validation
		public var beforeSaveFunction:Function; // Execute additional code before save
		
		[Bindable]
		public var largeIcon:Class;
		[Bindable]
		public var title:String;
		
		public function CrudPM() {
			super();
		}

		public function getDomainObject():DomainObject{
			return domainObject;
		}
		
		// This method must be override and call super.setDomainObject
		// set page state (update/new) according to incomming object.
		public function setDomainObject(domainObject:DomainObject):void{
			this.domainObject = domainObject;
			
			this.refreshState();	
		}
		
		public function refreshState():void {
			if (domainObject == null || domainObject.id == 0 ){
				this.state = CrudState.NEWS;
			}else{
				this.state = CrudState.UPDATE;
			}
			
			this.refreshTitle();
		}
		
		public function isNewState():Boolean {
			return state==CrudState.NEWS;
		}
		
		public function isUpdateState():Boolean {
			return state==CrudState.UPDATE;
		}
		
		// Get Page title according to model state
		public function refreshTitle():void{
			if (isNewState()){
				this.title = getMessageLocale("crudpanel", "crudpanel.title.new") + " " + modelName;
			}else{
				this.title = getMessageLocale("crudpanel", "crudpanel.title.update") + " " + modelName;
			}
		}
		
		public function postConstructor():void {
			throw new Error("CrudPM - postConstruct method must be implemented !!!");
		}
		
		public function get modelName():String {
			throw new Error("CrudPM - momdelName method must be implemented !!!");	
		}
		
		public function getNewDomainObject():DomainObject{
			throw new Error("CrudPM - getNewDomainObject must be implemented !!!");
		}
		
		public function getRefreshEventType():String {
			throw new Error("CrudPM - getRefreshEventType must be implemented !!!");
		}
		
		public function validate():Boolean {
			if (validateFunction!=null){
				return validateFunction();
			}
			
			return true;
		}
		
		private function onSave(panel:ICrudPanel, returnFunction:Function):void {
			if (validate() == false)
				return;
			
			if(beforeSaveFunction != null){
				beforeSaveFunction();
			}
			
			if (isNewState()){
				controller.onCreate(domainObject, returnFunction, panel)
			}else{
				controller.onUpdate(domainObject, returnFunction, panel)
			}
		}
			
		public function onSaveClick(event:MouseEvent, panel:ICrudPanel):void {
			onSave(panel, onSaveReturnMessage);
		}
		
		public function onSaveCloseClick(event:MouseEvent, panel:ICrudPanel):void {
			onSave(panel, onSaveCloseReturnMessage);
		}
		
		public function onSaveNewClick(event:MouseEvent, panel:ICrudPanel):void {	
			onSave(panel, onSaveNewReturnMessage);
		}
		
		public function onCloseClick(panel:ICrudPanel) :void {
			closePopup(panel as IFlexDisplayObject);
			
			dispatcher(new SearchRefreshEvent(getRefreshEventType()));
		}
		
		public function getTopParent(child:UIComponent):UIComponent {
			if(child == null || child.parent == null){
				return child;
			}
			
			var returnParent:UIComponent = getTopParent(child.parent as UIComponent);
			if (returnParent==null)
				return child;
			
			return returnParent;
		}
		
		public function onSaveReturnMessage(xml:XMLNode, returnParams:ICrudPanel):void {
			if (displayReturnMessage(xml)){
				dispatcher(new SearchRefreshEvent(getRefreshEventType()));
				
				var returnStatus:ReturnStatus = toReturnStatus(xml);
				
				// Update new saved object ID
				if (isNewState() && returnStatus.relatedId !=null)
					this.domainObject.id = new int(returnStatus.relatedId);
				
				refreshState();
				
				returnParams.onSaveSuccess();
			}
		}
		
		public function onSaveCloseReturnMessage(xml:XMLNode, returnParams:ICrudPanel):void {
			if (displayErrorMessage(xml)){
				onCloseClick(returnParams);		
			}
		}
		
		public function onSaveNewReturnMessage(xml:XMLNode, returnParams:ICrudPanel):void {
			if (displayErrorMessage(xml)){
				dispatcher(new SearchRefreshEvent(getRefreshEventType()));

				// Create new DominObject abd set it using binding.
				onSaveNewObject();
				
				refreshState();
				
				returnParams.onSaveNewSuccess();
			}
		}
		
		/**
		 * Ovveride this method to create a new Object via flex binding
		 **/
		public function onSaveNewObject():void {
			throw new Error("CrudPM - onSaveNewObject method must be implemented !!!");
		}
		
		public function getServerURL():String{
			return controller.getServerURL();
		}
	}
}