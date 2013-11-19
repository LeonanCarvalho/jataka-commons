package utils{
	import application.model.DomainObject;
	
	import mx.collections.ArrayList;
	
	import spark.components.ComboBox;

	public class ComboBoxUtils {
		public static const NON_SELECTED_INDEX:int = -1;
		public static const COMBOBOX_LIMIT:int = 200;
		
		public function ComboBoxUtils(){}
		
		public static function validateSelection(comboBox:ComboBox, selectedType:Class):Boolean{
			if (comboBox.selectedItem!=null && comboBox.selectedItem is selectedType){
				return true;
			}
			
			comboBox.selectedIndex = NON_SELECTED_INDEX;
			
			return false;
		}
		
		/**
		 * Acept only DomainObject ArrayList.
		 * */
		public static function getSelected(list:ArrayList, selectedItem:DomainObject):int{
			if (selectedItem == null || list==null)
				return NON_SELECTED_INDEX;
			
			for (var i:int=0; i<list.length; i++) {
				// Get this item's data 
				if ((list.getItemAt(i) as DomainObject).equals(selectedItem)){
					return i;
				}
			}
			
			return NON_SELECTED_INDEX;
		}
		
		public static function getSelectedIndex(list:ArrayList, selectedItemIndex:int):int{
			if (selectedItemIndex == NON_SELECTED_INDEX || list==null)
				return NON_SELECTED_INDEX;
			
			for (var i:int=0; i<list.length; i++) {
				// Get this item's data 
				if ((list.getItemAt(i) as DomainObject).id == selectedItemIndex){
					return i;
				}
			}
			
			return NON_SELECTED_INDEX;
		}
	}
}