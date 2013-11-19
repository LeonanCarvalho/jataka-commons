package application.model {
	public class DomainObject {				
		public static const FIELD_ID:String = "id";
		
		[Bindable]
		[XmlAttribute(alias="id")]
		public var id:int;
		
		public function DomainObject(){
		}
		
		public function getId():int{
			return id;
		}
		
		public function setId(id:int):void{
			this.id = id;
		}
		
		public function equals(other:DomainObject):Boolean {
			if (this == other)
				return true;
			if (other == null)
				return false;
		
			if (id != other.id)
				return false;
			
			return true;
		}
	}
}