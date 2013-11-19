package application.controller.http
{   
    import flash.events.EventDispatcher;
    import flash.external.ExternalInterface;
    import flash.net.URLRequest;
    import flash.net.navigateToURL;
    import flash.utils.ByteArray;
    import flash.xml.XMLNode;
    
    import mx.messaging.messages.HTTPRequestMessage;
    import mx.rpc.AsyncResponder;
    import mx.rpc.AsyncToken;
    import mx.rpc.IResponder;
    import mx.rpc.events.FaultEvent;
    import mx.rpc.events.ResultEvent;
    import mx.rpc.http.HTTPService;
    
    import utils.file.UploadPostHelper;
    import utils.string.StringUtils;
    
	public class HttpServiceWrapper extends EventDispatcher{
		
    	private static const HTTP_SERVICE_TIME_OUT:int = 180; // Client timeout in seconds
    	private static const HTTP_SERVICE_METHOD:String = HTTPRequestMessage.POST_METHOD;
		private static const HTTP_DEFAULT_CUSTOM_TIMEOUT:int = -1;
		
		// Error codes
		public static const SC_INTERNAL_SERVER_ERROR:int = 500;
		public static const SC_SERVICE_UNAVAILABLE:int = 503;
		public static const SC_MOVED_PERMANENTLY:int = 301; 
		public static const SC_MOVED_TEMPORARILY:int = 302;
		public static const SC_UNAUTHORIZED:int = 401;
		public static const SC_FORBIDDEN:int = 403;
		
		
        private static var httpService:HTTPService;       
		private static var processingQueue:Object = {};
		private static var instance:HttpServiceWrapper;
						
		public static function getInstance():HttpServiceWrapper {
			if (instance==null){
				initialize();	
			}
			
			return instance;
		}
		
        private static function initialize():void {
			instance = new HttpServiceWrapper(HttpServiceEnforcer);
            //create the service
            httpService = new HTTPService;
            
            // custom settings
            httpService.requestTimeout = HTTP_SERVICE_TIME_OUT;		
            httpService.method = HTTP_SERVICE_METHOD;
			
            var customHeaders:Object = new Object();
			customHeaders["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8";
			httpService.headers = customHeaders; 
        }
		
		public function HttpServiceWrapper(enforcer:Class):void {
			if(enforcer != HttpServiceEnforcer) {
				throw new Error("Use HttpServiceWrapper.getInstance to access !!!");
			}	
		}
        
		public function sendFile(url:String,
								 	bytesArray:ByteArray,
								 	fileName:String,
									resultFormat:String = null,
									callBack:Function = null,
									faultCallback:Function = null,
									requestParameters:Object = null,							
									returnErrorEvent:Boolean = true,
									customTimeout:int = HTTP_DEFAULT_CUSTOM_TIMEOUT
								   ) : void {			
			
			var asyncToken:AsyncToken;
			var internalIResponder:IResponder;
											
			var customHeaders:Object = new Object();
			customHeaders["Content-Type"] = "multipart/form-data; charset=UTF-8";
			httpService.headers = customHeaders; 
			
			
			if (customTimeout != HTTP_DEFAULT_CUSTOM_TIMEOUT) // useful for lengthy operations
				httpService.requestTimeout = customTimeout;
			else
				httpService.requestTimeout = HTTP_SERVICE_TIME_OUT;
			
			httpService.contentType="multipart/form-data; boundary=" + UploadPostHelper.getBoundary();
			httpService.url = "" + url;
			httpService.resultFormat = resultFormat != null ? resultFormat : HTTPService.RESULT_FORMAT_OBJECT; 
			
			// Request parameters
			if (requestParameters != null) {
				httpService.request = requestParameters;								
			}
			
			// send the request, getting back its token
			asyncToken = httpService.send();     
			
			internalIResponder = new AsyncResponder(onResult, onFault, asyncToken);
			asyncToken.addResponder(internalIResponder);
			
			// give token unique ID
			asyncToken = tokenID(asyncToken);
			
			// store the request details
			processingQueue[asyncToken.ID] = new QueueObject(httpService.url, callBack, faultCallback, httpService.resultFormat, returnErrorEvent);           
		}
		
	    /**
         * Get content from URL 
         * @param url
         * @param resultFormat
         * @param callBack
         * @param faultCallback - NOTE: must also except Null (in case of ERROR)
         * @param request
         * @param returnErrorEvent
         * @param customTimeout - send 0 for no timeout, or -1 (default) for default timeout
         * 
         */
        public function getContent(url:String,
                                   resultFormat:String = null,
                                   callBack:Function = null,
                                   faultCallback:Function = null,
                                   requestParameters:Object = null,						   
                                   returnErrorEvent:Boolean = true,
								   returnParams:Object=null,
                                   customTimeout:int = HTTP_DEFAULT_CUSTOM_TIMEOUT								   
								   ) : void 
        {		
            var asyncToken:AsyncToken;
            var internalIResponder:IResponder;
                 
            if (customTimeout != HTTP_DEFAULT_CUSTOM_TIMEOUT) // useful for lengthy operations
            	httpService.requestTimeout = customTimeout;
            else
            	httpService.requestTimeout = HTTP_SERVICE_TIME_OUT;
									
 			httpService.url = StringUtils.EMPTY + url;
            httpService.resultFormat = resultFormat != null ? resultFormat : HTTPService.RESULT_FORMAT_OBJECT; 
            
			// Request parameters
            if (requestParameters != null){
				httpService.request = requestParameters;								
            }

            // send the request, getting back its token
            asyncToken = httpService.send();     
			
            internalIResponder = new AsyncResponder(onResult, onFault, asyncToken);
            asyncToken.addResponder(internalIResponder);
            
            // give token unique ID
            asyncToken = tokenID(asyncToken);
													
           // store the request details
            processingQueue[asyncToken.ID] = new QueueObject(httpService.url, callBack, faultCallback, httpService.resultFormat, returnErrorEvent, returnParams);           
		}
		
        // This is the handler event for getContent. The callBack should accept a ResultEvent
		// We delegate the callBack so we can add global behaviour
        private function onResult(event:ResultEvent, token:AsyncToken):void {
        	// get call object
        	var queueObject:QueueObject = processingQueue[token.ID];

			if(event.result != StringUtils.EMPTY) {
				isLoginResponse(event, token, queueObject);
				
				if((processingQueue[token.ID] as QueueObject).returnParams!=null)
					queueObject.callBack(event.result, (processingQueue[token.ID] as QueueObject).returnParams);
				else
					queueObject.callBack(event.result);
			}
			
			// Clean ups
			queueObject = null; 
			delete processingQueue[token.ID];
        }
		
		private function isLoginResponse(event:ResultEvent, token:AsyncToken, queueObject:QueueObject): void{
			// If response was redirected and response content contain j_spring_security_check - Navigate to the same url
			if (event.message.body.toString().indexOf("j_spring_security_check") != -1){					
				var url:URLRequest = new URLRequest(queueObject.originalUrl);

				navigateToURL(url, "_self");
			}
		}
          
        // This is the fault event for the class. The callBack should accept a null ArrayCollection and a FaultEvent if
        // you want to handle the error.
        private function onFault(event:FaultEvent, token:AsyncToken):void {
        	var queueObject:QueueObject = processingQueue[token.ID];
        	
    		queueObject.faultCallback(event);       
            queueObject = null; // clean up
            delete processingQueue[token.ID];
        }
   
        // add a unique identifier to a token
        private function tokenID(token:AsyncToken):AsyncToken {
            token.ID = Math.random();
            return token;
        }        
    }
}

class QueueObject {
	public var originalUrl:String;
    public var callBack:Function;
    public var faultCallback:Function;
    public var resultFormat:String;
    public var returnErrorEvent:Object;
    public var returnParams:Object;
	
    public function QueueObject(originalUrl:String, callBack:Function, faultCallback:Function, resultFormat:String, returnErrorEvent:Boolean = true,returnParams:Object=null){
        this.originalUrl = originalUrl;
		this.callBack = callBack;
        this.faultCallback = faultCallback;
        this.resultFormat = resultFormat;
        this.returnErrorEvent = returnErrorEvent;
		this.returnParams=returnParams;
    }
}

class HttpServiceEnforcer {}