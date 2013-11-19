package presentation.model
{
	import application.model.messages.ReturnStatus;
	
	import com.googlecode.flexxb.FlexXBEngine;
	
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	import flash.xml.XMLNode;
	
	import mx.core.IFlexDisplayObject;
	import mx.core.LayoutDirection;
	import mx.formatters.DateFormatter;
	import mx.managers.SystemManagerGlobals;
	import mx.resources.ResourceManager;
	import mx.styles.IStyleManager2;
	import mx.styles.StyleManager;
	
	import presentation.events.context.PmContextEvent;
	import presentation.model.context.PmContext;
	import presentation.model.popup.ErrorMessage;
	import presentation.model.popup.InfoMessage;
	import presentation.model.popup.QuestionMessage;
	import presentation.view.messages.Messages;
	import presentation.view.poup.PopUpManagerWrapper;

	public class BasePM {
		protected static const SELF:String = "_self"; 
		
		// Used to dispatch events to NON UI conponents
		[Bindable]
		[MessageDispatcher]
		public var dispatcher:Function;
		private var pmContext:PmContext;
		
		public function BasePM(){
		}
		
		public function getStyleManager():IStyleManager2{
			return mx.styles.StyleManager.getStyleManager(SystemManagerGlobals.topLevelSystemManagers[0]);
		}
		
		[MessageHandler]
		public function contextChanged(event:PmContextEvent):void { 
			this.pmContext = event.getContext();
		}
		
		/************************************************
		 * GET A CURRENT DATE FORMATTED WITH DEFAULT
		 ************************************************/
		public function getFormattedDate(currentDate:String, dateFormat:String):String{
			var df:DateFormatter = new DateFormatter();				
			df.formatString = dateFormat;
			return df.format(currentDate);	
		}
		
		public function showError(errorMessage:ErrorMessage, container:DisplayObject, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			Messages.showError(errorMessage, container, direction, alignment);
		}			
		
		// send api=true if error was generated as a result of a server warrning
		public function showWarning(errorMessage:ErrorMessage, container:DisplayObject, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			Messages.showWarning(errorMessage, container, direction, alignment);
		}
		
		// send api=true if error was generated as a result of a server warrning
		public function showInfo(infoMessage:InfoMessage, container:DisplayObject, returnFunction:Function=null, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			Messages.showInfo(infoMessage, container, returnFunction, direction, alignment);
		}
		
		public function showQuestion(questionMessage:QuestionMessage, container:DisplayObject, returnFunction:Function, returnNoFunction:Function=null, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			Messages.showQuestion(questionMessage, container, returnFunction, returnNoFunction, direction, alignment);
		}
		
		public function getMessageLocale(resourceBundle:String, keyname:String, parametes:Array = null):String{
			return ResourceManager.getInstance().getString(resourceBundle, keyname, parametes);
		}

		protected function closePopup(popup:IFlexDisplayObject):void {
			PopUpManagerWrapper.getInstance().remove(popup);
		}
		
		protected static function getFlexXBEngineInstance():FlexXBEngine{
			FlexXBEngine.instance.configuration.escapeSpecialChars=true;
			return FlexXBEngine.instance;
		}

		protected static function toReturnStatus(xml:XMLNode):ReturnStatus {
			return getFlexXBEngineInstance().deserialize(new XML(xml), ReturnStatus) as ReturnStatus;
		}
		
		// Display any return messages from server
		// Return false on error
		protected static function displayReturnMessage(xml:XMLNode):Boolean {
			var returnStatus:ReturnStatus = toReturnStatus(xml);
			
			if (returnStatus.isError()){				
				Messages.showErrorStatus(returnStatus, Messages.getContainer()); 
				return false;
			}else{
				Messages.showInfoStatus(returnStatus, Messages.getContainer());
				return true;
			}
		}
		
		// Display only error messages from server
		protected static function displayErrorMessage(xml:XMLNode):Boolean {
			var returnStatus:ReturnStatus = toReturnStatus(xml);
			
			if (returnStatus.isError()){
				Messages.showErrorStatus(returnStatus, Messages.getContainer());
				return false;
			}
			
			return true;
		}

		public function isAdmin():Boolean {
			if (pmContext==null || pmContext.getUser()==null)
				return false;
			
			return pmContext.getUser().administrator;
		}
	}
}