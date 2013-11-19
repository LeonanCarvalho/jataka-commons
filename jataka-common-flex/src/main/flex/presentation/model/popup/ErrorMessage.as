package presentation.model.popup {
	
	public class ErrorMessage{
		private var message:String;
		private var title:String;
		
		public function ErrorMessage(title:String, message:String)
		{
			this.message = message;
			this.title = title;
		}
		
		public function getMessage():String{
			return this.message;	
		} 
		
		public function getTitle():String{
			return this.title;
		}
	}
}