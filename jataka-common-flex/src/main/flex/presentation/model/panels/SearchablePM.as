package presentation.model.panels
{
	import application.controller.GridController;
	import application.model.DomainObject;
	import application.model.grid.GridParameters;
	
	import flash.events.MouseEvent;
	import flash.xml.XMLNode;
	
	import mx.collections.ArrayList;
	import mx.collections.ListCollectionView;
	import mx.core.LayoutDirection;
	import mx.core.UIComponent;
	
	import org.spicefactory.parsley.core.context.Context;
	
	import presentation.events.Subscriber;
	import presentation.events.panels.searchpanel.LinkBarItemClickEvent;
	import presentation.events.panels.searchpanel.SearchCompletedEvent;
	import presentation.events.panels.searchpanel.SearchRefreshEvent;
	import presentation.model.BasePM;
	import presentation.model.grid.PagingEvent;
	import presentation.model.popup.ErrorMessage;
	import presentation.model.popup.QuestionMessage;
	import presentation.view.grid.IPaging;
	import presentation.view.grid.Paging;
	import presentation.view.messages.Messages;
	import presentation.view.panels.CrudPanel;
	import presentation.view.poup.PopUpManagerWrapper;
	
	import spark.components.DataGrid;
	import spark.components.Grid;
	import spark.events.GridSelectionEvent;

	public class SearchablePM extends BasePM implements ISearchable, IPaging {
		protected static const ID_DEFAULT_WIDTH:int = 40;
		
		[Bindable]
		public var columns:ArrayList = null; // Grid columns
		[Bindable]
		public var data:ListCollectionView = null; // Grid data provider		 
		
		[Bindable]
		public var linkBarButtons:Array = null; // Title header link bar buttons
		[Inject]
		public var context:Context; // This is required for parsley autowired to succed.
		
		[Bindable]
		public var freeText:String;

		private var selectedIndex:int = 0;
		private var selectedItem:DomainObject = null;
		protected var controller:GridController;
		
		// Paging Parameters
		private var _paging:Paging=null;
		private var pagingEvent:PagingEvent=null;
			
		[Embed("/images/search/delete-16.png")]
		private const DELETE_ICON:Class;
		[Embed("/images/search/edit-16.png")]
		private const EDIT_ICON:Class;
		
		public function SearchablePM() {
			linkBarButtons = new Array();
			
			if(isNewButtonVisible()){
				var newButton:Object = getNewButton();
				if (newButton != null)
					linkBarButtons[linkBarButtons.length] = newButton; 
			}
			
			if(isEditButtonVisible()){
				var editButton:Object = getEditButton();
				if (editButton != null)
					linkBarButtons[linkBarButtons.length] = editButton; 
			}
			
			if(isDeleteButtonVisible()){
				var deleteButton:Object = getDeleteButton();
				if (deleteButton != null)
					linkBarButtons[linkBarButtons.length] = deleteButton;  
			}
		}
		
		public function search(freeText:String, pagingEvent:PagingEvent=null):void {
			if (pagingEvent==null){
				// If pagging event is missing (Ususally a search event), use last offset.
				this.pagingEvent = new PagingEvent(paging.getOffset());		
			}else{
				this.pagingEvent = pagingEvent;
			}
			
			clearSelected();
			
			controller.search(freeText, addGridParameters);
		}
		
		protected function addGridParameters(gridParameters:GridParameters):void {
			if (pagingEvent!=null){
				gridParameters.setStart(pagingEvent.getOffset());
				gridParameters.setLimit(getGridNumberOfRows());
			}
			
			// Ovveride this method in order to send additional search parameters.
		}
		
		protected function clearSelected():void {
			this.selectedIndex = 0;
			this.selectedItem = null;
		}
		
		protected function getSelectedItem():DomainObject {
			return selectedItem;
		}
		
		public function postConstructor():void {
			throw new Error("SearchablePM - postConstruct must be implemented !!!");
		}
		
		public function searchCompleted(event:SearchCompletedEvent):void {
			if (paging != null)
				paging.init(pagingEvent!=null? pagingEvent.getOffset(): GridParameters.DEFAULT_PAGE_START,  getGridNumberOfRows(), event.hasNext);
		}
		
		public function isNewButtonVisible():Boolean{
			return true;
		}
		
		public function isEditButtonVisible():Boolean{
			return true;
		}
		
		public function isDeleteButtonVisible():Boolean{
			return true;
		}
		
		public function getNewButton():Object {
			throw new Error("SearchablePM - getNewButton must be implemented !!!");
		}
		
		public function getNewDomainObject():DomainObject {
			throw new Error("SearchablePM - getNewDomainObject must be implemented !!!");
		}
		
		public function onSearchRefreshEvent(event:SearchRefreshEvent):void {
			throw new Error("SearchablePM - onSearchRefreshEvent must be implemented !!!");
		}
		
		public function getModelName():String {
			throw new Error("SearchablePM - getModelName must be override by derived class.");
		}
		
		public function getEditButton():Object {
			var editButton:Object = new Object();
			editButton.icon = EDIT_ICON;
			
			return editButton;
		}
		
		public function getDeleteButton():Object {
			var deleteButton:Object = new Object();
			deleteButton.icon = DELETE_ICON;
			
			return deleteButton;
		}
		
		public function newCrud(crudPanel:CrudPanel):void {
			crudPanel.setDomainObject(getNewDomainObject() as DomainObject);
			
			openCrudPopup(crudPanel);			
		}
		
		public function editCrud(crudPanel:CrudPanel):void {
			if (!isGridItemSelected()){
				return;
			}
			
			crudPanel.setDomainObject(getSelectedItem());
			
			openCrudPopup(crudPanel);
		}
		
		public function deleteCrud():void {
			if (!isGridItemSelected()){
				return;
			}

			Messages.showQuestion(
				new QuestionMessage(getMessageLocale("searchpanel", "searchpanel.confirm.delete.title"),
					getMessageLocale("searchpanel", "searchpanel.confirm.delete.desc", [getModelName()])), null, onDeleteClick);		
		}
		
		private function onDeleteClick():void {
			controller.onDeleteClick(getSelectedItem().id, onDeleteReturnMessage);
		}
		
		private function onDeleteReturnMessage(xml:XMLNode):void {
			if (displayReturnMessage(xml)){
				search(freeText);
			}
		}
		
		private function openCrudPopup(crudPanel:CrudPanel):void {
			// This is required for parsley autowired to succed.
			context.viewManager.addViewRoot(crudPanel);
			PopUpManagerWrapper.getInstance().addPopUp(crudPanel, false);
			
			crudPanel.focusEnabled=true;
			crudPanel.setFocus();
		}
		
		protected function openPopup(uiComponent:UIComponent):void {
			// This is required for parsley autowired to succed.
			context.viewManager.addViewRoot(uiComponent);
			PopUpManagerWrapper.getInstance().addPopUp(uiComponent, true);
		}

		protected function isGridItemSelected():Boolean {
			if (selectedItem == null){
				Messages.showError(
					new ErrorMessage(getMessageLocale("searchpanel", "searchpanel.missing.seleted.item.title"),
					getMessageLocale("searchpanel", "searchpanel.missing.seleted.item.desc", [getModelName()])), null);
				return false;
			}
			
			return true;
		}
		
		public function selectionChangeHandler(event:GridSelectionEvent):void { 
			const eventGrid:Grid = event.currentTarget.grid;
			
			selectedIndex = eventGrid.selectedIndex; 
			selectedItem = eventGrid.selectedItem as DomainObject; 
		}
	
		public function doubleClick(event:MouseEvent, innerDataGrid:DataGrid):void {
			if (getEditButton()!=null)
				dispatcher(new LinkBarItemClickEvent(getEditButton().eventType));
		}
		
		[Bindable]
		public function get paging():Paging{
			return _paging;
		}
		
		public function set paging(paging:Paging):void{
			this._paging = paging;	
		}
		
		/*
		 * TODO - Change this method to dynamicly get application direction from system configuration (DB/File). 
		 */
		public function getDirection():String {
			return LayoutDirection.LTR;		
		}
		
		/*
		* TODO - Change this method to dynamicly get number of grid rows from system configuration (DB/File). 
		*/
		public function getGridNumberOfRows():int {
			return GridParameters.DEFAULT_PAGE_SIZE;		
		}
		
		// Handle events which subscribed object was the source initiater.
		protected function isSubscribed(subscriber:Subscriber, subscribed:Object):Boolean {
			if (subscriber != null && subscriber.getSubscriber() != null && subscriber.getSubscriber() == subscribed){
				return true;
			}
			
			return false; 
		}
		
		
	}
}
