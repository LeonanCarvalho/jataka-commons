package application.model.messages {
	
	[XmlClass(alias="RETURN_MESSAGE")]
	public class ReturnStatus {
		public static const STATUS_INFO:String="INFO";
		public static const STATUS_ERROR:String="ERROR";		
		public static const STATUS_WARNING:String="WARNING";
		public static const STATUS_CRITICAL:String="CRITICAL";
		
		[XmlAttribute(alias="type")]
		public var type:String;
		
		[XmlAttribute(alias="relatedId")]
		public var relatedId:String;
		
		[XmlElement(alias="TITLE")]
		public var title:String;
		
		[XmlElement(alias="MESSAGE")]
		public var message:String;
		
		public function ReturnStatus(){
		}
		
		public function isError():Boolean { 
			if (type==STATUS_ERROR || type==STATUS_CRITICAL){
				return true;
			}
			
			return false;
		}
	}
}