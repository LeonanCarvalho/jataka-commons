package application.model.security {
	public interface IUser {
		function get administrator ():Boolean;
		function set administrator (administrator:Boolean):void;
		
		function get username ():String;
		function set username (username:String):void;
		
	}
}