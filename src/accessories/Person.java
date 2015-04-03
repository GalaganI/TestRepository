package accessories;

public class Person {
	private String name;
	private String jobTitle;
	private String skills;
	private String description;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name =name ;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	//checks if any object field contains introduced word
    public boolean contain(String word){
    	return this.description.toLowerCase().contains(word)||this.jobTitle.toLowerCase().contains(word)||this.skills.toLowerCase().contains(word);
    }
    //print into console
    public void print(){
    	 System.out.println(" \n name="+name+"\n jobTitle="+jobTitle+"\n skills="+skills+"\n description="+description);
    }
    //method that checks objects for equality 
    public boolean equals(Person person){
        return this.name.trim().regionMatches(true, 0,person.name.trim(), 0, this.name.trim().length())&&this.jobTitle.trim().equalsIgnoreCase(person.jobTitle.trim())&&this.skills.trim().regionMatches(true, 0, person.skills.trim(), 0, this.skills.trim().length())&&this.description.trim().regionMatches(true, 0, person.description.trim(), 0, this.description.trim().length());	
    }
}
