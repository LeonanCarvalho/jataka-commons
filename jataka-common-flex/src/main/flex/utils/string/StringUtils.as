package utils.string {
	
	import flash.utils.ByteArray;
	import mx.utils.StringUtil;

	public class StringUtils extends StringUtil {
		public static const EMPTY:String = ""; 
		
		public function StringUtils(){
		}

		public static function getTextBytesLength(text:String):int {
			if(StringUtils.isEmpty(text))
				return 0;
			var byteArray:ByteArray = new ByteArray();			
			
			byteArray.writeUTFBytes(text);
			return byteArray.length;
		}
		
		public static function isEmpty(str:String):Boolean {
			return str == null || str == EMPTY || trim(str) == EMPTY;
		}
	}
}