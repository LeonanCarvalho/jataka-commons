package presentation.view.messages {
	
	import application.model.messages.ReturnStatus;
	
	import flash.display.DisplayObject;
	
	import mx.core.FlexGlobals;
	import mx.core.LayoutDirection;
	
	import presentation.model.IApplicationContextPM;
	import presentation.model.panels.IPanel;
	import presentation.model.popup.ErrorMessage;
	import presentation.model.popup.InfoMessage;
	import presentation.model.popup.QuestionMessage;
	import presentation.view.poup.PopUpManagerWrapper;

	public class Messages {
		// Main window Container
		public static function getContainer():DisplayObject{
			FlexGlobals.topLevelApplication.messagesCanvas.visible = true;
			return FlexGlobals.topLevelApplication.messagesCanvas;
		}
		
		public static function showErrorStatus(returnStatus:ReturnStatus, container:DisplayObject, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			showError(getErrorMessage(returnStatus), container, direction, alignment);
		}
		
		// send api=true if error was generated as a result of a server error
		public static function showError(errorMessage:ErrorMessage, container:DisplayObject, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			var messageWindow:MessageWindow = PopUpManagerWrapper.getInstance().add((container == null ? getContainer() : container), MessageWindow, true) as MessageWindow;
			messageWindow.message = errorMessage.getMessage();
			messageWindow.title = errorMessage.getTitle();
			messageWindow.type = MessageWindow.WINDOW_TYPE_ERROR;
			messageWindow.layoutDir=direction;
			messageWindow.textAlignment=alignment;
		}			
		
		// send api=true if error was generated as a result of a server warrning
		public static function showWarning(errorMessage:ErrorMessage, container:DisplayObject, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			var messageWindow:MessageWindow = PopUpManagerWrapper.getInstance().add((container == null ? getContainer() : container), MessageWindow, true) as MessageWindow;
			messageWindow.message = errorMessage.getMessage();
			messageWindow.title = errorMessage.getTitle();
			messageWindow.type = MessageWindow.WINDOW_TYPE_WARNING;
			messageWindow.layoutDir=direction;
			messageWindow.textAlignment=alignment;
		}
		
		public static function showInfoStatus(returnStatus:ReturnStatus, container:DisplayObject,returnFunction:Function=null, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			showInfo(getInfoMessage(returnStatus), container, returnFunction, direction, alignment);
		}
		
		// send api=true if error was generated as a result of a server warrning
		public static function showInfo(infoMessage:InfoMessage, container:DisplayObject, returnFunction:Function=null, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			var messageWindow:MessageWindow = PopUpManagerWrapper.getInstance().add((container == null ? getContainer() : container), MessageWindow, true) as MessageWindow;
			messageWindow.message = infoMessage.getMessage();
			messageWindow.title = infoMessage.getTitle();
			messageWindow.type = MessageWindow.WINDOW_TYPE_INFO;
			if(returnFunction!=null)
				messageWindow.returnFunction=returnFunction;
			messageWindow.layoutDir=direction;
			messageWindow.textAlignment=alignment;
		}
		
		public static function showQuestion(questionMessage:QuestionMessage, container:DisplayObject, returnFunction:Function, returnNoFunction:Function=null, direction:String = LayoutDirection.LTR, alignment:String = "left"):void {	
			var messageWindow:MessageWindow = PopUpManagerWrapper.getInstance().add((container == null ? getContainer() : container), MessageWindow, true) as MessageWindow;
			messageWindow.message = questionMessage.getMessage();
			messageWindow.title = questionMessage.getTitle();
			messageWindow.type = MessageWindow.WINDOW_TYPE_QUESTION;
			messageWindow.returnFunction=returnFunction;
			messageWindow.returnNoFunction=returnNoFunction;
			messageWindow.layoutDir=direction;
			messageWindow.textAlignment=alignment;
		}
		
		private static function getErrorMessage(returnStatus:ReturnStatus):ErrorMessage {
			return new ErrorMessage(returnStatus.title, returnStatus.message);
		}
		
		private static function getInfoMessage(returnStatus:ReturnStatus):InfoMessage {
			return new InfoMessage(returnStatus.title, returnStatus.message);
		}
	}
}