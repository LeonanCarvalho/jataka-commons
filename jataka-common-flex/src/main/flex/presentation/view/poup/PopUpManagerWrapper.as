package presentation.view.poup
{
	import flash.display.DisplayObject;
	
	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.managers.PopUpManagerChildList;
	
	public class PopUpManagerWrapper
	{
		// Singelton instance
		public static var instance:PopUpManagerWrapper;
		
		public static function getInstance():PopUpManagerWrapper
		{
			if (instance==null){
				instance = new PopUpManagerWrapper();	
			}
			
			return instance;
		}
			
		public function addPopUp(window:IFlexDisplayObject, modal:Boolean):void {
			PopUpManager.addPopUp(window, FlexGlobals.topLevelApplication.messagesCanvas, modal, PopUpManagerChildList.POPUP) as IFlexDisplayObject;
					
			centerPopUp(window);
		}

		/**
		 * @param parent - the parent of that window (mainly used to center the popup in it)
		 * @param className - the class type of that window (e.g. Component.Settings.UserSet)
		 * @param modal - (boolean) whether to open as modal or not
		 */		
		public function add(parent:DisplayObject, className:Class, modal:Boolean):IFlexDisplayObject {
			return PopUpManager.createPopUp(parent, className, modal, PopUpManagerChildList.POPUP) as IFlexDisplayObject;	
		}
		
		public function remove(popup:IFlexDisplayObject):void {
			PopUpManager.removePopUp(popup);
		}
		
		public function centerPopUp(popUp:IFlexDisplayObject):void {
			PopUpManager.centerPopUp(popUp);
		}
	}
}