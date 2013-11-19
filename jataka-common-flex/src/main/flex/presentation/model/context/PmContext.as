package presentation.model.context {
	import application.model.security.IUser;
	
	public class PmContext {
		private var user:IUser;
		
		public function PmContext(user:IUser) {
			this.user = user;
		}
		
		public function getUser():IUser {
			return this.user;
		}
	}
}