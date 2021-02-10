package aero.minova.test.saw.rcp.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	private final long groupID;
	private List<Contact> members;
	private String name;
	
	public Group(List<Contact> members, String name, long id) {
		if (members.size() > 0) {
			this.members = members;
		} else {
			this.members = new ArrayList<Contact>();
		}
		this.groupID = id;
		this.name = name;
	}
	
	public List<Contact> addMember(Contact c) {
		members.add(c);
		return members;
	}
	
	public List<Contact> removeMember(Contact c) {
		if (members.contains(c)) {
			members.remove(c);
		}
		
		return members;
	}

	public List<Contact> getMembers() {
		return members;
	}

	public void setMembers(List<Contact> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getGroupID() {
		return groupID;
	}
}
