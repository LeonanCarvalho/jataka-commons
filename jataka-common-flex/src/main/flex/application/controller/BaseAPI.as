package application.controller {
	public class BaseAPI {
		public static const SERVICE:String = "/services";
		public static const PRIVATE_SERVICE:String = SERVICE + "/private";
		public static const PUBLIC_SERVICE:String = SERVICE + "/public";
		public static const PROTECTED_SERVICE:String = SERVICE +"/protected"  		
		
		public static const GRID_SEARCH:String = "/search";
		public static const GRID_ALL:String = "/all";
		
		public static const CRUD_DELETE:String = "/delete";
		public static const CRUD_CREATE:String = "/create";
		public static const CRUD_UPDATE:String = "/update";
		public static const GRID_COPY:String = "/copy";
		
		public static const TESTRUN_START:String = "/start";
		public static const TESTRUN_STOP:String = "/stop";
		
		public static const TESTRUN_RESULT_STATUS:String = "/status";
		public static const TESTRUN_STATUS_BYDATE:String = "/status/bydate";
		
	}
}