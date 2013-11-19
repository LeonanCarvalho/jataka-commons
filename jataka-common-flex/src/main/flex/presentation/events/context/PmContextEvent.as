package presentation.events.context
{
	import flash.events.Event;
	
	import presentation.model.context.PmContext;

	public class PmContextEvent extends Event {	
		/** Event type for PM Context Changes */
		private static const PM_CONTEXT_CHANGE:String = "PM_CONTEXT_CHANGE";
		
		private var context:PmContext;
		
		public function PmContextEvent(context:PmContext){
			super(PM_CONTEXT_CHANGE, true, false);
			
			this.context = context;
		}
		
		public function getContext():PmContext{
			return this.context;
		}
	}
}