package presentation.model.panels {
	import application.model.DomainObject;
	
	import flash.events.MouseEvent;
	
	import presentation.view.crud.ICrudPanel;

	public interface ICrud {
		function get largeIcon():Class;
		function set largeIcon(largeIcon:Class):void;
		
		function get title():String;
		function get modelName():String;
	
		function getDomainObject():DomainObject;
		function setDomainObject(domainObject:DomainObject):void;
		
		function onSaveClick(event:MouseEvent, owner:ICrudPanel):void;
		function onSaveCloseClick(event:MouseEvent, owner:ICrudPanel ):void;
		function onSaveNewClick(event:MouseEvent, owner:ICrudPanel ):void;
		function onCloseClick(owner:ICrudPanel):void;
	}
}