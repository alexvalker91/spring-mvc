package alex.valker91.spring_boot.oxm;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ticket")
public class TicketXml {

	private Long user;

	private Long event;

	private String category;

	private Integer place;

	@XmlAttribute(name = "user", required = true)
	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	@XmlAttribute(name = "event", required = true)
	public Long getEvent() {
		return event;
	}

	public void setEvent(Long event) {
		this.event = event;
	}

	@XmlAttribute(name = "category", required = true)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlAttribute(name = "place", required = true)
	public Integer getPlace() {
		return place;
	}

	public void setPlace(Integer place) {
		this.place = place;
	}
}

