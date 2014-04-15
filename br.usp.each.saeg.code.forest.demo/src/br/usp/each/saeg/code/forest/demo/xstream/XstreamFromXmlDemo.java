package br.usp.each.saeg.code.forest.demo.xstream;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XstreamFromXmlDemo {

    private static final String xml =
            "<?xml version=\"1.0\"?>           " +
            "<person>                          " +
            "  <firstName>Danilo</firstName>   " +
            "    <lastName>Mutti</lastName>    " +
            "    <landLine>                    " +
            "      <code>11</code>             " +
            "      <number>3892-1234</number>  " +
            "    </landLine>                   " +
            "    <mobile>                      " +
            "      <code>19</code>             " +
            "      <number>99141-0982</number> " +
            "    </mobile>                     " +
            "</person>                         ";

    public static void main(String[] args) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("person", Person.class);
        Person person = (Person) xstream.fromXML(xml);
        System.out.println("firstName: " + person.getFirstName());
        System.out.println("lastName: " + person.getLastName());
        System.out.println("landLine: " + person.getLandLine().getCode() + " " + person.getLandLine().getNumber());
        System.out.println("mobile: " + person.getMobile().getCode() + " " + person.getMobile().getNumber());
    }
}
