package presentation.model.panels {
	
	public interface ITopPanel extends IPanel {
		function get logoutText():String;
		function get logoutQuestion():String;
		function get logoutButtonTitle():String;
	}
}