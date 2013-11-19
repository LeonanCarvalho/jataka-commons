package utils.list {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import presentation.view.lists.IconListItem;
	
	import spark.components.List;

	public class ListUtils {
		
		public function ListUtils() {
			
		}
		
		public static function toIconListItem(original:ArrayList, defaultImage:Object = null, nameAttr:String = "name", textAttr:String = "text", descAttr:String = "desc", imageAttr:String = "image"):ArrayCollection {
			var collection:ArrayCollection = new ArrayCollection();
			var item:IconListItem = null;
			
			for (var i:int = 0; i < original.length; i++) {
				item = new IconListItem();
				item.name = original.getItemAt(i)[nameAttr];
				item.text = original.getItemAt(i)[textAttr];
				item.desc = original.getItemAt(i)[descAttr];
				item.original = original.getItemAt(i);
					
				if (defaultImage == null){
					item.image = original[i][imageAttr];	
				}else {
					item.image = defaultImage;	
				}
				
				
				collection.addItem(item);
			}
			
			return collection;
		}
	}
}