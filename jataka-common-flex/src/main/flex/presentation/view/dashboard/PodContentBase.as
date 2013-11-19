/*
* Base class for pod content.
*/

package presentation.view.dashboard
{
	import com.esria.samples.dashboard.managers.PodLayoutManager;
	import com.esria.samples.dashboard.view.IPodContentBase;
	import com.esria.samples.dashboard.view.Pod;
	
	import mx.controls.Alert;
	import mx.events.FlexEvent;
	import mx.events.IndexChangedEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.utils.ObjectProxy;
	
	import spark.modules.Module;
	
	//public class PodContentBase extends Module implements IPodContentBase
	public class PodContentBase extends Module implements IPodContentBase, IRefreshablePod	
	{
		private var _properties:XML; // Properties are from pods.xml.
		
		private var _pod:DashboardPod;
		private var _podMgr:PodLayoutManager;
		
		
		function PodContentBase()
		{
			super();
			percentWidth = 100;
			percentHeight = 100;
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		// sreiner made protected instead of private
		protected function onCreationComplete(e:FlexEvent):void
		{
			// sreiner addded check for no properties, no dataSource
			if ( properties != null)
			{
				// Load the data source.
				var httpService:HTTPService = new HTTPService();
				httpService.url = properties.@dataSource;
				// sreiner addded check for no dataSource
				if (httpService.url != "")
				{
					httpService.resultFormat = "e4x";
					httpService.addEventListener(FaultEvent.FAULT, onFaultHttpService);
					httpService.addEventListener(ResultEvent.RESULT, onResultHttpService);
					httpService.send();
				}
			}
		}
		
		private function onFaultHttpService(e:FaultEvent):void
		{
			Alert.show("Unable to load datasource, " + properties.@dataSource + ".");
		}
		
		// abstract.
		protected function onResultHttpService(e:ResultEvent):void	{}
		
		// Converts XML attributes in an XMLList to an Array.
		protected function xmlListToObjectArray(xmlList:XMLList):Array
		{
			var a:Array = new Array();
			for each(var xml:XML in xmlList)
			{
				var attributes:XMLList = xml.attributes();
				var o:Object = new Object();
				for each (var attribute:XML in attributes)
				{
					var nodeName:String = attribute.name().toString();
					var value:*;
					if (nodeName == "date")
					{
						var date:Date = new Date();
						date.setTime(Number(attribute.toString()));
						value = date;
					}
					else
					{
						value = attribute.toString();
					}
					
					o[nodeName] = value;
				}
				
				a.push(new ObjectProxy(o));
			}
			
			return a;
		}
		
		// Dispatches an event when the ViewStack index changes, which triggers a state save.
		// ViewStacks are only in ChartContent and FormContent.
		protected function dispatchViewStackChange(newIndex:Number):void
		{
			dispatchEvent(new IndexChangedEvent(IndexChangedEvent.CHANGE, true, false, null, -1, newIndex));
		}
		
		[Bindable]
		public function get properties():XML
		{
			return _properties;
		}
		
		public function set properties(value:XML):void
		{
			_properties = value;
		}
		
		[Bindable]
		public function get pod():Pod
		{
			return _pod;
		}
		
		public function set pod(pod:Pod):void
		{
			_pod = pod as DashboardPod;
		}
		
		[Bindable]
		public function get podManager():PodLayoutManager
		{
			return _podMgr;
		}
		
		public function set podManager(podMgr:PodLayoutManager):void
		{
			_podMgr = podMgr;
		}	
		
		public function onRefreshData(e:FlexEvent):void {
			this.refreshData();		
		}
		
		public function refreshData():void {
			throw new Error("PodContentBase - refreshData must be override by derived module/pod.");
		}
		
	}
}