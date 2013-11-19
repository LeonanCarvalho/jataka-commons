package application.model {
	public class NameDescriptionObject extends DomainObject {
		public static const FILED_NAME:String = "name";
		
		[Bindable]
		[XmlElement(alias="NAME")]
		public var name:String;
		
		[Bindable]
		[XmlElement(alias="DESCRIPTION")]
		public var description:String;
		
		public function NameDescriptionObject() {
		}
	}
}