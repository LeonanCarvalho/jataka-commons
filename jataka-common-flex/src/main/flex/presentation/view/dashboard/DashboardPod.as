package presentation.view.dashboard
{
	import com.esria.samples.dashboard.view.Pod;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import skins.dashboard.PodWindowSkin;
	
	import spark.components.Button;

	
	/**
	 * Extention class to Pod, this is necessary in order to add refresh button.
	 */
	public class DashboardPod extends Pod
	{
		
		[SkinPart(required="false")]
		public var refreshButton:Button;
		private var _podContent:IRefreshablePod;
		
		public function DashboardPod(podContent:IRefreshablePod)
		{
			super();
			
			this._podContent = podContent;
			
			setStyle("skinClass", PodWindowSkin);	
			addEventListener("creationComplete", onCreationComplete);
		}
		
		private function onCreationComplete(eventObj:Event):void
		{
			addListeners();
		}
		
		/**
		 * Add listeners for resize handles and window controls.
		 */
		private function addListeners():void
		{
			// maximize/restore button handler
			if (refreshButton != null)
			{
				refreshButton.addEventListener(MouseEvent.CLICK, onRefreshButton, false, 0, true);				
			}
		}
		
		/**
		 * Click handler for maximize/restore button
		 */
		protected function onRefreshButton(event:MouseEvent):void
		{
			_podContent.refreshData();
		}
		
	}
}