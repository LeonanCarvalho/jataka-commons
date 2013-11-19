package presentation.model.panels {
	public class CrudState {
		private var value:String;
		
		public static const NEWS:CrudState = new CrudState("NEW");
		public static const UPDATE:CrudState =  new CrudState("UPDATE");
		
		public function CrudState(state:String){
			this.value = state;
		}
	}
}