package br.usp.each.saeg.code.forest.xml;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="FaultClassification")
public class XmlInput {

    private static final Random random = new Random();
    private List<XmlPackage> packages = new ArrayList<XmlPackage>();

    @XmlElementWrapper(name="test-criteria") @XmlElement(name="package")
    public List<XmlPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<XmlPackage> packages) {
        this.packages = packages;
    }

    public static XmlInput unmarshal(File reportLocation) {
        try {
            JAXBContext context = JAXBContext.newInstance(XmlInput.class);
            return (XmlInput) context.createUnmarshaller().unmarshal(reportLocation);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static float nextScore() {
        return random.nextFloat();
    }
}
