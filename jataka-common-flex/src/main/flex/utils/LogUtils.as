package utils{
	import flash.utils.getQualifiedClassName;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.logging.LogEventLevel;
	import mx.logging.targets.TraceTarget;

	public class LogUtils{
		public function LogUtils(){
			
		}
		
		public static function getLogger(theClass:Class):ILogger {
			// TODO add logger framework like log4j

//			var logTarget:TraceTarget = new TraceTarget();
//			logTarget.level = LogEventLevel.INFO;
//			//logTarget.filters=["flex.grid.*"];
//			
//			Log.addTarget(logTarget);
			return Log.getLogger(getQualifiedClassName(theClass).replace("::", "."));
		}
	}
}