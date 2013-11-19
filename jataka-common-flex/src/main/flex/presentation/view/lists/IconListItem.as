package presentation.view.lists {
	[Bindable]
	public class IconListItem {
		private var _name:String;
		private var _text:String;
		private var _desc:String;
		private var _image:Object;
		private var _original:Object;
		private var _lastItem:Boolean = false;

	
		public function IconListItem() {
		}	
	
		/**
		 * Name is used to identify click event.
		 * */
		public function get name():String {
			return _name;
		}
		
		public function set name(name:String):void {
			_name = name;
		}
		
		public function get text():String {
			return _text;
		}
		
		public function set text(text:String):void {
			_text = text;
		}
		
		public function get desc():String {
			return _desc;
		}
		
		public function set desc(desc:String):void {
			_desc = desc;
		}
		
		public function get image():Object {
			return _image;
		}
		
		public function set image(image:Object):void{
			_image = image;
		}
		
		public function get original():Object {
			return _original;
		}
		
		public function set original(original:Object):void{
			_original = original;
		}
		
		public function get lastItem():Boolean {
			return _lastItem;
		}
		
		public function set lastItem(lastItem:Boolean):void{
			_lastItem = lastItem;
		}
	}
}
