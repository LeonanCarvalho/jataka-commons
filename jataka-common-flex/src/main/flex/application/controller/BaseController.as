package application.controller{
	import application.controller.http.HttpServiceWrapper;
	
	import com.googlecode.flexxb.FlexXBEngine;
	
	import flash.external.ExternalInterface;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayList;
	import mx.logging.ILogger;
	import mx.managers.IBrowserManager;
	import mx.messaging.messages.ErrorMessage;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.http.HTTPService;
	import mx.utils.URLUtil;
	
	import presentation.model.dropdown.DropdownItem;
	import presentation.view.messages.Messages;
	
	import utils.LogUtils;

	public class BaseController {
		private static const PROTOCOL_STRING:String="http://";
		private static const DEFAULT_LOCAL_HOST:String="localhost";
		private static const LOCAL_DEBUG_SERVER:String = PROTOCOL_STRING + DEFAULT_LOCAL_HOST + ":8080/" + contextRoot;
		private static const LOG:ILogger = LogUtils.getLogger(BaseController);
		
		public static const MAX_RATE:Number = Number.MAX_VALUE;
		
		private static var contextRoot:String;
		private static var debugIP:String;
		
		private static var browserManager:IBrowserManager = null;
		
		public function BaseController(contextRoot:String, debugIP:String){
			BaseController.contextRoot = contextRoot;
			BaseController.debugIP = debugIP;
		}
		
		private function getContextRoot():String {
			return contextRoot;
		}
		
		private function getDebugIP():String {
			return debugIP;
		}
		
		/********************************************************************************************************
		 *  GETS THE SERVER URL FROM THE BROWSER (LOCAL HOST)
		 *******************************************************************************************************/
		public function getServerURL():String {
			var url:String = getBrowserURL();
			// Local debug enviorment URL
			if (url==null || url.substring(0, 7).indexOf("file://") != -1)
			{
				if(debugIP!= null && debugIP != DEFAULT_LOCAL_HOST){
					return PROTOCOL_STRING + debugIP + "/" + contextRoot;
				}
				else{
					LOG.debug("Using Server URL(Const) " + LOCAL_DEBUG_SERVER);
					return LOCAL_DEBUG_SERVER;
				}
			}
			url = getContextPath(URLUtil.getProtocol(url), url, URLUtil.getServerNameWithPort(url));
			LOG.debug("Using Server URL(Browser) " + url);
			return url;
		}
		
		private static function getBrowserURL():String{
			return ExternalInterface.call("window.location.href.toString");
		}
		
		/********************************************************************************************************
		 *  
		 *******************************************************************************************************/
		private static function getContextPath(protocol:String, url:String, serverNameAndPort:String):String {
			// Find first slash after server name;
			var start:int = url.indexOf(serverNameAndPort) + serverNameAndPort.length + 1;
			var length:int = url.indexOf("/", start);
			var context:String = length == -1 ? url.substring(start) : url.substring(start, length);
			return protocol + "://"  + serverNameAndPort + "/" + context;
		}
		
		public function deserlize(xml:XML,modelClass:Class):*{
			return getFlexXBEngineInstance().deserialize(xml, modelClass) as modelClass;
		}
		
		/********************************************************************************************************
		 *  
		 *******************************************************************************************************/
		public function getFlexXBEngineInstance():FlexXBEngine{
			FlexXBEngine.instance.configuration.escapeSpecialChars=true;
			return FlexXBEngine.instance;
		}
		
		/********************************************************************************************************
		 *  OPENS A NEW WINDOW WITH URL
		 *******************************************************************************************************/
		public function openNewWindow(redirectUrl:String):void {
			var urlRequest:URLRequest = new URLRequest(redirectUrl);
			navigateToURL(urlRequest,"_blank");
		}
		
		/********************************************************************************************************
		 *  REDIRCET TO A NEW WINDOW
		 *******************************************************************************************************/
		public function redirectWindow(redirectUrl:String):void {
			var urlRequest:URLRequest = new URLRequest(redirectUrl);
			navigateToURL(urlRequest,"_self");
		}
		
		/********************************************************************************************************
		 *  INTSANTCE OF HTTP SERVICE
		 *******************************************************************************************************/
		protected function getHttpService():HttpServiceWrapper {						
			return HttpServiceWrapper.getInstance();	
		}
		
		public function getClassTypeFromObject(obj:Object):String{
			var className:String=Class(getDefinitionByName(getQualifiedClassName(obj))).toString();
			className=className.replace("[class ","");
			return className.replace("]",""); 
		}
		
		public function getIndexOfArrayByItem(curid:String,sourceArray:Array):int{
			var index:int=0;
			for each (var nextItem:DropdownItem in sourceArray){
				if(nextItem.data==curid){
					return index; 
				}				
				index++;
			}
			return -1;
		}
		
		public function getLabelByDataFromArray(curdata:String,sourceArray:Array):String{
			for each (var nextItem:DropdownItem in sourceArray){
				if(nextItem.data==curdata){
					return nextItem.label; 
				}
			}
			return null;
		}
		
		public function getObjectByIdFromArray(curId:String, sourceArray:Array):Object{
			for each (var nextItem:Object in sourceArray){
				if(nextItem.id.toString()==curId){
					return nextItem; 
				}
			}		
			return null;
		}	
		
		public function getDefaultValueFromArray(sourceArray:ArrayList):String{
			if (sourceArray.length > 0 ) {
				return sourceArray.getItemAt(0).data;
			}
			return null;
		}
		
		/********************************************************************************************************
		 *  CONVERT DICTIONERY INTO AN ARRAY
		 *******************************************************************************************************/
		public function convertMapToArray(map:Dictionary):Array{
			var array:Array=new Array();
			for each (var value:Object in map){
				if(value!=null){
					array.push(value);
				}
			}
			return array;
		}
		
		public function getSelectedItemByData(currentList:ArrayList,checkedValue:String):DropdownItem{
			for each(var nextData:DropdownItem in currentList.source){
				if(nextData.data==checkedValue){
					return nextData; 
				}					
			}
			return null;
		} 
		
		/********************************************************************************************************
		 *  CONVERTS AN OBJECT ARRAY INTO DROP DOWN ELEMENTS ARRAY
		 *******************************************************************************************************/
		public function convertArrayToDropdownItems(dataList:Array,idField:String,labelField:String,secondLabelField:String=null):ArrayList{					
			var dropdownArray:ArrayList= new ArrayList();
			for each(var item:Object in dataList){				
				if(secondLabelField==null){
					dropdownArray.addItem(new DropdownItem(item[idField],item[labelField]));
				}
				else{
					dropdownArray.addItem(new DropdownItem(item[idField],item[labelField]+" "+item[secondLabelField]));
				}
			}
			return dropdownArray;
		}
		
		/********************************************************************************************************
		 *  CONVERTS AN OBJECT ARRAY INTO DROP DOWN ELEMENTS ARRAY
		 *******************************************************************************************************/
		public function getIndexOfArrayByItemObject(curid:String,sourceArray:Array,fieldName:String):int{
			var index:int=0;
			for each (var nextItem:Object in sourceArray){
				if(nextItem[fieldName]==curid){
					return index; 
				}
				index++;
			}
			return -1;
		}
		
		public function getMessageLocale(resourceBundle:String, keyname:String):String{
			return ResourceManager.getInstance().getString(resourceBundle, keyname);
		}
		
		/********************************************************************************************************
		 *  UPLOAD FILE 
		 *******************************************************************************************************/		
		protected function uploadFile(url:String, byteArray:ByteArray, fileName:String, callBack:Function, parameters:Object):void {
			LOG.debug("Before sending file Content to url: " + url + ", with parameters: " + parameters);
			getHttpService().sendFile(url,byteArray,fileName, HTTPService.RESULT_FORMAT_XML, callBack, onErrorCallBack, parameters);			
		}
		
		/************************************************************************************************
		 * CALL TO THE HTTPSERVICE WITH DEFAULT TIMEOUT AND RETURN METHODS
		 *************************************************************************************************/ 
		public function getXMLContent(url:String, callBack:Function, parameters:Object=null, returnParams:Object=null):void {
			LOG.debug("Before getting XML Content from url: " + url + ", with parameters: " + parameters);
			getHttpService().getContent(url, HTTPService.RESULT_FORMAT_XML, callBack, onErrorCallBack, parameters, true, returnParams);			
		}
		
		/************************************************************************************************
		 * CALL TO THE HTTPSERVICE WITH DEFAULT TIMEOUT AND RETURN METHODS
		 *************************************************************************************************/ 
		public function getTextContent(url:String, callBack:Function, parameters:Object, returnParams:Object=null):void {
			LOG.debug("Before getting XML Content from url: " + url + ", with parameters: " + parameters);
			getHttpService().getContent(url, HTTPService.RESULT_FORMAT_TEXT, callBack, onErrorCallBack, parameters,true,returnParams);			
		}
		
		/************************************************************************************************
		 * CALL TO THE HTTPSERVICE WITH A TIMEOUT AND RETURN METHODS
		 *************************************************************************************************/
		public function getXMLContentSetTimout(url:String, callBack:Function, parameters:Object,timeout:int, returnParams:Object=null):void {
			LOG.debug("Before getting XML Content from url: " + url + ", with parameters: " + parameters);
			getHttpService().getContent(url, HTTPService.RESULT_FORMAT_XML, callBack, onErrorCallBack, parameters,true,returnParams,timeout);			
		}
		
		/************************************************************************************************
		 * CALL TO THE HTTPSERVICE , DISABLES THE BUSY CURSOR
		 *************************************************************************************************/
		public function getXMLContentNotBusyCursor(url:String, callBack:Function, parameters:Object, returnParams:Object=null):void {
			LOG.debug("Before getting XML Content from url: " + url + ", with parameters: " + parameters);
			getHttpService().getContent(url, HTTPService.RESULT_FORMAT_XML, callBack, onErrorCallBack, parameters,true,returnParams);			
		}
		
		/************************************************************************************************
		 * CALL TO THE HTTPSERVICE WITH A RETURN TO A SPECIFIC ERROR CALL BACK FUNCTION
		 *************************************************************************************************/
		protected function getXMLContentCallbackError(url:String, callBack:Function,errorCallBack:Function, parameters:Object):void {
			LOG.debug("Before getting XML Content from url: " + url + ", with parameters: " + parameters);
			getHttpService().getContent(url, HTTPService.RESULT_FORMAT_XML, callBack, errorCallBack, parameters);			
		}
		
		protected function onErrorCallBack(event:FaultEvent):void {
			throw new Error("BaseController - onErrorCallBack must be override by derived class !");
		}
	}
}