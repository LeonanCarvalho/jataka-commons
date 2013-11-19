package presentation.view.grid {
	
	public interface IPaging {
		function getMessageLocale(bondle:String, key:String, parametes:Array = null):String;
		function getDirection():String;
	}
}