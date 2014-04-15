package br.usp.each.saeg.code.forest.demo.xstream;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class Person {

    private String firstName;
    private String lastName;
    private PhoneNumber landLine;
    private PhoneNumber mobile;

    public Person() { }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PhoneNumber getLandLine() {
        return landLine;
    }
    public void setLandLine(PhoneNumber landLine) {
        this.landLine = landLine;
    }

    public PhoneNumber getMobile() {
        return mobile;
    }
    public void setMobile(PhoneNumber mobile) {
        this.mobile = mobile;
    }
}
