package utils.file
{
	import flash.events.Event;
	import flash.net.FileReference;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;
	
	public class FileUpload extends FileReference{
		
		public function FileUpload(){
			super();
		}
		
		// TODO Change to bytearray
		public function uploadFile(returnCompleteFunction:Function, serviceName:String):void {			
			var urlRequest:URLRequest = new URLRequest();
			urlRequest.url = serviceName;
			urlRequest.contentType = "multipart/form-data; boundary=" + UploadPostHelper.getBoundary();
			
			urlRequest.method = URLRequestMethod.POST;
			urlRequest.data = UploadPostHelper.getPostData(this.name, this.data);
			urlRequest.requestHeaders.push(new URLRequestHeader('Cache-Control', 'no-cache' ));			
			
			var urlLoader : URLLoader = new URLLoader();					
			
			urlLoader.addEventListener(Event.COMPLETE, returnCompleteFunction);
			urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
			urlLoader.load(urlRequest);
		}
	}
}