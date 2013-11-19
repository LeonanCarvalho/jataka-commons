package presentation.view.crud {
	import application.model.DomainObject;
	
	public interface ICrudPanel {
		function injectionComplete(): void;
		
		function setDomainObject(domainObject:DomainObject):void;
		
		function onSaveSuccess():void;
		
		function onSaveNewSuccess():void;
	}
}