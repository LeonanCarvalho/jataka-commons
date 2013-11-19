package presentation.view.dashboard
{
	import com.esria.samples.dashboard.managers.PodLayoutManager;
	
	import flash.events.Event;
	
	import flexlib.mdi.events.MDIManagerEvent;
	import flexlib.mdi.events.MDIWindowEvent;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.resources.ResourceManager;
	
	import presentation.model.popup.QuestionMessage;
	import presentation.view.messages.Messages;
	
	import spark.components.SkinnableContainer;

	/**
	 *  Override PodLayoutManager in order to change default popup window according to jataka standards.
	 **/
	public class DashboardLayoutManager extends PodLayoutManager
	{
		// mdiwindow close event for processing after confirm close
		private var savedCloseEvent:MDIWindowEvent;
		
		public function DashboardLayoutManager(container:SkinnableContainer){
			super(container);
		}
		
		// prompt for confirm of window close
		override protected function onCloseWindow(event:Event):void
		{
			if (event is MDIWindowEvent)
			{
				// store a copy of the event in for use in handleAlertResponse
				savedCloseEvent = event.clone() as MDIWindowEvent;
				var title:String = ResourceManager.getInstance().getString("dashboard", "dashboard.close.title");
				var message:String = ResourceManager.getInstance().getString("dashboard", "dashboard.close.message");
				
				Messages.showQuestion(new QuestionMessage(title, message), null, handleClosetResponse);
			}
		}
		
		// handle results of confirm for close
		private function handleClosetResponse():void
		{
			var mgrEvent:MDIManagerEvent = new MDIManagerEvent(windowToManagerEventMap[savedCloseEvent.type], savedCloseEvent.window, this, null, null, savedCloseEvent.resizeHandle);
			
			mgrEvent.effect = this.effects.getWindowCloseEffect(mgrEvent.window, this);			
			dispatchEvent(mgrEvent);
		}
	}
}