package presentation.model.dropdown{
	
	public class DropdownItem {
		public var data:String;
		public var label:String;
		
		public function DropdownItem(id:String,label:String){
			data=id;
			this.label=label;
		}
	}
}