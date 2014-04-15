package br.usp.each.saeg.code.forest.demo.xstream;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XstreamToXmlDemo {

    public static void main(String[] args) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("person", Person.class);

        Person person = new Person("Danilo", "Mutti");
        person.setLandLine(new PhoneNumber(11, "3892-1234"));
        person.setMobile(new PhoneNumber(19, "99141-0982"));
        xstream.toXML(person, System.out);
    }
}
